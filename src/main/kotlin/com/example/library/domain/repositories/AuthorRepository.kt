package com.example.library.domain.repositories

import com.example.library.domain.entities.Author
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository: JpaRepository<Author, Long> {
    override fun findAll(pageable: Pageable): Page<Author>
}