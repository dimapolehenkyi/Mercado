package com.example.mercado.configs;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.SQLException;

@TestConfiguration(
        proxyBeanMethods = false
)
public class TestContainerConfig {

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() throws SQLException {
        return new PostgreSQLContainer<>("postgres:15").withReuse(false);
    }

}
