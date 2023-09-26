package com.example.library.application.services

import com.example.library.common.errors.ErrorCode
import com.example.library.domain.entities.Category
import com.example.library.domain.repositories.CategoryRepository
import com.example.library.presentation.dtos.CategoryDto
import com.example.library.presentation.exceptions.ResourceNotFoundException
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.util.Optional

class CategoryServiceTest {

    private val categoryRepository: CategoryRepository = mockk()
    private val caregoryService: CategoryService = CategoryService(categoryRepository)

    @Test
    fun `should create a new category`() {
        val categoryId: Long = 1
        val categoryName = "IT"
        val categoryDto = CategoryDto(categoryId, categoryName)
        val category = Category(categoryId, categoryName)

        every { categoryRepository.save(any()) } returns category

        val createdCategory = caregoryService.createCategory(categoryDto)

        verify(exactly = 1) { categoryRepository.save(any()) }
        assert(createdCategory.id == categoryDto.id)
        assert(createdCategory.name == categoryDto.name)
    }

    @Test
    fun `should update a category`() {
        val categoryId: Long = 1
        val categoryName = "IT"
        val categoryDto = CategoryDto(categoryId, categoryName)
        val category = Category(categoryId, categoryName)

        every { categoryRepository.save(any()) } returns category
        every { categoryRepository.findById(any()) } returns Optional.of(category)

        val createdCategory = caregoryService.updateCategory(categoryId, categoryDto)

        verify(exactly = 1) { categoryRepository.save(any()) }
        assert(createdCategory.id == categoryDto.id)
        assert(createdCategory.name == categoryDto.name)
    }

    @Test
    fun `should delete a category`() {
        val categoryId: Long = 1
        val category = Category(categoryId, "IT")

        justRun { categoryRepository.deleteById(any()) }
        every { categoryRepository.findById(any()) } returns Optional.of(category)

        caregoryService.deleteCategory(categoryId)

        verify(exactly = 1) { categoryRepository.deleteById(categoryId) }
    }

    @Test
    fun `should get a category by id`() {
        val categoryId: Long = 1
        val categoryName = "IT"
        val category = Category(categoryId, categoryName)
        val categoryDto = CategoryDto(categoryId, categoryName)

        every { categoryRepository.findById(any()) } returns Optional.of(category)

        val foundCategory = caregoryService.getCategoryById(categoryId)

        verify(exactly = 1) { categoryRepository.findById(categoryId) }
        assert(foundCategory.id == categoryDto.id)
        assert(foundCategory.name == categoryDto.name)
    }

    @Test
    fun `should through exception when category not found by id`() {
        val categoryId: Long = 1

        every { categoryRepository.findById(any()) } returns Optional.empty()

        val exception = assertThrows<ResourceNotFoundException> {
            caregoryService.getCategoryById(categoryId)
        }

        verify(exactly = 1) { categoryRepository.findById(categoryId) }
        assert(exception.error == ErrorCode.CARTEGORY_NOT_FOUND)
    }

    @Test
    fun `search categories`() {
        val category1 = Category(1, "Science")
        val category2 = Category(2, "Novel")
        val categories = listOf(category1, category2)

        val page: Page<Category> = PageImpl(categories)

        every { categoryRepository.findAll(any(Pageable::class)) } returns page

        val pageable = PageRequest.of(0, 10)
        val response = caregoryService.searchCategories(pageable)

        verify(exactly = 1) { categoryRepository.findAll(pageable) }
        assertEquals(2, response.totalElements)
    }
}