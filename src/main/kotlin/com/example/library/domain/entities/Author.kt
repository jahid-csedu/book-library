package com.example.library.domain.entities

import jakarta.persistence.*

@Entity
data class Author(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val name: String,

    @ManyToMany(mappedBy = "authors")
    val books: MutableSet<Book> = mutableSetOf()
): BaseEntity()