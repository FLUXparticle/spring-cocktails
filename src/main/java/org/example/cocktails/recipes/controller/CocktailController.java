package org.example.cocktails.recipes.controller;

import org.example.cocktails.recipes.service.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mvc")
public class CocktailController {

    private final CocktailService cocktailService;

    public CocktailController(CocktailService cocktailService) {
        this.cocktailService = cocktailService;
    }

    @GetMapping({"", "/"})
    public String main() {
        return "mvc";
    }

    @GetMapping("/search")
    public String search() {
        // TODO: Aufgabe - Cocktails: Das Suchformular anzeigen.
        throw new UnsupportedOperationException("Aufgabe - Cocktails");
    }

    @PostMapping("/search")
    public String search(@RequestParam String query, Model model) {
        // TODO: Aufgabe - Cocktails: Nach Cocktail- und Zutatennamen suchen und doppelte Treffer vermeiden.
        throw new UnsupportedOperationException("Aufgabe - Cocktails");
    }

    @GetMapping("/cocktails")
    public String cocktails(Model model) {
        // TODO: Aufgabe - Cocktails: Die Cocktail-Liste laden und für die Anzeige bereitstellen.
        throw new UnsupportedOperationException("Aufgabe - Cocktails");
    }

    @GetMapping("/cocktails/{id}")
    public String cocktail(@PathVariable Long id, Model model) {
        // TODO: Aufgabe - Cocktails: Das Rezept zur Cocktail-ID laden und für die Anzeige bereitstellen.
        throw new UnsupportedOperationException("Aufgabe - Cocktails");
    }

    @GetMapping("/ingredients")
    public String ingredients(Model model) {
        // TODO: Aufgabe - Zutaten: Die Zutatenliste laden und für die Anzeige bereitstellen.
        throw new UnsupportedOperationException("Aufgabe - Zutaten");
    }

    @GetMapping("/ingredients/{id}")
    public String ingredient(@PathVariable Long id, Model model) {
        // TODO: Aufgabe - Zutaten: Eine Zutat mit den zugehörigen Cocktails laden und für die Anzeige bereitstellen.
        throw new UnsupportedOperationException("Aufgabe - Zutaten");
    }

}
