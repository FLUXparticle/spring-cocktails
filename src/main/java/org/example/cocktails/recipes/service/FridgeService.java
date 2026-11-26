package org.example.cocktails.recipes.service;

import org.example.cocktails.recipes.entry.ApiModels.*;
import org.example.cocktails.recipes.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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
        // TODO: Aufgabe - Kühlschrank: Die Zutatenliste mit dem Bestand des angemeldeten Benutzers zusammenführen.
        throw new UnsupportedOperationException("Aufgabe - Kühlschrank");
    }

    public void updateIngredientStatus(Long id, boolean present) {
        // TODO: Aufgabe - Kühlschrank: Die Zutat prüfen und den Bestand des angemeldeten Benutzers ändern.
        throw new UnsupportedOperationException("Aufgabe - Kühlschrank");
    }

    public List<CocktailSummary> getPossibleCocktails() {
        // TODO: Aufgabe - Kühlschrank: Kandidaten aus dem Bestand sammeln, jedes Rezept höchstens einmal laden und vollständige Rezepte auswählen.
        throw new UnsupportedOperationException("Aufgabe - Kühlschrank");
    }

    private FridgeStore getCurrentFridge() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return fridges.computeIfAbsent(username, key -> new FridgeStore());
    }
}
