package com.example.library.application.mappers

import com.example.library.domain.entities.Book
import com.example.library.presentation.dtos.BookDto
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface BookMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(BookMapper::class.java)
    }

    fun toDto(book: Book): BookDto

    fun toEntity(bookDto: BookDto): Book
}
