package com.example.library.presentation.controllers

import com.example.library.application.services.BookService
import com.example.library.presentation.controllers.config.BookControllerTestConfig
import com.example.library.presentation.dtos.BookDto
import com.example.library.presentation.dtos.BookSearchDto
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageImpl
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.Year

@WebMvcTest(BookController::class)
@Import(BookControllerTestConfig::class)
class BookControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var bookService: BookService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var bookDto1: BookDto
    private lateinit var bookDto2: BookDto

    @BeforeEach
    fun setup() {
        bookDto1 = BookDto(
            id = 1L,
            isbn = "978-0451169518",
            title = "To Kill a Mockingbird",
            authorId = 2L,
            categoryId = 3L,
            publisherId = 4L,
            publicationYear = Year.of(1960),
            price = BigDecimal("12.99")
        )

        bookDto2 = BookDto(
            id = 2L,
            isbn = "978-0142416468",
            title = "The Catcher in the Rye",
            authorId = 1L,
            categoryId = 5L,
            publisherId = 6L,
            publicationYear = Year.of(1937),
            price = BigDecimal("9.99")
        )
    }

    @Test
    fun `should create a new book`() {

        every { bookService.createBook(any()) } returns bookDto1

        mockMvc.post("/books") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(bookDto1)
        }
            .andDo { print() }
            .andExpect {
                status { isCreated() }
                verify(exactly = 1) { bookService.createBook(bookDto1) }
                jsonPath("$.id") { value(bookDto1.id) }
                jsonPath("$.title") { value(bookDto1.title) }
            }
            .andReturn()
    }

    @Test
    fun `should update a book`() {
        val bookId = 1L

        every { bookService.updateBook(any(), any()) } returns bookDto1

        mockMvc.put("/books/${bookId}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(bookDto1)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                verify(exactly = 1) { bookService.updateBook(bookId, bookDto1) }
                jsonPath("$.id") { value(bookId) }
                jsonPath("$.title") { value(bookDto1.title) }
            }
            .andReturn()
    }

    @Test
    fun `should delete a book by id`() {
        val bookId = 1L

        justRun { bookService.deleteBook(any()) }

        mockMvc.delete("/books/${bookId}")
            .andDo { print() }
            .andExpect {
                status { isNoContent() }
                verify(exactly = 1) { bookService.deleteBook(bookId) }
            }
            .andReturn()
    }

    @Test
    fun `should get a book by id`() {
        val bookId = 1L

        every { bookService.getBookById(any()) } returns bookDto1

        mockMvc.get("/books/${bookId}")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                verify(exactly = 1) { bookService.getBookById(bookId) }
                jsonPath("$.id") { value(bookId) }
                jsonPath("$.title") { value(bookDto1.title) }
            }
            .andReturn()
    }

    @Test
    fun `should get paginated book list`() {
        val books = listOf(bookDto1, bookDto2)
        val searchDto = BookSearchDto.Builder()
            .priceFrom(BigDecimal("8.00"))
            .priceTo(BigDecimal("15.00"))
            .build()

        every { bookService.searchBook(any(), any()) } returns PageImpl(books)

        val expectedResponseJson = javaClass.getResource("/unit/book_search_response.json").readText()


        mockMvc.perform(
            post("/books/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchDto))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(expectedResponseJson))
            .andReturn()
        verify(exactly = 1) { bookService.searchBook(any(), any()) }
    }
}