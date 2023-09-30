package com.example.library.presentation.controllers

import com.example.library.application.services.PublisherService
import com.example.library.presentation.dtos.PublisherDto
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
@RequestMapping("/publishers")
class PublisherController(private val publisherService: PublisherService) {

    @PostMapping
    fun createPublisher(@RequestBody @Valid publisherDto: PublisherDto): ResponseEntity<PublisherDto> {
        val publisher = publisherService.createPublisher(publisherDto)
        return ResponseEntity(publisher, HttpStatus.CREATED)
    }

    @PutMapping("/{publisherId}")
    fun updatePublisher(
        @PathVariable publisherId: Long,
        @RequestBody @Valid publisherDto: PublisherDto
    ): ResponseEntity<PublisherDto> {
        val publisher = publisherService.updatePublisher(publisherId, publisherDto)
        return ResponseEntity(publisher, HttpStatus.OK)
    }

    @DeleteMapping("/{publisherId}")
    fun deletePublisher(@PathVariable publisherId: Long): ResponseEntity<Unit> {
        publisherService.deletePublisher(publisherId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @GetMapping("/{publisherId}")
    fun getPublisherById(@PathVariable publisherId: Long): ResponseEntity<PublisherDto> {
        val publisher = publisherService.getPublisherById(publisherId)
        return ResponseEntity(publisher, HttpStatus.OK)
    }

    @GetMapping
    fun searchPublishers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<PublisherDto>> {
        val pageable = PageRequest.of(page, size)
        val publishers = publisherService.searchPublisher(pageable)
        return ResponseEntity(publishers, HttpStatus.OK)
    }
}
