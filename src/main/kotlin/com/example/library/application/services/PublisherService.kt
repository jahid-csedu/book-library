package com.example.library.application.services

import com.example.library.application.mappers.PublisherMapper
import com.example.library.common.errors.ErrorCode
import com.example.library.domain.entities.Publisher
import com.example.library.domain.repositories.PublisherRepository
import com.example.library.presentation.dtos.PublisherDto
import com.example.library.presentation.exceptions.ResourceNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PublisherService(private val publisherRepository: PublisherRepository) {

    fun createPublisher(publisherDto: PublisherDto): PublisherDto {
        val publisher = PublisherMapper.INSTANCE.toEntity(publisherDto)
        return PublisherMapper.INSTANCE.toDto(publisherRepository.save(publisher))
    }

    fun updatePublisher(id: Long, publisherDto: PublisherDto): PublisherDto {
        validateId(id, publisherDto)
        val publisher = findPublisherByIdOrElseThrow(id)
        PublisherMapper.INSTANCE.toUpdateEntity(publisherDto, publisher)
        return PublisherMapper.INSTANCE.toDto(publisherRepository.save(publisher))
    }

    fun deletePublisher(id: Long) {
        findPublisherByIdOrElseThrow(id)
        publisherRepository.deleteById(id)
    }

    fun getPublisherById(id: Long): PublisherDto {
        return PublisherMapper.INSTANCE.toDto(findPublisherByIdOrElseThrow(id))
    }

    fun searchPublisher(pageable: Pageable): Page<PublisherDto> {
        val publishers = publisherRepository.findAll(pageable)
        return publishers.map { PublisherMapper.INSTANCE.toDto(it) }
    }

    private fun validateId(id: Long, publisherDto: PublisherDto) {
        if (id != publisherDto.id) {
            throw ResourceNotFoundException(ErrorCode.ID_MISSMATCH)
        }
    }

    private fun findPublisherByIdOrElseThrow(id: Long): Publisher =
        publisherRepository.findById(id)
            .orElseThrow { ResourceNotFoundException(ErrorCode.PUBLISHER_NOT_FOUND) }
}