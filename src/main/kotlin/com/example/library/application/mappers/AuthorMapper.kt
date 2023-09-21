package com.example.library.application.mappers

import com.example.library.domain.entities.Author
import com.example.library.presentation.dtos.AuthorDto
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers

@Mapper
interface AuthorMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(AuthorMapper::class.java)
    }

    fun toDto(author: Author): AuthorDto

    fun toDtoList(authors: List<Author>): List<AuthorDto>

    fun toEntity(authorDto: AuthorDto): Author

    fun toUpdateEntity(authorDto: AuthorDto, @MappingTarget author: Author)
}