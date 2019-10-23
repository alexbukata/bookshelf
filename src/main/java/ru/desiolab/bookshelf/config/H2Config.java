package ru.desiolab.bookshelf.config;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.sql.SQLException;

@Configuration
@Profile("dev")
public class H2Config {

    @Bean
    public Server webServer() throws SQLException {
        return Server.createWebServer("-webPort", "8081").start();
    }
}
