package com.example.library.domain.repositories

import com.example.library.domain.entities.Book
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.Year

@Repository
interface BookRepository : JpaRepository<Book, Long> {
    @Query(
        """
    SELECT b
    FROM Book b
    WHERE (:isbn IS NULL OR b.isbn = :isbn) AND
          (:title IS NULL OR lower(b.title) LIKE lower(concat('%', :title, '%'))) AND
          (:authorId IS NULL OR b.authorId = :authorId) AND
          (:publisherId IS NULL OR b.publisherId = :publisherId) AND
          (:categoryId IS NULL OR b.categoryId = :categoryId) AND
          (:publicationYear IS NULL OR b.publicationYear = :publicationYear) AND
          (:priceFrom IS NULL OR b.price >= :priceFrom) AND
          (:priceTo IS NULL OR b.price <= :priceTo)
    """
    )
    fun searchBook(
        @Param("isbn") isbn: String?,
        @Param("title") title: String?,
        @Param("authorId") authorId: Long?,
        @Param("categoryId") categoryId: Long?,
        @Param("publisherId") publisherId: Long?,
        @Param("publicationYear") publicationYear: Year?,
        @Param("priceFrom") priceFrom: BigDecimal?,
        @Param("priceTo") priceTo: BigDecimal?,
        pageable: Pageable
    ): Page<Book>
}