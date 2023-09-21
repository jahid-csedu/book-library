package com.example.library.domain.repositories

import com.example.library.domain.entities.Publisher
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PublisherRepository: JpaRepository<Publisher, Long>