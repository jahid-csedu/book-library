package com.example.library.presentation.controllers.config

import com.example.library.application.services.CategoryService
import io.mockk.mockk
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class CategoryControllerTestConfig {
    @Bean
    fun categoryService() = mockk<CategoryService>()
}