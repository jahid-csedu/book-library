package com.example.library.common.errors

interface ValidationErrors {
    companion object {
        const val VALIDATION_ERROR = "INVALID REQUEST"
        const val AUTHOR_NAME_REQUIRED = "Author name is required"
        const val AUTHOR_ID_REQUIRED = "Author ID is required"
        const val PUBLISHER_NAME_REQUIRED = "Publisher name is required"
        const val PUBLISHER_ID_REQUIRED = "Publisher ID is required"
        const val CATEGORY_NAME_REQUIRED = "Category name is required"
        const val CATEGORY_ID_REQUIRED = "Category ID is required"
        const val BOOK_TITLE_REQUIRED = "Book title is required"
        const val PRICE_REQUIRED = "Book price is required"
    }
}