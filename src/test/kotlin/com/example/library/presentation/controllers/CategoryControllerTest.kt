package com.example.library.presentation.controllers

import com.example.library.application.services.CategoryService
import com.example.library.presentation.controllers.config.CategoryControllerTestConfig
import com.example.library.presentation.dtos.CategoryDto
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(CategoryController::class)
@Import(CategoryControllerTestConfig::class)
class CategoryControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var categoryService: CategoryService

    @Test
    fun `should get all categories`() {
        val categories = listOf(CategoryDto(id = 1, name = "IT"), CategoryDto(id = 2, name = "Novel"))

        every { categoryService.searchCategories(any()) } returns PageImpl(categories)

        val expectedResponseJson = javaClass.getResource("/unit/category_search_response.json").readText()

        mockMvc.perform(
            MockMvcRequestBuilders.get("/categories")
                .param("page", "0")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(expectedResponseJson))
        verify(exactly = 1) { categoryService.searchCategories(PageRequest.of(0, 5)) }
    }

    @Test
    fun `should get category by id`() {
        val categoryId = 1L
        val category = CategoryDto(id = categoryId, name = "IT")

        every { categoryService.getCategoryById(any()) } returns category

        mockMvc.get("/categories/${categoryId}")
            .andDo { print() }
            .andExpect {
                content { contentType(MediaType.APPLICATION_JSON) }
                verify(exactly = 1) { categoryService.getCategoryById(categoryId) }
                jsonPath("$.id") { value(category.id) }
                jsonPath("$.name") { value(category.name) }
            }
            .andReturn()
    }

    @Test
    fun `should create a new category`() {
        val categoryDto = CategoryDto(id = 1, name = "Comic")

        every { categoryService.createCategory(any()) } returns categoryDto

        mockMvc.post("/categories") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(categoryDto)
        }
            .andDo { print() }
            .andExpect {
                status { isCreated() }
                content { contentType(MediaType.APPLICATION_JSON) }
                verify(exactly = 1) { categoryService.createCategory(categoryDto) }
                jsonPath("$.id") { value(categoryDto.id) }
                jsonPath("$.name") { value(categoryDto.name) }
            }
            .andReturn()
    }

    @Test
    fun `should update a category`() {
        val categoryId = 1L
        val categoryDto = CategoryDto(id = categoryId, name = "Comic")

        every { categoryService.updateCategory(any(), any()) } returns categoryDto

        mockMvc.put("/categories/1") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(categoryDto)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                verify(exactly = 1) { categoryService.updateCategory(categoryId, categoryDto) }
                jsonPath("$.id") { value(categoryDto.id) }
                jsonPath("$.name") { value(categoryDto.name) }
            }
            .andReturn()
    }

    @Test
    fun `should delete a category`() {
        val categoryId = 1L

        justRun { categoryService.deleteCategory(any()) }

        mockMvc.delete("/categories/1")
            .andDo { print() }
            .andExpect {
                status { isNoContent() }
                verify(exactly = 1) { categoryService.deleteCategory(categoryId) }
            }
            .andReturn()
    }
}