package com.example.library.application.services

import com.example.library.common.errors.ErrorCode
import com.example.library.domain.entities.Publisher
import com.example.library.domain.repositories.PublisherRepository
import com.example.library.presentation.dtos.PublisherDto
import com.example.library.presentation.exceptions.ResourceNotFoundException
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.util.Optional

class PublisherServiceTest {
    private val publisherRepository: PublisherRepository = mockk()
    private val publisherService: PublisherService = PublisherService(publisherRepository)

    @Test
    fun `should create a new publisher`() {
        val publisherDto = PublisherDto(id = 1, name = "Galaxy Book House")
        val publisher = Publisher(id = 1, name = "Galaxy Book House")

        every { publisherRepository.save(any()) } returns publisher

        val savedPublisher = publisherService.createPublisher(publisherDto)

        verify(exactly = 1) { publisherRepository.save(publisher) }
        assert(savedPublisher.id == publisherDto.id)
        assert(savedPublisher.name == publisherDto.name)
    }

    @Test
    fun `should update a publisher`() {
        val publisherId = 1L
        val publisherDto = PublisherDto(id = publisherId, name = "Galaxy Book House")
        val publisher = Publisher(id = publisherId, name = "Galaxy Book House")

        every { publisherRepository.findById(any()) } returns Optional.of(publisher)
        every { publisherRepository.save(any()) } returns publisher

        val savedPublisher = publisherService.updatePublisher(publisherId, publisherDto)

        verify(exactly = 1) { publisherRepository.save(publisher) }
        assert(savedPublisher.id == publisherDto.id)
        assert(savedPublisher.name == publisherDto.name)
    }

    @Test
    fun `should delete a publisher`() {
        val publisherId = 1L
        val publisher = Publisher(id = publisherId, name = "Galaxy Book House")

        every { publisherRepository.findById(any()) } returns Optional.of(publisher)
        justRun { publisherRepository.deleteById(any()) }

        publisherService.deletePublisher(publisherId)

        verify(exactly = 1) { publisherRepository.deleteById(publisherId) }
    }

    @Test
    fun `should get a publisher by id`() {
        val publisherId = 1L
        val publisher = Publisher(id = publisherId, name = "Galaxy Book House")

        every { publisherRepository.findById(any()) } returns Optional.of(publisher)

        val foundPublisher = publisherService.getPublisherById(publisherId)

        verify(exactly = 1) { publisherRepository.findById(publisherId) }
        assert(foundPublisher.id == publisherId)
        assert(foundPublisher.name == publisher.name)
    }

    @Test
    fun `should throw an error if a publisher not found by id`() {
        val publisherId = 1L

        every { publisherRepository.findById(any()) } returns Optional.empty()

        val exception = assertThrows<ResourceNotFoundException> {
            publisherService.getPublisherById(publisherId)
        }

        verify(exactly = 1) { publisherRepository.findById(publisherId) }
        assert(exception.error == ErrorCode.PUBLISHER_NOT_FOUND)
    }

    @Test
    fun `should get a paginated list of publisher`() {
        val publishers = listOf(
            Publisher(id = 1, name = "Galaxy Publications"),
            Publisher(id = 2, name = "Neptune Publications")
        )

        val page = PageImpl(publishers)
        every { publisherRepository.findAll(any(Pageable::class)) } returns page

        val pageRequest = PageRequest.of(0, 5)
        val searchResult = publisherService.searchPublisher(pageRequest)

        verify(exactly = 1) { publisherRepository.findAll(pageRequest) }
        assert(2L == searchResult.totalElements)
    }
}