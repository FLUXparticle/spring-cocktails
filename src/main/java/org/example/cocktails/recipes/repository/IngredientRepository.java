package org.example.cocktails.recipes.repository;

import org.example.cocktails.recipes.entry.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface IngredientRepository extends Repository<Ingredient, Long> {
    Collection<Ingredient> findAll();

    Optional<Ingredient> findById(Long id);

    Collection<Ingredient> findByNameContains(String query);
}
