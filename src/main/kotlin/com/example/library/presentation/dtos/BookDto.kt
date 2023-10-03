package com.example.library.presentation.dtos

import com.example.library.common.errors.ValidationErrors
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.Year

data class BookDto(
    val id: Long?,
    val isbn: String?,
    @field:NotBlank(message = ValidationErrors.BOOK_TITLE_REQUIRED)
    val title: String?,
    @field:NotNull(message = ValidationErrors.AUTHOR_ID_REQUIRED)
    val authorId: Long?,
    @field:NotNull(message = ValidationErrors.CATEGORY_ID_REQUIRED)
    val categoryId: Long?,
    @field:NotNull(message = ValidationErrors.PUBLISHER_ID_REQUIRED)
    val publisherId: Long?,
    val publicationYear: Year?,
    @field:NotNull(message = ValidationErrors.PRICE_REQUIRED)
    val price: BigDecimal
): BaseDto()