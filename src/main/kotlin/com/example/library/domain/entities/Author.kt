package com.example.library.domain.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "authors")
data class Author(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_id_seq_generator")
    @SequenceGenerator(name = "author_id_seq_generator", sequenceName = "author_id_seq", allocationSize = 1)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String
): BaseEntity()