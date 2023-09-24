package com.example.library.presentation.exceptions

import com.example.library.common.errors.ValidationErrors
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleResourceNotFoundException(exception: ResourceNotFoundException): ErrorResponse {
        return ErrorResponse(
                errorCode = exception.error.code,
                message = exception.error.message
        )
    }

    @ExceptionHandler(BackendServiceException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleBackendServiceException(exception: ResourceNotFoundException): ErrorResponse {
        return ErrorResponse(
            errorCode = exception.error.code,
            message = exception.error.message
        )
    }

    @ExceptionHandler(RequestValidationException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleRequestValidationException(exception: RequestValidationException): ErrorResponse {
        return ErrorResponse(
            errorCode = exception.error.code,
            message = exception.error.message
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ErrorResponse {
        val errorMessage = ex.bindingResult.fieldErrors.joinToString(", ") {
            "${it.field} ${it.defaultMessage}"
        }
        return ErrorResponse(ValidationErrors.VALIDATION_ERROR, errorMessage)
    }
}