package com.example.library.presentation.integration

import com.example.library.domain.repositories.AuthorRepository
import com.example.library.presentation.dtos.AuthorDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class AuthorIntegrationTest {

    companion object {
        @Container
        val postgreSQLContainer = PostgreSQLContainer(DockerImageName.parse("postgres:13.3"))
            .apply {
                this.withDatabaseName("testDb")
                    .withUsername("user")
                    .withPassword("password")
                    .withInitScript("schema.sql")
            }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword)
        }

        @JvmStatic
        @BeforeAll
        internal fun setUp() {
            postgreSQLContainer.start()
        }

        @JvmStatic
        @AfterAll
        internal fun cleanUp() {
            postgreSQLContainer.stop()
        }
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var authorRepository: AuthorRepository

    @AfterEach
    fun cleanup() {
        authorRepository.deleteAll()
    }

    @Test
    fun `should create an author`() {
        val authorDto = """
            {
                "name": "John Doe"
            }
        """

        mockMvc.post("/authors") {
            contentType = MediaType.APPLICATION_JSON
            content = authorDto
        }
            .andExpect {
                status { isCreated() }
            }
    }

    @Test
    @Sql("/sql/author-data.sql")
    fun `should update an author`() {
        val authorId = 1L
        val authorDto = AuthorDto(authorId, "Michael")

        mockMvc.put("/authors/${authorId}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(authorDto)
        }
            .andExpect {
                status { isOk() }
                jsonPath("$.name") { value("Michael") }
            }
    }

    @Test
    @Sql("/sql/author-data.sql")
    fun `should delete an author by id`() {
        val authorId = 1L

        mockMvc.delete("/authors/${authorId}")
            .andExpect {
                status { isNoContent() }
            }
    }

    @Test
    @Sql("/sql/author-data.sql")
    fun `should get an author by id`() {
        val authorId = 1L

        mockMvc.get("/authors/${authorId}")
            .andExpect {
                status { isOk() }
                jsonPath("$.name") { value("John Doe") }
            }
    }

    @Test
    @Sql("/sql/author-data.sql")
    fun `should get paginated authors`() {
        val expected = javaClass.getResource("/integration/author_response.json").readText()

        mockMvc.perform(
            get("/authors")
                .param("page", "0")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(expected))
    }
}
