package com.example.library.presentation.integration

import com.example.library.domain.repositories.AuthorRepository
import com.example.library.presentation.dtos.AuthorDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuthorIntegrationTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var authorRepository: AuthorRepository

    @AfterEach
    fun setup() {
        authorRepository.deleteAll()
    }

    @Test
    fun `should create an author`() {
        val authorDto = """
            {
                "name": "John Doe"
            }
        """

        mockMvc.post("/authors") {
            contentType = MediaType.APPLICATION_JSON
            content = authorDto
        }
            .andExpect {
                status { isCreated() }
            }
    }

    @Test
    @Sql("/author-data.sql")
    fun `should update an author`() {
        val authorId = 1L
        val authorDto = AuthorDto(authorId, "Michael")

        mockMvc.put("/authors/${authorId}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(authorDto)
        }
            .andExpect {
                status { isOk() }
                jsonPath("$.name") { value("Michael") }
            }
    }

    @Test
    @Sql("/author-data.sql")
    fun `should delete an author by id`() {
        val authorId = 1L

        mockMvc.delete("/authors/${authorId}")
            .andExpect {
                status { isNoContent() }
            }
    }

    @Test
    @Sql("/author-data.sql")
    fun `should get an author by id`() {
        val authorId = 1L

        mockMvc.get("/authors/${authorId}")
            .andExpect {
                status { isOk() }
                jsonPath("$.name") { value("John Doe") }
            }
    }

    @Test
    @Sql("/author-data.sql")
    fun `should get All authors`() {
        mockMvc.get("/authors")
            .andExpect {
                status { isOk() }
                jsonPath("$[0].name") { value("John Doe") }
                jsonPath("$[1].name") { value("Mickey") }
            }
    }
}
