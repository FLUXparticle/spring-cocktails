package org.example.cocktails.bars.service;

import org.example.cocktails.bars.entry.*;
import org.example.cocktails.bars.repository.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.*;

@Configuration
public class BarInitializer {
    @Bean
    @ConditionalOnProperty(name = "bars.seed", havingValue = "true", matchIfMissing = true)
    ApplicationRunner seedBars(BarRepository repository, BarService service) {
        return args -> {
            if (repository.count() == 0) {
                for (int i = 1; i <= 26; i++)
                    service.create(new Bar("Bar " + (char) ('A' + i - 1), String.format(java.util.Locale.ROOT, "bar%02d@example.com", i), "Musterstraße " + i));
            }
        };
    }
}
