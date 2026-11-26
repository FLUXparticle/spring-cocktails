package org.example.cocktails.recipes.repository;

import org.example.cocktails.recipes.entry.Cocktail;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;
import java.util.Optional;

@Repository
@Profile("test")
public class RemoteCocktailRepository implements CocktailRepository {
    private final WebClient client;

    public RemoteCocktailRepository(WebClient client) {
        this.client = client;
    }

    @Override
    public Collection<Cocktail> findAll() {
        // TODO: Aufgabe - Milk: Alle Cocktails über GET /api/cocktails laden.
        throw new UnsupportedOperationException("TODO: Alle Cocktails laden");
    }

    @Override
    public Optional<Cocktail> findById(Long id) {
        // TODO: Aufgabe - Milk: Einen Cocktail über GET /api/cocktails/{id} laden.
        throw new UnsupportedOperationException("TODO: Cocktail laden");
    }

    @Override
    public Collection<Cocktail> findByNameContains(String query) {
        // TODO (Bonus): Alternative Lösung: Die Namenssuche ins Repository verlagern.
        // Für die Pflichtaufgabe reichen findAll und findById; im Service nach "Milk" filtern.
        throw new UnsupportedOperationException("TODO: Cocktails suchen");
    }

    @Override
    public Collection<Cocktail> findDistinctByInstructionsIngredientId(Long ingredientId) {
        // TODO (Bonus): Alternative Lösung: Cocktails direkt über eine bekannte Zutaten-ID suchen.
        // GET /api/ingredients/{id} laden und dessen cocktails zurückgeben.
        // Für die Pflichtaufgabe nicht erforderlich.
        throw new UnsupportedOperationException("TODO: Cocktails zu einer Zutat laden");
    }

    public record IngredientDetail(String name, Collection<Cocktail> cocktails) {
    }
}
