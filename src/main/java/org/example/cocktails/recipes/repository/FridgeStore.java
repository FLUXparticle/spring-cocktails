package org.example.cocktails.recipes.repository;

import java.util.HashSet;
import java.util.Set;

public class FridgeStore {
    private final Set<Long> stock = new HashSet<>();

    public Set<Long> ingredients() {
        // TODO: Aufgabe - Kühlschrank synchronisieren: Lesen und Snapshot-Erstellung mit einem Leselock schützen.
        return Set.copyOf(stock);
    }

    public void setIngredient(Long id, boolean present) {
        // TODO: Aufgabe - Kühlschrank synchronisieren: Änderungen mit einem Schreiblock schützen.
        if (present) stock.add(id);
        else stock.remove(id);
    }
}
