package com.example.library

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@SpringBootTest
@Testcontainers
class LibraryApplicationTests {

	companion object {
		@Container
		val postgreSQLContainer = PostgreSQLContainer(DockerImageName.parse("postgres:13.3"))
			.apply {
				this.withDatabaseName("testDb")
					.withUsername("user")
					.withPassword("password")
					.withInitScript("schema.sql")
			}

		@JvmStatic
		@DynamicPropertySource
		fun properties(registry: DynamicPropertyRegistry) {
			registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl)
			registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
			registry.add("spring.datasource.password", postgreSQLContainer::getPassword)
		}
	}

	@Test
	fun contextLoads() {
	}

}
