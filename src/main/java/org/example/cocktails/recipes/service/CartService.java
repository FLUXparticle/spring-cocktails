package org.example.cocktails.recipes.service;

import org.example.cocktails.recipes.entry.ApiModels.*;
import org.example.cocktails.recipes.repository.*;
import org.springframework.stereotype.*;
import org.springframework.web.context.annotation.*;

import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
        var cocktail = cocktails.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cocktail nicht gefunden."));
        cocktail.getInstructions().forEach(i -> ingredientIds.add(i.getIngredient().getId()));
    }

    public synchronized void removeIngredient(Long id) {
        ingredients.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Zutat nicht gefunden."));
        ingredientIds.remove(id);
    }

    public synchronized void clear() {
        ingredientIds.clear();
    }

    public synchronized CartView getCart() {
        var stock = ingredients.findAll().stream().filter(i -> ingredientIds.contains(i.getId()))
                .map(i -> new IngredientView(i.getId(), i.getName())).sorted(Comparator.comparing(IngredientView::name)).toList();
        var recipes = cocktails.findAll().stream().map(c -> new CocktailSummary(c.getId(), c.getName())).sorted(Comparator.comparing(CocktailSummary::name)).toList();
        return new CartView(stock, recipes);
    }
}
