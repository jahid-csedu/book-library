package com.example.library.presentation.dtos

import java.math.BigDecimal
import java.time.Year

class BookSearchDto(
    val isbn: String?,
    val title: String?,
    val authorId: Long?,
    val categoryId: Long?,
    val publisherId: Long?,
    val publicationYear: Year?,
    val priceFrom: BigDecimal?,
    val priceTo: BigDecimal?
) {
    class Builder {
        private var isbn: String? = null
        private var title: String? = null
        private var authorId: Long? = null
        private var categoryId: Long? = null
        private var publisherId: Long? = null
        private var publicationYear: Year? = null
        private var priceFrom: BigDecimal? = null
        private var priceTo: BigDecimal? = null

        fun isbn(isbn: String?) = apply { this.isbn = isbn }
        fun title(title: String?) = apply { this.title = title }
        fun authorId(authorId: Long?) = apply { this.authorId = authorId }
        fun categoryId(categoryId: Long?) = apply { this.categoryId = categoryId }
        fun publisherId(publisherId: Long?) = apply { this.publisherId = publisherId }
        fun publicationYear(publicationYear: Year?) = apply { this.publicationYear = publicationYear }
        fun priceFrom(priceFrom: BigDecimal?) = apply { this.priceFrom = priceFrom }
        fun priceTo(priceTo: BigDecimal?) = apply { this.priceTo = priceTo }

        fun build() = BookSearchDto(
            isbn, title, authorId, categoryId, publisherId, publicationYear, priceFrom, priceTo
        )
    }
}