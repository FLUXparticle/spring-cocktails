package org.example.cocktails.recipes.service;

import org.example.cocktails.recipes.repository.CocktailRepository;
import org.springframework.stereotype.Service;

@Service
public class MilkService {
    private final CocktailRepository repository;
    private final CocktailService cocktails;

    public MilkService(CocktailRepository repository, CocktailService cocktails) {
        this.repository = repository;
        this.cocktails = cocktails;
    }

    public int sequential() {
        // TODO: Aufgabe - Milk: Die Details aller Cocktails mit "Milk" im Namen nacheinander laden.
        // Die Mengen der Zutat "Milch" in cl addieren.
        throw new UnsupportedOperationException("TODO: Milchmenge sequenziell berechnen");
    }

    public int parallel() {
        // TODO: Aufgabe - Milk: Alle Detailabfragen über CocktailService asynchron starten.
        // Erst danach die Ergebnisse abwarten und die Milchmengen addieren.
        throw new UnsupportedOperationException("TODO: Milchmenge parallel berechnen");
    }
}
