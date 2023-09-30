package com.example.library.presentation.controllers

import com.example.library.application.services.AuthorService
import com.example.library.presentation.controllers.config.AuthorControllerTestConfig
import com.example.library.presentation.dtos.AuthorDto
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(AuthorController::class)
@Import(AuthorControllerTestConfig::class)
internal class AuthorControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var authorService: AuthorService

    @Test
    fun `should get all authors`() {
        val authors = listOf(AuthorDto(id = 1, name = "John Doe"), AuthorDto(2, "Michael"))
        every { authorService.searchAuthors(any()) } returns PageImpl(authors)

        val expectedResponseJson = javaClass.getResource("/unit/author_search_response.json").readText()

        mockMvc.perform(
            get("/authors")
                .param("page", "0")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().json(expectedResponseJson))
        verify(exactly = 1) { authorService.searchAuthors(PageRequest.of(0, 5)) }
    }

    @Test
    fun `should get correct author by id`() {
        val author = AuthorDto(id = 1, name = "John Doe")
        every { authorService.getAuthorById(any()) } returns author

        mockMvc.get("/authors/1")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                verify(exactly = 1) { authorService.getAuthorById(1) }
                jsonPath("$.id") { value(author.id) }
                jsonPath("$.name") { value(author.name) }
            }
            .andReturn()
    }

    @Test
    fun `should create a new author`() {
        val author = AuthorDto(id = 1, name = "John Doe")
        every { authorService.createAuthor(any()) } returns author

        mockMvc.post("/authors") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(author)
        }
            .andDo { print() }
            .andExpect {
                status { isCreated() }
                content { contentType(MediaType.APPLICATION_JSON) }
                verify(exactly = 1) { authorService.createAuthor(author) }
                jsonPath("$.id") { value(author.id) }
                jsonPath("$.name") { value(author.name) }
            }
            .andReturn()
    }

    @Test
    fun `should update an existing author`() {
        val authorId = 1L
        val author = AuthorDto(id = authorId, name = "John Doe")
        every { authorService.updateAuthor(any(), any()) } returns author

        mockMvc.put("/authors/1") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(author)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                verify(exactly = 1) { authorService.updateAuthor(1, author) }
                jsonPath("$.id") { value(authorId) }
                jsonPath("$.name") { value(author.name) }
            }
            .andReturn()
    }

    @Test
    fun `should delete an author by id`() {
        val authorId = 1L
        justRun { authorService.deleteAuthor(any()) }

        mockMvc.delete("/authors/1")
            .andDo { print() }
            .andExpect {
                status { isNoContent() }
                verify(exactly = 1) { authorService.deleteAuthor(authorId) }
            }
            .andReturn()
    }

}