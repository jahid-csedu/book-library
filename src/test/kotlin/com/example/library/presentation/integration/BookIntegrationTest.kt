package com.example.library.presentation.integration

import com.example.library.domain.repositories.BookRepository
import com.example.library.presentation.dtos.BookDto
import com.example.library.presentation.dtos.BookSearchDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
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
import java.math.BigDecimal
import java.time.Year

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class BookIntegrationTest {
    companion object {
        @Container
        val postgreSQLContainer = PostgreSQLContainer(DockerImageName.parse("postgres:13.3"))
            .apply {
                this.withDatabaseName("testDb")
                    .withUsername("user")
                    .withPassword("password")
                    .withInitScript("schema-with-data.sql")
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
    private lateinit var bookRepository: BookRepository

    private lateinit var bookDto1: BookDto
    private lateinit var bookDto2: BookDto


    @BeforeEach
    fun setup() {
        bookDto1 = BookDto(
            id = 1L,
            isbn = "978-0451169518",
            title = "To Kill a Mockingbird",
            authorId = 1L,
            categoryId = 1L,
            publisherId = 1L,
            publicationYear = Year.of(1960),
            price = BigDecimal("12.99")
        )

        bookDto2 = BookDto(
            id = 2L,
            isbn = "978-0142416468",
            title = "The Catcher in the Rye",
            authorId = 2L,
            categoryId = 2L,
            publisherId = 2L,
            publicationYear = Year.of(1937),
            price = BigDecimal("9.99")
        )
    }

    @AfterEach
    fun cleanup() {
        bookRepository.deleteAll()
    }

    @Test
    fun `should create a new book`() {

        mockMvc.post("/books") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(bookDto1)
        }
            .andDo { print() }
            .andExpect {
                status { isCreated() }
                jsonPath("$.id") { value(bookDto1.id) }
                jsonPath("$.title") { value(bookDto1.title) }
            }
            .andReturn()
    }

    @Test
    @Sql("/sql/book-data.sql")
    fun `should update a book`() {
        val bookId = 1L
        val updatedDto = BookDto(
            id = 1L,
            isbn = "978-0451169518",
            title = "Lords of the Rings",
            authorId = 1L,
            categoryId = 1L,
            publisherId = 1L,
            publicationYear = Year.of(1960),
            price = BigDecimal("15.99")
        )

        mockMvc.put("/books/${bookId}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(updatedDto)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.id") { value(bookId) }
                jsonPath("$.title") { value(updatedDto.title) }
            }
            .andReturn()
    }

    @Test
    @Sql("/sql/book-data.sql")
    fun `should delete a book by id`() {
        val bookId = 1L

        mockMvc.delete("/books/${bookId}")
            .andDo { print() }
            .andExpect {
                status { isNoContent() }
            }
            .andReturn()
    }

    @Test
    @Sql("/sql/book-data.sql")
    fun `should get a book by id`() {
        val bookId = 1L

        mockMvc.get("/books/${bookId}")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.id") { value(bookId) }
                jsonPath("$.title") { value(bookDto1.title) }
            }
            .andReturn()
    }

    @Test
    @Sql("/sql/book-data.sql")
    fun `should get paginated book list`() {
        val searchDto = BookSearchDto.Builder()
            .priceFrom(BigDecimal("8.00"))
            .priceTo(BigDecimal("15.00"))
            .build()

        val expectedResponseJson = javaClass.getResource("/integration/book_response.json").readText()


        mockMvc.perform(
            MockMvcRequestBuilders.post("/books/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchDto))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(expectedResponseJson))
            .andReturn()
    }
}