package com.example.library.application.mappers

import com.example.library.domain.entities.Publisher
import com.example.library.presentation.dtos.PublisherDto
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface PublisherMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(PublisherMapper::class.java)
    }

    fun toDto(publisher: Publisher): PublisherDto

    fun toEntity(publisherDto: PublisherDto): Publisher
}