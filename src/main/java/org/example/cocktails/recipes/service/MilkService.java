package org.example.cocktails.recipes.service;

import org.example.cocktails.recipes.repository.CocktailRepository;
import org.example.cocktails.recipes.entry.Cocktail;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Service;

@Service
public class MilkService {
    private final CocktailRepository repository;
    private final CocktailService cocktails;

    public MilkService(CocktailRepository repository, CocktailService cocktails) {
        this.repository = repository;
        this.cocktails = cocktails;
    }

    public int sequential() {
        int total = 0;
        for (Cocktail cocktail : milkCocktails()) {
            total += milkIn(repository.findById(cocktail.getId()).orElseThrow());
        }
        return total;
    }

    public int parallel() {
        var requests = milkCocktails().stream()
                .map(cocktail -> cocktails.findByIdAsync(cocktail.getId()))
                .toList();
        return requests.stream().map(CompletableFuture::join).mapToInt(this::milkIn).sum();
    }

    private List<Cocktail> milkCocktails() {
        return repository.findAll().stream()
                .filter(cocktail -> cocktail.getName().contains("Milk"))
                .toList();
    }

    private int milkIn(Cocktail cocktail) {
        return cocktail.getInstructions().stream()
                .filter(instruction -> instruction.getIngredient().getName().equals("Milch"))
                .mapToInt(instruction -> instruction.getAmountCL())
                .sum();
    }
}
