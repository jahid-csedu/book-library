package com.example.library.common.errors

enum class ErrorCode(val code: String, val message: String) {
    BOOK_NOT_FOUND("RS101", "Book not found"),
    AUTHOR_NOT_FOUND("RS102", "Book not found"),
}