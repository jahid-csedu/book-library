package com.example.library.presentation.controllers

import com.example.library.application.services.BookService
import com.example.library.presentation.dtos.BookDto
import com.example.library.presentation.dtos.BookSearchDto
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class BookController(private val bookService: BookService) {
    @PostMapping
    fun createBook(@RequestBody @Valid bookDto: BookDto): ResponseEntity<BookDto> {
        val book = bookService.createBook(bookDto)
        return ResponseEntity(book, HttpStatus.CREATED)
    }

    @PutMapping("/{bookId}")
    fun updateBook(@PathVariable bookId: Long, @RequestBody @Valid bookDto: BookDto): ResponseEntity<BookDto> {
        val book = bookService.updateBook(bookId, bookDto)
        return ResponseEntity(book, HttpStatus.OK)
    }

    @DeleteMapping("/{bookId}")
    fun deleteBook(@PathVariable bookId: Long): ResponseEntity<Unit> {
        val book = bookService.deleteBook(bookId)
        return ResponseEntity(book, HttpStatus.NO_CONTENT)
    }

    @GetMapping("/{bookId}")
    fun getBookById(@PathVariable bookId: Long): ResponseEntity<BookDto> {
        val book = bookService.getBookById(bookId)
        return ResponseEntity(book, HttpStatus.OK)
    }

    @PostMapping("/search")
    fun searchBook(
        @RequestBody searchDto: BookSearchDto,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int
    ): ResponseEntity<Page<BookDto>> {
        val pageable = PageRequest.of(page, size)
        val books = bookService.searchBook(searchDto, pageable)
        return ResponseEntity(books, HttpStatus.OK)
    }
}