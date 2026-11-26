package org.example.cocktails.recipes.service;

import org.example.cocktails.recipes.entry.Cocktail;
import org.example.cocktails.recipes.repository.CocktailRepository;
import org.example.cocktails.recipes.repository.IngredientRepository;
import org.example.cocktails.recipes.entry.ApiModels.*;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CocktailService {
    private final CocktailRepository repository;
    private final IngredientRepository ingredients;

    public CocktailService(CocktailRepository repository, IngredientRepository ingredients) {
        this.repository = repository;
        this.ingredients = ingredients;
    }

    public CompletableFuture<Cocktail> findByIdAsync(long id) {
        // TODO: Aufgabe - Milk: Mit @Async einen Cocktail laden und als CompletableFuture zurückgeben.
        throw new UnsupportedOperationException("TODO: Cocktail asynchron laden");
    }

    public List<CocktailSummary> cocktails() {
        // TODO: Aufgabe - Cocktails: Die Cocktail-Liste laden und für die Anzeige bereitstellen.
        throw new UnsupportedOperationException("Aufgabe - Cocktails");
    }

    public CocktailDetail cocktail(Long id) {
        // TODO: Aufgabe - Cocktails: Das Rezept zur Cocktail-ID laden und für die Anzeige bereitstellen.
        throw new UnsupportedOperationException("Aufgabe - Cocktails");
    }

    public List<IngredientView> ingredients() {
        // TODO: Aufgabe - Zutaten: Die Zutatenliste laden und für die Anzeige bereitstellen.
        throw new UnsupportedOperationException("Aufgabe - Zutaten");
    }

    public IngredientDetail ingredient(Long id) {
        // TODO: Aufgabe - Zutaten: Eine Zutat mit den zugehörigen Cocktails laden und für die Anzeige bereitstellen.
        throw new UnsupportedOperationException("Aufgabe - Zutaten");
    }

    public List<CocktailSummary> search(String query) {
        // TODO: Aufgabe - Cocktails: Nach Cocktail- und Zutatennamen suchen und doppelte Treffer vermeiden.
        throw new UnsupportedOperationException("Aufgabe - Cocktails");
    }
}
