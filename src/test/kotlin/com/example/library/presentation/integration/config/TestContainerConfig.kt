package com.example.library.presentation.integration.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import javax.sql.DataSource

@Testcontainers
class TestContainerConfig {

    companion object {
        private val postgreSQLContainer: PostgreSQLContainer<*> = PostgreSQLContainer(DockerImageName.parse("postgres:13.3"))
            .apply {
                this.withDatabaseName("testDb").withUsername("root").withPassword("123456")
            }

        init {
            postgreSQLContainer.start()
        }
    }

    @Bean
    @Primary
    fun dataSource(): DataSource {
        val dataSourceConfig = HikariConfig()
        dataSourceConfig.jdbcUrl = postgreSQLContainer.jdbcUrl
        dataSourceConfig.username = postgreSQLContainer.username
        dataSourceConfig.password = postgreSQLContainer.password
        dataSourceConfig.driverClassName = postgreSQLContainer.driverClassName

        return HikariDataSource(dataSourceConfig)
    }

    @Test
    fun `is test container running`() {
        Assertions.assertTrue(postgreSQLContainer.isRunning())
    }
}