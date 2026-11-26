package org.example.cocktails.recipes.entry;

import java.util.*;

public final class ApiModels {
    private ApiModels() {
    }

    public record IngredientView(Long id, String name) {
    }

    public record CocktailSummary(Long id, String name) {
    }

    public record InstructionView(Integer amountCL, IngredientView ingredient) {
    }

    public record CocktailDetail(Long id, String name, List<InstructionView> instructions) {
    }

    public record IngredientDetail(Long id, String name, List<CocktailSummary> cocktails) {
    }

    public record FridgeIngredient(Long id, String name, boolean inFridge) {
    }

    public record CartView(List<IngredientView> ingredients, List<CocktailSummary> cocktails) {
    }
}
