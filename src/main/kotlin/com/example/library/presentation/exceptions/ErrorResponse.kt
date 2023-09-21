package com.example.library.presentation.exceptions

import java.time.LocalDateTime

class ErrorResponse(
        val errorCode: String,
        val message: String,
        val timestamp: LocalDateTime = LocalDateTime.now()
)
