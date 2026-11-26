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
        return client.get().uri("/api/cocktails").retrieve()
                .bodyToFlux(Cocktail.class).collectList().block();
    }

    @Override
    public Optional<Cocktail> findById(Long id) {
        return Optional.ofNullable(client.get().uri("/api/cocktails/{id}", id)
                .retrieve().bodyToMono(Cocktail.class).block());
    }

    // Bonus: Die Namenssuche kann alternativ ins Repository verlagert werden.
    @Override
    public Collection<Cocktail> findByNameContains(String query) {
        return findAll().stream().filter(cocktail -> cocktail.getName().contains(query)).toList();
    }

    // Bonus: Cocktails direkt über eine bekannte Zutaten-ID suchen.
    @Override
    public Collection<Cocktail> findDistinctByInstructionsIngredientId(Long ingredientId) {
        return client.get().uri("/api/ingredients/{id}", ingredientId)
                .retrieve().bodyToMono(IngredientDetail.class).block().cocktails();
    }

    public record IngredientDetail(String name, Collection<Cocktail> cocktails) {
    }
}
