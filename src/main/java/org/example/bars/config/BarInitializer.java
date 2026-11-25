package org.example.bars.config;

import org.example.bars.entry.*;
import org.example.bars.repository.*;
import org.example.bars.service.BarService;
import org.springframework.boot.*;
import org.springframework.context.annotation.*;

@Configuration
public class BarInitializer {
    @Bean
    ApplicationRunner seedBars(BarRepository repository, BarService service) {
        return args -> {
            if (repository.count() == 0) {
                for (int i = 1; i <= 26; i++)
                    service.create(new Bar("Bar " + (char) ('A' + i - 1), String.format(java.util.Locale.ROOT, "bar%02d@example.com", i), "Musterstraße " + i));
            }
        };
    }
}
