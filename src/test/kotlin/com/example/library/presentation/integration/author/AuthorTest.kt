package com.example.library.presentation.integration.author

import com.example.library.presentation.integration.config.TestContainerConfig
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@SpringBootTest
@Import(TestContainerConfig::class)
class AuthorTest {

}