package org.example.cocktails.recipes.service;

import org.example.cocktails.recipes.entry.ApiModels.*;
import org.example.cocktails.recipes.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import org.example.cocktails.recipes.entry.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FridgeService {
    private final CocktailRepository cocktails;
    private final IngredientRepository ingredients;
    private final Map<String, FridgeStore> fridges = new ConcurrentHashMap<>();

    public FridgeService(CocktailRepository cocktails, IngredientRepository ingredients) {
        this.cocktails = cocktails;
        this.ingredients = ingredients;
    }

    public List<FridgeIngredient> getFridgeIngredients() {
        Set<Long> stock = getCurrentFridge().ingredients();
        return ingredients.findAll().stream().sorted(Comparator.comparing(Ingredient::getName))
                .map(i -> new FridgeIngredient(i.getId(), i.getName(), stock.contains(i.getId()))).toList();
    }

    public void updateIngredientStatus(Long id, boolean present) {
        ingredients.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Zutat nicht gefunden."));
        getCurrentFridge().setIngredient(id, present);
    }

    public List<CocktailSummary> getPossibleCocktails() {
        Set<Long> stock = getCurrentFridge().ingredients();
        Map<Long, Cocktail> candidates = new LinkedHashMap<>();
        for (Long id : stock)
            cocktails.findDistinctByInstructionsIngredientId(id).forEach(c -> candidates.put(c.getId(), c));
        List<CocktailSummary> result = new ArrayList<>();
        for (var candidate : candidates.values()) {
            var recipe = cocktails.findById(candidate.getId()).orElseThrow();
            if (!recipe.getInstructions().isEmpty() && recipe.getInstructions().stream().allMatch(i -> stock.contains(i.getIngredient().getId())))
                result.add(new CocktailSummary(candidate.getId(), candidate.getName()));
        }
        return result.stream().sorted(Comparator.comparing(CocktailSummary::name)).toList();
    }

    private FridgeStore getCurrentFridge() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return fridges.computeIfAbsent(username, key -> new FridgeStore());
    }
}
