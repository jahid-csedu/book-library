package com.example.library.application.services

import com.example.library.domain.entities.Book
import com.example.library.domain.repositories.BookRepository
import com.example.library.presentation.dtos.BookDto
import com.example.library.presentation.dtos.BookSearchDto
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.math.BigDecimal
import java.time.Year
import java.util.Optional

class BookServiceTest {

    private val bookRepository: BookRepository = mockk()
    private val bookService: BookService = BookService(bookRepository)
    private lateinit var bookDto1: BookDto
    private lateinit var bookDto2: BookDto
    private lateinit var book1: Book
    private lateinit var book2: Book

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
            publicationYear = Year.of(1951),
            price = BigDecimal("9.99")
        )
        book1 = Book(
            id = 1L,
            isbn = "978-0451169518",
            title = "To Kill a Mockingbird",
            authorId = 2L,
            categoryId = 3L,
            publisherId = 4L,
            publicationYear = Year.of(1960),
            price = BigDecimal("12.99")
        )

        book2 = Book(
            id = 2L,
            isbn = "978-0142416468",
            title = "The Catcher in the Rye",
            authorId = 1L,
            categoryId = 5L,
            publisherId = 6L,
            publicationYear = Year.of(1951),
            price = BigDecimal("9.99")
        )
    }

    @Test
    fun `should create a new book`() {

        every { bookRepository.save(any()) } returns book1

        val book = bookService.createBook(bookDto1)

        verify(exactly = 1) { bookRepository.save(any()) }
        assert(book.id == bookDto1.id)
        assert(book.isbn == bookDto1.isbn)
        assert(book.title == bookDto1.title)
        assert(book.publicationYear == bookDto1.publicationYear)
        assert(book.price == bookDto1.price)
    }

    @Test
    fun `should update a book`() {
        val bookId = 1L

        every { bookRepository.findById(any()) } returns Optional.of(book1)
        every { bookRepository.save(any()) } returns book1

        val book = bookService.updateBook(bookId, bookDto1)

        verify(exactly = 1) { bookRepository.save(any()) }
        assert(book.id == bookDto1.id)
        assert(book.isbn == bookDto1.isbn)
        assert(book.title == bookDto1.title)
        assert(book.publicationYear == bookDto1.publicationYear)
        assert(book.price == bookDto1.price)
    }

    @Test
    fun `should delete a book by id`() {
        val bookId = 1L

        every { bookRepository.findById(any()) } returns Optional.of(book1)
        justRun { bookRepository.deleteById(any()) }

        bookService.deleteBook(bookId)

        verify(exactly = 1) { bookRepository.deleteById(bookId) }
    }

    @Test
    fun `should get a book by id`() {
        val bookId = 1L

        every { bookRepository.findById(any()) } returns Optional.of(book1)

        val book = bookService.getBookById(bookId)

        verify(exactly = 1) { bookRepository.findById(bookId) }
        assert(book.id == bookDto1.id)
        assert(book.isbn == bookDto1.isbn)
        assert(book.title == bookDto1.title)
        assert(book.publicationYear == bookDto1.publicationYear)
        assert(book.price == bookDto1.price)
    }

    @Test
    fun `should search books by different search parameters`() {

        val searchDto = BookSearchDto.Builder()
            .priceFrom(BigDecimal("5.00"))
            .priceTo(BigDecimal("20.00"))
            .build()

        val page: Page<Book> = PageImpl(listOf(book1, book2))
        every {
            bookRepository.searchBook(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(Pageable::class)
            )
        } returns page

        val pageable = PageRequest.of(0, 10)
        val books = bookService.searchBook(searchDto, pageable)

        assertEquals(2, books.totalElements)
    }
}