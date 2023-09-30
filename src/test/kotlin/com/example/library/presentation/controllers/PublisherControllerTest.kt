package com.example.library.presentation.controllers

import com.example.library.application.services.PublisherService
import com.example.library.presentation.controllers.config.PublisherControllerTestConfig
import com.example.library.presentation.dtos.PublisherDto
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(PublisherController::class)
@Import(PublisherControllerTestConfig::class)
class PublisherControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var publisherService: PublisherService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should create a new publisher`() {
        val publisherDto = PublisherDto(1, "Marvels")

        every { publisherService.createPublisher(any()) } returns publisherDto

        mockMvc.post("/publishers") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(publisherDto)
        }
            .andDo { print() }
            .andExpect {
                status { isCreated() }
                verify(exactly = 1) { publisherService.createPublisher(publisherDto) }
                jsonPath("$.id") { value(publisherDto.id) }
                jsonPath("$.name") { value(publisherDto.name) }
            }
            .andReturn()
    }

    @Test
    fun `should update a publisher`() {
        val publisherId = 1L
        val publisherDto = PublisherDto(publisherId, "Marvels")

        every { publisherService.updatePublisher(any(), any()) } returns publisherDto

        mockMvc.put("/publishers/${publisherId}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(publisherDto)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                verify(exactly = 1) { publisherService.updatePublisher(publisherId, publisherDto) }
                jsonPath("$.id") { value(publisherId) }
                jsonPath("$.name") { value(publisherDto.name) }
            }
            .andReturn()
    }

    @Test
    fun `should delete a publisher by id`() {
        val publisherId = 1L

        justRun { publisherService.deletePublisher(any()) }

        mockMvc.delete("/publishers/${publisherId}")
            .andDo { print() }
            .andExpect {
                status { isNoContent() }
                verify(exactly = 1) { publisherService.deletePublisher(publisherId) }
            }
            .andReturn()
    }

    @Test
    fun `should get a publisher by id`() {
        val publisherId = 1L
        val publisherDto = PublisherDto(publisherId, "Marvels")

        every { publisherService.getPublisherById(any()) } returns publisherDto

        mockMvc.get("/publishers/${publisherId}")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                verify(exactly = 1) { publisherService.getPublisherById(publisherId) }
                jsonPath("$.id") { value(publisherId) }
                jsonPath("$.name") { value(publisherDto.name) }
            }
            .andReturn()
    }

    @Test
    fun `should get paginated publisher list`() {
        val publishers = listOf(
            PublisherDto(1, "Jupiter"),
            PublisherDto(2, "Marvels")
        )

        every { publisherService.searchPublisher(any()) } returns PageImpl(publishers)

        val expectedResponseJson = javaClass.getResource("/unit/publisher_search_response.json").readText()


        mockMvc.perform(
            get("/publishers")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(expectedResponseJson))
        verify(exactly = 1) { publisherService.searchPublisher(PageRequest.of(0, 10)) }
    }
}