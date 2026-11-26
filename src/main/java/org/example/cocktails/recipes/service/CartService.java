package org.example.cocktails.recipes.service;

import org.example.cocktails.recipes.entry.ApiModels.*;
import org.example.cocktails.recipes.repository.*;
import org.springframework.stereotype.*;
import org.springframework.web.context.annotation.*;

import java.util.*;

@Service
@SessionScope
public class CartService {
    private final CocktailRepository cocktails;
    private final IngredientRepository ingredients;
    private final Set<Long> ingredientIds = new LinkedHashSet<>();

    public CartService(CocktailRepository cocktails, IngredientRepository ingredients) {
        this.cocktails = cocktails;
        this.ingredients = ingredients;
    }

    public synchronized void addCocktail(Long id) {
        // TODO: Aufgabe - Warenkorb: Das Rezept laden und seine Zutaten-IDs im Session-Warenkorb sammeln.
        throw new UnsupportedOperationException("Aufgabe - Warenkorb");
    }

    public synchronized void removeIngredient(Long id) {
        // TODO: Aufgabe - Warenkorb: Die Zutaten-ID prüfen und aus dem Session-Warenkorb entfernen.
        throw new UnsupportedOperationException("Aufgabe - Warenkorb");
    }

    public synchronized void clear() {
        ingredientIds.clear();
    }

    public synchronized CartView getCart() {
        // TODO: Aufgabe - Warenkorb: Den aktuellen Warenkorbinhalt und die Cocktail-Auswahl zusammenstellen.
        throw new UnsupportedOperationException("Aufgabe - Warenkorb");
    }
}
