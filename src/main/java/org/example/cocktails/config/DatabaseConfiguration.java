package org.example.cocktails.config;

import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@Profile("!test")
public class DatabaseConfiguration {
    @Bean
    public DataSource dataSource() {
        return new DriverManagerDataSource("jdbc:sqlite::resource:cocktails.db");
    }

    @Bean
    public HibernatePropertiesCustomizer sqliteProperties() {
        return properties -> {
            properties.put("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");
            properties.put("hibernate.hbm2ddl.auto", "none");
        };
    }
}
