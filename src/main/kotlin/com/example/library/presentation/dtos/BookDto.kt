package com.example.library.presentation.dtos

import java.math.BigDecimal
import java.time.Year

data class BookDto(
    val id: Long?,
    val isbn: String,
    val title: String,
    val authors: Set<AuthorDto>,
    val category: CategoryDto,
    val publisher: PublisherDto,
    val publicationYear: Year,
    val price: BigDecimal
)