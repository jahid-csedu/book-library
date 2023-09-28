package com.example.library.domain.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Year

@Entity
@Table(name = "books")
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_id_seq_generator")
    @SequenceGenerator(name = "book_id_seq_generator", sequenceName = "book_id_seq", allocationSize = 1)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "isbn")
    var isbn: String,

    @Column(name = "title")
    var title: String,

    @Column(name = "author_id")
    var authorId: Long,

    @Column(name = "category_id")
    var categoryId: Long,

    @Column(name = "publisher_id")
    var publisherId: Long,

    @Column(name = "publication_year")
    var publicationYear: Year,

    @Column(name = "price")
    var price: BigDecimal
): BaseEntity()