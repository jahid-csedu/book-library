package com.example.library.application.mappers

import com.example.library.domain.entities.Category
import com.example.library.presentation.dtos.CategoryDto
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface CategoryMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(CategoryMapper::class.java)
    }

    fun toDto(category: Category): CategoryDto

    fun toEntity(categoryDto: CategoryDto): Category
}