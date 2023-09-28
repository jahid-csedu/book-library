package com.example.library.application.mappers

import com.example.library.domain.entities.Category
import com.example.library.presentation.dtos.CategoryDto
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers

@Mapper
interface CategoryMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(CategoryMapper::class.java)
    }

    fun toDto(category: Category): CategoryDto

    fun toDtoList(categories: List<Category>): List<CategoryDto>

    fun toEntity(categoryDto: CategoryDto): Category

    fun toUpdateEntity(categoryDto: CategoryDto, @MappingTarget category: Category)
}