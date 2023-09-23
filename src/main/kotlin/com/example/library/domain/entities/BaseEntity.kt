package com.example.library.domain.entities

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.io.Serializable
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity: Serializable {

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    var createdDate: LocalDateTime? = null

    @LastModifiedDate
    @Column(name = "last_modified_date")
    var lastModifiedDate: LocalDateTime? = null

    @PrePersist
    fun prePersist() {
        createdDate = LocalDateTime.now()
    }

    @PreUpdate
    fun preUpdate() {
        lastModifiedDate = LocalDateTime.now()
    }
}
