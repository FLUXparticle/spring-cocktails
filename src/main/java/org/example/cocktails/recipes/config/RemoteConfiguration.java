package org.example.cocktails.recipes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Profile("test")
public class RemoteConfiguration {
    @Bean
    public WebClient cocktailWebClient(WebClient.Builder builder) {
        return builder.baseUrl("https://cocktails.fluxparticle.com").build();
    }
}
