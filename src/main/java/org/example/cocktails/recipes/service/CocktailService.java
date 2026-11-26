package org.example.cocktails.recipes.service;

import org.example.cocktails.recipes.entry.Cocktail;
import org.example.cocktails.recipes.repository.CocktailRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class CocktailService {
    private final CocktailRepository repository;

    public CocktailService(CocktailRepository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Cocktail> findByIdAsync(long id) {
        // TODO: Aufgabe - Milk: Mit @Async einen Cocktail laden und als CompletableFuture zurückgeben.
        throw new UnsupportedOperationException("TODO: Cocktail asynchron laden");
    }
}
