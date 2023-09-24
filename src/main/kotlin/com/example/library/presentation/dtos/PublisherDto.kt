package com.example.library.presentation.dtos

import com.example.library.common.errors.ValidationErrors
import jakarta.validation.constraints.NotBlank

data class PublisherDto(
    val id: Long?,

    @field:NotBlank(message = ValidationErrors.PUBLISHER_NAME_REQUIRED)
    val name: String
): BaseDto()