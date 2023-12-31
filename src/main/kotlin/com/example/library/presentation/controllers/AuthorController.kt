package com.example.library.presentation.controllers

import com.example.library.application.services.AuthorService
import com.example.library.presentation.dtos.AuthorDto
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
@RequestMapping("/authors")
class AuthorController(private val authorService: AuthorService) {

    @PostMapping
    fun createAuthor(@RequestBody @Valid authorDto: AuthorDto): ResponseEntity<AuthorDto> {
        val createdAuthor = authorService.createAuthor(authorDto)
        return ResponseEntity(createdAuthor, HttpStatus.CREATED)
    }

    @GetMapping("/{authorId}")
    fun getAuthorById(@PathVariable authorId: Long): ResponseEntity<AuthorDto> {
        val authorDto = authorService.getAuthorById(authorId)
        return ResponseEntity(authorDto, HttpStatus.OK)
    }

    @PutMapping("/{authorId}")
    fun updateAuthor(
        @PathVariable authorId: Long,
        @RequestBody @Valid updatedAuthorDto: AuthorDto
    ): ResponseEntity<AuthorDto> {
        val updatedAuthor = authorService.updateAuthor(authorId, updatedAuthorDto)
        return ResponseEntity(updatedAuthor, HttpStatus.OK)
    }

    @DeleteMapping("/{authorId}")
    fun deleteAuthor(@PathVariable authorId: Long): ResponseEntity<Unit> {
        authorService.deleteAuthor(authorId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @GetMapping
    fun searchAuthors(@RequestParam(defaultValue = "0") page: Int,
                      @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<AuthorDto>> {
        val pageable = PageRequest.of(page, size)
        val authors = authorService.searchAuthors(pageable)
        return ResponseEntity(authors, HttpStatus.OK)
    }
}
