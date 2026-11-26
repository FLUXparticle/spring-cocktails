package org.example.cocktails.recipes.repository;

import org.example.cocktails.recipes.entry.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.context.annotation.Profile;

import java.util.*;

@Profile("!test")
public interface CocktailRepository extends Repository<Cocktail, Long> {
    Collection<Cocktail> findAll();

    @EntityGraph(attributePaths = "instructions.ingredient")
    Optional<Cocktail> findById(Long id);

    Collection<Cocktail> findByNameContains(String query);

    Collection<Cocktail> findDistinctByInstructionsIngredientId(Long ingredientId);
}
