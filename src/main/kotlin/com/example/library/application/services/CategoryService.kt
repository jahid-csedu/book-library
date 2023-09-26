package com.example.library.application.services

import com.example.library.application.mappers.CategoryMapper
import com.example.library.common.errors.ErrorCode
import com.example.library.domain.entities.Category
import com.example.library.domain.repositories.CategoryRepository
import com.example.library.presentation.dtos.CategoryDto
import com.example.library.presentation.exceptions.RequestValidationException
import com.example.library.presentation.exceptions.ResourceNotFoundException
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@CacheConfig(cacheNames = ["categories"])
class CategoryService(private val categoryRepository: CategoryRepository) {

    @Transactional(readOnly = true)
    @Cacheable(key = "#categoryId")
    fun getCategoryById(categoryId: Long): CategoryDto {
        val category = findCategoryByIdOrElseThrow(categoryId)
        return CategoryMapper.INSTANCE.toDto(category)
    }

    @Transactional(readOnly = true)
    fun searchCategories(pageable: Pageable): Page<CategoryDto> {
        val categories = categoryRepository.findAll(pageable)
        return categories.map { CategoryMapper.INSTANCE.toDto(it) }
    }

    @Transactional
    fun createCategory(categoryDto: CategoryDto): CategoryDto {
        val category = CategoryMapper.INSTANCE.toEntity(categoryDto)
        val savedCategory = categoryRepository.save(category)
        return CategoryMapper.INSTANCE.toDto(savedCategory)
    }

    @Transactional
    @CachePut(key = "#categoryId")
    fun updateCategory(categoryId: Long, categoryDto: CategoryDto): CategoryDto {
        validateUpdateRequest(categoryId, categoryDto)
        val category = findCategoryByIdOrElseThrow(categoryId)
        CategoryMapper.INSTANCE.toUpdateEntity(categoryDto, category)
        val savedCategory = categoryRepository.save(category)
        return CategoryMapper.INSTANCE.toDto(savedCategory)
    }

    @Transactional
    @CacheEvict(key = "#categoryId")
    fun deleteCategory(categoryId: Long) {
        findCategoryByIdOrElseThrow(categoryId)
        categoryRepository.deleteById(categoryId)
    }

    private fun validateUpdateRequest(categoryId: Long, updatedCategoryDto: CategoryDto) {
        if (categoryId != updatedCategoryDto.id) {
            throw RequestValidationException(ErrorCode.ID_MISSMATCH)
        }
    }

    private fun findCategoryByIdOrElseThrow(categoryId: Long): Category =
        categoryRepository.findById(categoryId)
            .orElseThrow { ResourceNotFoundException(ErrorCode.CARTEGORY_NOT_FOUND) }
}