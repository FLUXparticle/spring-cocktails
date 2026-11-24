package org.example.cocktails;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;

@SpringBootApplication
@org.springframework.context.annotation.Import(org.example.cocktails.config.PersistenceConfiguration.class)
public class CocktailWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(CocktailWebApplication.class, args);
    }

}