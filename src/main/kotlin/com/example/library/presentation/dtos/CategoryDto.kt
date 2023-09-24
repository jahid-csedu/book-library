package com.example.library.presentation.dtos

import com.example.library.common.errors.ValidationErrors
import jakarta.validation.constraints.NotBlank

data class CategoryDto(
    val id: Long?,

    @field:NotBlank(message = ValidationErrors.CATEGORY_NAME_REQUIRED)
    val name: String
): BaseDto()