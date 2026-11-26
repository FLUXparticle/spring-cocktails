package org.example.cocktails.recipes.service;

import org.example.cocktails.recipes.entry.Cocktail;
import org.example.cocktails.recipes.repository.CocktailRepository;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

@Service
public class CocktailService {
    private final CocktailRepository repository;

    public CocktailService(CocktailRepository repository) {
        this.repository = repository;
    }

    @Async
    public CompletableFuture<Cocktail> findByIdAsync(long id) {
        return CompletableFuture.completedFuture(repository.findById(id).orElseThrow());
    }
}
