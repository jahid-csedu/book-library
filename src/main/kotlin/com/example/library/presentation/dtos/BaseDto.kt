package com.example.library.presentation.dtos

import java.io.Serializable
import java.time.LocalDateTime

abstract class BaseDto: Serializable {
    var createdDate: LocalDateTime? = null

    var lastModifiedDate: LocalDateTime? = null
}