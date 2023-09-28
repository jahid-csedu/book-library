package com.example.library.presentation.controllers

import com.example.library.application.services.CategoryService
import com.example.library.presentation.dtos.CategoryDto
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
@RequestMapping("categories")
class CategoryController(private val categoryService: CategoryService) {

    @GetMapping
    fun searchCategories(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<CategoryDto>> {
        val pageable = PageRequest.of(page, size)
        val categories = categoryService.searchCategories(pageable)
        return ResponseEntity(categories, HttpStatus.OK)
    }

    @GetMapping("/{categoryId}")
    fun getCategoryById(@PathVariable categoryId: Long): ResponseEntity<CategoryDto> {
        val category = categoryService.getCategoryById(categoryId)
        return ResponseEntity(category, HttpStatus.OK)
    }

    @PostMapping
    fun createCategory(@RequestBody @Valid categoryDto: CategoryDto): ResponseEntity<CategoryDto> {
        val category = categoryService.createCategory(categoryDto)
        return ResponseEntity(category, HttpStatus.CREATED)
    }

    @PutMapping("/{categoryId}")
    fun updateCategory(
        @PathVariable categoryId: Long,
        @RequestBody @Valid categoryDto: CategoryDto
    ): ResponseEntity<CategoryDto> {
        val category = categoryService.updateCategory(categoryId, categoryDto)
        return ResponseEntity(category, HttpStatus.OK)
    }

    @DeleteMapping("/{categoryId}")
    fun deleteCategory(@PathVariable categoryId: Long): ResponseEntity<Unit> {
        categoryService.deleteCategory(categoryId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}