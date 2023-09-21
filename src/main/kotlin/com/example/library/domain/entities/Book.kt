package com.example.library.domain.entities

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Year

@Entity
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val isbn: String,

    val title: String,

    @ManyToMany
    @JoinTable(
        name = "book_author",
        joinColumns = [JoinColumn(name = "book_id")],
        inverseJoinColumns = [JoinColumn(name = "author_id")]
    )
    val authors: MutableSet<Author> = mutableSetOf(),

    @ManyToOne
    val category: Category,

    @ManyToOne
    val publisher: Publisher,

    val publicationYear: Year,

    val price: BigDecimal
): BaseEntity()