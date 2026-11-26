package org.example.cocktails.recipes.service;

import org.example.cocktails.recipes.entry.Cocktail;
import org.example.cocktails.recipes.repository.CocktailRepository;
import org.example.cocktails.recipes.repository.IngredientRepository;
import org.example.cocktails.recipes.entry.ApiModels.*;

import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.concurrent.CompletableFuture;

@Service
public class CocktailService {
    private final CocktailRepository repository;
    private final IngredientRepository ingredients;

    public CocktailService(CocktailRepository repository, IngredientRepository ingredients) {
        this.repository = repository;
        this.ingredients = ingredients;
    }

    @Async
    public CompletableFuture<Cocktail> findByIdAsync(long id) {
        return CompletableFuture.completedFuture(repository.findById(id).orElseThrow());
    }

    public List<CocktailSummary> cocktails() {
        return repository.findAll().stream().map(c -> new CocktailSummary(c.getId(), c.getName())).sorted(Comparator.comparing(CocktailSummary::name)).toList();
    }

    public CocktailDetail cocktail(Long id) {
        var cocktail = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cocktail nicht gefunden."));
        return new CocktailDetail(cocktail.getId(), cocktail.getName(), cocktail.getInstructions().stream()
                .map(i -> new InstructionView(i.getAmountCL(), new IngredientView(i.getIngredient().getId(), i.getIngredient().getName()))).toList());
    }

    public List<IngredientView> ingredients() {
        return ingredients.findAll().stream().map(i -> new IngredientView(i.getId(), i.getName())).sorted(Comparator.comparing(IngredientView::name)).toList();
    }

    public IngredientDetail ingredient(Long id) {
        var ingredient = ingredients.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Zutat nicht gefunden."));
        var recipes = repository.findDistinctByInstructionsIngredientId(id).stream()
                .map(c -> new CocktailSummary(c.getId(), c.getName())).sorted(Comparator.comparing(CocktailSummary::name)).toList();
        return new IngredientDetail(ingredient.getId(), ingredient.getName(), recipes);
    }

    public List<CocktailSummary> search(String query) {
        String term = query.toLowerCase(Locale.ROOT);
        if (term.isEmpty()) return cocktails();
        Map<Long, CocktailSummary> matches = new LinkedHashMap<>();
        repository.findAll().stream().filter(c -> c.getName().toLowerCase(Locale.ROOT).contains(term))
                .forEach(c -> matches.put(c.getId(), new CocktailSummary(c.getId(), c.getName())));
        for (var ingredient : ingredients.findAll()) {
            if (ingredient.getName().toLowerCase(Locale.ROOT).contains(term))
                repository.findDistinctByInstructionsIngredientId(ingredient.getId()).forEach(c -> matches.put(c.getId(), new CocktailSummary(c.getId(), c.getName())));
        }
        return matches.values().stream().sorted(Comparator.comparing(CocktailSummary::name)).toList();
    }
}
