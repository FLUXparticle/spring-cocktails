package org.example.cocktails.config;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.core.env.*;
import org.springframework.jdbc.datasource.*;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.*;
import org.springframework.transaction.*;

import javax.sql.*;
import java.util.*;

/**
 * JPA configuration for the first day's H2 in-memory database.
 */
@Configuration
public class PersistenceConfiguration {
    private static LocalContainerEntityManagerFactoryBean factory(DataSource source, String unit, String entities, Map<String, ?> properties) {
        var factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(source);
        factory.setPersistenceUnitName(unit);
        factory.setPackagesToScan(entities.split(","));
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setJpaPropertyMap(properties);
        return factory;
    }

    @Bean
    DataSource barsDataSource(Environment env) {
        return new DriverManagerDataSource(env.getRequiredProperty("bars.datasource.url"), "sa", "");
    }

    @Bean
    LocalContainerEntityManagerFactoryBean barsEntityManagerFactory(@Qualifier("barsDataSource") DataSource source) {
        return factory(source, "bars", "org.example.cocktails.bars.entry", Map.of("hibernate.hbm2ddl.auto", "update"));
    }

    @Bean
    PlatformTransactionManager barsTransactionManager(@Qualifier("barsEntityManagerFactory") EntityManagerFactory factory) {
        return new JpaTransactionManager(factory);
    }

}
