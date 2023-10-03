package com.example.library.presentation.dtos

import java.math.BigDecimal
import java.time.Year

class BookSearchDto(
    val isbn: String?,
    val title: String?,
    val authorId: Long?,
    val categoryId: Long?,
    val publisherId: Long?,
    val publicationYear: Year?,
    val priceFrom: BigDecimal?,
    val priceTo: BigDecimal?
)