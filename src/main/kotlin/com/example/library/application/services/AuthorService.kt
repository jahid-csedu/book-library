package com.example.library.application.services


import com.example.library.application.mappers.AuthorMapper
import com.example.library.common.errors.ErrorCode
import com.example.library.domain.entities.Author
import com.example.library.domain.repositories.AuthorRepository
import com.example.library.presentation.dtos.AuthorDto
import com.example.library.presentation.exceptions.RequestValidationException
import com.example.library.presentation.exceptions.ResourceNotFoundException
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Service
@CacheConfig(cacheNames = ["authors"])
class AuthorService(private val authorRepository: AuthorRepository) {

    val logger: Logger = LoggerFactory.getLogger(AuthorService::class.java)

    @Transactional
    fun createAuthor(authorDto: AuthorDto): AuthorDto {
        logger.info("Author create request: {}", authorDto)
        val author = AuthorMapper.INSTANCE.toEntity(authorDto)
        val savedAuthor = authorRepository.save(author)
        return AuthorMapper.INSTANCE.toDto(savedAuthor)
    }

    @Cacheable(key = "#authorId")
    @Transactional(readOnly = true)
    fun getAuthorById(authorId: Long): AuthorDto {
        logger.info("Getting author by ID: {}", authorId)
        val author = findAuthorByIdOrElseThrow(authorId)
        return AuthorMapper.INSTANCE.toDto(author)
    }

    @CachePut(key = "#authorId")
    @Transactional
    fun updateAuthor(authorId: Long, updatedAuthorDto: AuthorDto): AuthorDto {
        logger.info("Author update request: {}", updatedAuthorDto)
        validateUpdateRequest(authorId, updatedAuthorDto)
        val author = findAuthorByIdOrElseThrow(authorId)

        AuthorMapper.INSTANCE.toUpdateEntity(updatedAuthorDto, author)

        val updatedAuthor = authorRepository.save(author)
        return AuthorMapper.INSTANCE.toDto(updatedAuthor)
    }

    @CacheEvict(key = "#authorId")
    @Transactional
    fun deleteAuthor(authorId: Long) {
        logger.info("Deleting author by id: {}", authorId)
        findAuthorByIdOrElseThrow(authorId)
        authorRepository.deleteById(authorId)
    }

    @Transactional(readOnly = true)
    fun searchAuthors(pageable: Pageable): Page<AuthorDto> {
        val authors = authorRepository.findAll(pageable)
        return authors.map { AuthorMapper.INSTANCE.toDto(it) }
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
