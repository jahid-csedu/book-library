package com.example.library.presentation.integration

import com.example.library.domain.repositories.CategoryRepository
import com.example.library.presentation.dtos.CategoryDto
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
class CategoryIntegrationTest {

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
    private lateinit var categoryRepository: CategoryRepository

    @AfterEach
    fun cleanup() {
        categoryRepository.deleteAll()
    }

    @Test
    @Sql("/sql/category-data.sql")
    fun `should get all categories`() {

        val expectedResponseJson = javaClass.getResource("/integration/category_response.json").readText()

        mockMvc.perform(
            MockMvcRequestBuilders.get("/categories")
                .param("page", "0")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(expectedResponseJson))
    }

    @Test
    @Sql("/sql/category-data.sql")
    fun `should get category by id`() {
        val categoryId = 1L
        val category = CategoryDto(id = categoryId, name = "IT")

        mockMvc.get("/categories/${categoryId}")
            .andDo { print() }
            .andExpect {
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(category.id) }
                jsonPath("$.name") { value(category.name) }
            }
            .andReturn()
    }

    @Test
    fun `should create a new category`() {
        val categoryDto = CategoryDto(id = 1, name = "Comic")

        mockMvc.post("/categories") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(categoryDto)
        }
            .andDo { print() }
            .andExpect {
                status { isCreated() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(categoryDto.id) }
                jsonPath("$.name") { value(categoryDto.name) }
            }
            .andReturn()
    }

    @Test
    @Sql("/sql/category-data.sql")
    fun `should update a category`() {
        val categoryId = 1L
        val categoryDto = CategoryDto(id = categoryId, name = "Comic")

        mockMvc.put("/categories/${categoryId}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(categoryDto)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.name") { value("Comic") }
            }
            .andReturn()
    }

    @Test
    @Sql("/sql/category-data.sql")
    fun `should delete a category`() {
        val categoryId = 1L

        mockMvc.delete("/categories/${categoryId}")
            .andDo { print() }
            .andExpect {
                status { isNoContent() }
            }
            .andReturn()
    }
}