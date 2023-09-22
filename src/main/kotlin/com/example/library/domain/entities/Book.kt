package com.example.library.domain.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Year

@Entity
@Table(name = "books")
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "isbn")
    val isbn: String,

    @Column(name = "title")
    val title: String,

    @Column(name = "author_id")
    val authorId: Long,

    @Column(name = "category_id")
    val categoryId: Long,

    @Column(name = "publisher_id")
    val publisherId: Long,

    @Column(name = "publication_year")
    val publicationYear: Year,

    @Column(name = "price")
    val price: BigDecimal
): BaseEntity()