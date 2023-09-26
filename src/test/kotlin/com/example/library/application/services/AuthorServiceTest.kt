package com.example.library.application.services

import com.example.library.common.errors.ErrorCode
import com.example.library.domain.entities.Author
import com.example.library.domain.repositories.AuthorRepository
import com.example.library.presentation.dtos.AuthorDto
import com.example.library.presentation.exceptions.RequestValidationException
import com.example.library.presentation.exceptions.ResourceNotFoundException
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.util.Optional

internal class AuthorServiceTest {

    private val authorRepository: AuthorRepository = mockk()
    private val authorService: AuthorService = AuthorService(authorRepository)

    @Test
    fun `create author`() {
        // Arrange
        val authorDto = AuthorDto(id = 1, name = "John Doe")
        val author = Author(id = 1, name = "John Doe")

        // Mock behavior for the authorRepository
        every { authorRepository.save(any()) } returns author

        // Act
        val createdAuthor = authorService.createAuthor(authorDto)

        // Assert
        verify(exactly = 1) { authorRepository.save(any()) }
        assert(createdAuthor.id == authorDto.id)
        assert(createdAuthor.name == authorDto.name)
    }

    @Test
    fun `get author by id`() {
        // Arrange
        val authorId = 1L
        val author = Author(id = authorId, name = "John Doe")

        // Mock behavior for the authorRepository
        every { authorRepository.findById(authorId) } returns Optional.of(author)

        // Act
        val authorDto = authorService.getAuthorById(authorId)

        // Assert
        verify(exactly = 1) { authorRepository.findById(authorId) }
        assert(authorDto.id == author.id)
        assert(authorDto.name == author.name)
    }

    @Test
    fun `get author by id when author not found then throws exception`() {
        // Arrange
        val authorId = 1L

        // Mock behavior for the authorRepository
        every { authorRepository.findById(authorId) } returns Optional.empty()

        // Act and Assert
        val exception = assertThrows<ResourceNotFoundException> {
            authorService.getAuthorById(authorId)
        }

        // Assert
        assert(exception.error == ErrorCode.AUTHOR_NOT_FOUND)
    }

    @Test
    fun `update author`() {
        // Arrange
        val authorId = 1L
        val updatedAuthorDto = AuthorDto(id = authorId, name = "Updated Name")
        val existingAuthor = Author(id = authorId, name = "John Doe")

        // Mock behavior for the authorRepository
        every { authorRepository.findById(authorId) } returns Optional.of(existingAuthor)
        every { authorRepository.save(any()) } returns existingAuthor

        // Act
        val updatedAuthor = authorService.updateAuthor(authorId, updatedAuthorDto)

        // Assert
        verify(exactly = 1) { authorRepository.findById(authorId) }
        verify(exactly = 1) { authorRepository.save(any()) }
        assert(updatedAuthor.id == updatedAuthorDto.id)
        assert(updatedAuthor.name == updatedAuthorDto.name)
    }

    @Test
    fun `when update request with mismatched ids then throws exception`() {
        // Arrange
        val authorId = 1L
        val updatedAuthorDto = AuthorDto(id = 2, name = "Updated Name")

        // Act and Assert
        val exception = assertThrows<RequestValidationException> {
            authorService.updateAuthor(authorId, updatedAuthorDto)
        }

        // Assert
        assert(exception.error == ErrorCode.ID_MISSMATCH)
    }

    @Test
    fun `delete author`() {
        // Arrange
        val authorId = 1L
        val author = Author(id = authorId, name = "John Doe")

        // Mock behavior for the authorRepository
        every { authorRepository.findById(authorId) } returns Optional.of(author)
        justRun { authorRepository.deleteById(authorId) }

        // Act
        authorService.deleteAuthor(authorId)

        // Assert
        verify(exactly = 1) { authorRepository.deleteById(authorId) }
    }

    @Test
    fun `search authors`() {
        // Arrange
        val author1 = Author(id = 1L, name = "John Doe")
        val author2 = Author(id = 2L, name = "Jane Smith")
        val authors = listOf(author1, author2)

        // Create a Page object for testing
        val page: Page<Author> = PageImpl(authors)

        // Mock the behavior of the authorRepository
        every { authorRepository.findAll(any(Pageable::class)) } returns page

        // Call the service method with a Pageable parameter
        val pageable = Pageable.unpaged()
        val result: Page<AuthorDto> = authorService.searchAuthors(pageable)

        // Assert that the result contains the expected data
        assertEquals(2, result.totalElements)
        assertEquals("John Doe", result.content[0].name)
        assertEquals("Jane Smith", result.content[1].name)
    }
}