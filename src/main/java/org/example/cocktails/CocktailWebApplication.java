package org.example.cocktails;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CocktailWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(CocktailWebApplication.class, args);
    }

}