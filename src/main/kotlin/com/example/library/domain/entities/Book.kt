package com.example.library.domain.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.math.BigDecimal
import java.time.Year

@Entity
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val isbn: String,

    val title: String,

    val authorId: Long,

    val categoryId: Long,

    val publisherId: Long,

    val publicationYear: Year,

    val price: BigDecimal
): BaseEntity()