package com.example.library.presentation.dtos

import java.math.BigDecimal
import java.time.Year

data class BookDto(
    val id: Long?,
    val isbn: String,
    val title: String,
    val authorId: Long,
    val categoryId: Long,
    val publisherId: Long,
    val publicationYear: Year,
    val price: BigDecimal
)