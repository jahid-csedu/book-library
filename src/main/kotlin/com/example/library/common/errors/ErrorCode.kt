package com.example.library.common.errors

enum class ErrorCode(val code: String, val message: String) {
    BOOK_NOT_FOUND("RS101", "Book not found"),
    AUTHOR_NOT_FOUND("RS102", "Author not found"),
    CARTEGORY_NOT_FOUND("RS103", "Category not found"),
    PUBLISHER_NOT_FOUND("RS104", "Publisher not found"),
    ID_MISSMATCH("RS105", "Requested ID doesn't match"),
}