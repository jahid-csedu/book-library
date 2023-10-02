package com.example.library.presentation.integration

import com.example.library.domain.repositories.PublisherRepository
import com.example.library.presentation.dtos.PublisherDto
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class PublisherIntegrationTest {

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
    private lateinit var publisherRepository: PublisherRepository

    @AfterEach
    fun cleanup() {
        publisherRepository.deleteAll()
    }

    @Test
    fun `should create a new publisher`() {
        val publisherDto = PublisherDto(1, "Mars")

        mockMvc.post("/publishers") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(publisherDto)
        }
            .andDo { print() }
            .andExpect {
                status { isCreated() }
                jsonPath("$.id") { value(publisherDto.id) }
                jsonPath("$.name") { value(publisherDto.name) }
            }
            .andReturn()
    }

    @Test
    @Sql("/sql/publisher-data.sql")
    fun `should update a publisher`() {
        val publisherId = 1L
        val publisherDto = PublisherDto(publisherId, "Galaxy")

        mockMvc.put("/publishers/${publisherId}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(publisherDto)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.id") { value(publisherId) }
                jsonPath("$.name") { value(publisherDto.name) }
            }
            .andReturn()
    }

    @Test
    @Sql("/sql/publisher-data.sql")
    fun `should delete a publisher by id`() {
        val publisherId = 1L

        mockMvc.delete("/publishers/${publisherId}")
            .andDo { print() }
            .andExpect {
                status { isNoContent() }
            }
            .andReturn()
    }

    @Test
    @Sql("/sql/publisher-data.sql")
    fun `should get a publisher by id`() {
        val publisherId = 1L
        val publisherDto = PublisherDto(publisherId, "Mars")

        mockMvc.get("/publishers/${publisherId}")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.id") { value(publisherId) }
                jsonPath("$.name") { value(publisherDto.name) }
            }
            .andReturn()
    }

    @Test
    @Sql("/sql/publisher-data.sql")
    fun `should get paginated publisher list`() {

        val expectedResponseJson = javaClass.getResource("/integration/publisher_response.json").readText()


        mockMvc.perform(
            MockMvcRequestBuilders.get("/publishers")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(expectedResponseJson))
    }

}