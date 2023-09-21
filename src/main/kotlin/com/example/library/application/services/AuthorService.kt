package com.example.library.application.services


import com.example.library.application.mappers.AuthorMapper
import com.example.library.common.errors.ErrorCode
import com.example.library.domain.entities.Author
import com.example.library.domain.repositories.AuthorRepository
import com.example.library.presentation.dtos.AuthorDto
import com.example.library.presentation.exceptions.RequestValidationException
import com.example.library.presentation.exceptions.ResourceNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthorService(private val authorRepository: AuthorRepository) {

    fun createAuthor(authorDto: AuthorDto): AuthorDto {
        val author = AuthorMapper.INSTANCE.toEntity(authorDto)
        return AuthorMapper.INSTANCE.toDto(authorRepository.save(author))
    }

    fun getAuthorById(authorId: Long): AuthorDto {
        val author = findAuthorByIdOrElseThrow(authorId)
        return AuthorMapper.INSTANCE.toDto(author);
    }

    fun updateAuthor(authorId: Long, updatedAuthorDto: AuthorDto): AuthorDto {
        validateUpdateRequest(authorId, updatedAuthorDto)
        val author = findAuthorByIdOrElseThrow(authorId)

        AuthorMapper.INSTANCE.toUpdateEntity(updatedAuthorDto, author)

        return AuthorMapper.INSTANCE.toDto(authorRepository.save(author))
    }

    fun deleteAuthor(authorId: Long) {
        authorRepository.deleteById(authorId)
    }

    fun getAllAuthors(): List<AuthorDto> {
        val authors = authorRepository.findAll()
        return AuthorMapper.INSTANCE.toDtoList(authors)
    }

    private fun validateUpdateRequest(authorId: Long, updatedAuthorDto: AuthorDto) {
        if (authorId != updatedAuthorDto.id) {
            throw RequestValidationException(ErrorCode.ID_MISSMATCH)
        }
    }

    private fun findAuthorByIdOrElseThrow(authorId: Long): Author =
        authorRepository.findById(authorId)
            .orElseThrow { ResourceNotFoundException(ErrorCode.AUTHOR_NOT_FOUND) }
}
