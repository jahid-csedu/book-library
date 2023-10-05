package com.example.library.application.services

import com.example.library.application.mappers.BookMapper
import com.example.library.common.errors.ErrorCode
import com.example.library.domain.entities.Book
import com.example.library.domain.repositories.BookRepository
import com.example.library.presentation.dtos.BookDto
import com.example.library.presentation.dtos.BookSearchDto
import com.example.library.presentation.exceptions.RequestValidationException
import com.example.library.presentation.exceptions.ResourceNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
@CacheConfig(cacheNames = ["books"])
class BookService(private val bookRepository: BookRepository) {

    val logger: Logger = LoggerFactory.getLogger(BookService::class.java)

    fun createBook(bookDto: BookDto): BookDto {
        logger.info("book create request: {}", bookDto)
        val book = bookRepository.save(BookMapper.INSTANCE.toEntity(bookDto))
        return BookMapper.INSTANCE.toDto(book)
    }

    @CachePut(key = "#bookId")
    fun updateBook(bookId: Long, bookDto: BookDto): BookDto {
        logger.info("book update request: {}", bookDto)
        validateUpdateRequest(bookId, bookDto)
        val book = findBookByIdOrElseThrow(bookId)
        BookMapper.INSTANCE.toUpdateEntity(bookDto, book)
        val updatedBook = bookRepository.save(book)
        return BookMapper.INSTANCE.toDto(updatedBook)
    }

    @CacheEvict(key = "#bookId")
    fun deleteBook(bookId: Long) {
        logger.info("Deleting book by ID: {}", bookId)
        findBookByIdOrElseThrow(bookId)
        bookRepository.deleteById(bookId)
    }

    @Cacheable(key = "#bookId")
    fun getBookById(bookId: Long): BookDto {
        logger.info("Getting book by ID: {}", bookId)
        val book = findBookByIdOrElseThrow(bookId)
        return BookMapper.INSTANCE.toDto(book)
    }

    fun searchBook(searchDto: BookSearchDto, pageable: Pageable): Page<BookDto> {
        logger.info("Book Search request: {}", searchDto)
        val books = bookRepository.searchBook(
            searchDto.isbn,
            searchDto.title,
            searchDto.authorId,
            searchDto.categoryId,
            searchDto.publisherId,
            searchDto.publicationYear,
            searchDto.priceFrom,
            searchDto.priceTo,
            pageable
        )

        return books.map { BookMapper.INSTANCE.toDto(it) }
    }

    private fun validateUpdateRequest(bookId: Long, bookDto: BookDto) {
        if (bookId != bookDto.id) {
            throw RequestValidationException(ErrorCode.ID_MISSMATCH)
        }
    }

    private fun findBookByIdOrElseThrow(bookId: Long): Book = bookRepository.findById(bookId)
        .orElseThrow { ResourceNotFoundException(ErrorCode.BOOK_NOT_FOUND) }
}