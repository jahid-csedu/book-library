package com.example.library.presentation.exceptions

import com.example.library.common.errors.ErrorCode
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException(val error: ErrorCode) : RuntimeException(error.message)