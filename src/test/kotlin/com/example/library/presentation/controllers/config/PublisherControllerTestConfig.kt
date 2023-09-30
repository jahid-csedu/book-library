package com.example.library.presentation.controllers.config

import com.example.library.application.services.PublisherService
import io.mockk.mockk
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class PublisherControllerTestConfig {

    @Bean
    fun publisherService() = mockk<PublisherService>()
}