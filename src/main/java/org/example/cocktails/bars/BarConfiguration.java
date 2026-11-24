package org.example.cocktails.bars;

import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.*;
import org.springframework.scheduling.annotation.*;

@Configuration
@EnableAsync
@EnableJpaRepositories(basePackages = "org.example.cocktails.bars.repository",
        entityManagerFactoryRef = "barsEntityManagerFactory", transactionManagerRef = "barsTransactionManager")
public class BarConfiguration {
}
