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
        return "search";
    }

    @PostMapping("/search")
    public String search(@RequestParam String query, Model model) {
        var cocktails = cocktailService.search(query);
        model.addAttribute("cocktails", cocktails);
        return "result";
    }

    @GetMapping("/cocktails")
    public String cocktails(Model model) {
        var cocktails = cocktailService.cocktails();
        model.addAttribute("cocktails", cocktails);
        return "cocktails";
    }

    @GetMapping("/cocktails/{id}")
    public String cocktail(@PathVariable Long id, Model model) {
        var cocktail = cocktailService.cocktail(id);
        model.addAttribute("name", cocktail.name());
        model.addAttribute("instructions", cocktail.instructions());
        return "cocktail";
    }

    @GetMapping("/ingredients")
    public String ingredients(Model model) {
        var ingredients = cocktailService.ingredients();
        model.addAttribute("ingredients", ingredients);
        return "ingredients";
    }

    @GetMapping("/ingredients/{id}")
    public String ingredient(@PathVariable Long id, Model model) {
        var ingredient = cocktailService.ingredient(id);
        var cocktails = ingredient.cocktails();
        model.addAttribute("name", ingredient.name());
        model.addAttribute("cocktails", cocktails);
        return "ingredient";
    }

}
