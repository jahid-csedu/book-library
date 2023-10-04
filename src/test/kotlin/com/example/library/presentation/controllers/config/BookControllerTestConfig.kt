package com.example.library.presentation.controllers.config

import com.example.library.application.services.BookService
import io.mockk.mockk
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class BookControllerTestConfig {
    @Bean
    fun bookService() = mockk<BookService>()
}