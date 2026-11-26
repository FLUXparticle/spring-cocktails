package org.example.cocktails.recipes.controller;

import org.example.cocktails.recipes.service.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mvc/cart")
public class CartController {
    private final CartService cart;

    public CartController(CartService cart) {
        this.cart = cart;
    }

    @GetMapping
    public String view(Model model) {
        // TODO: Aufgabe - Warenkorb: Warenkorb und verfügbare Cocktails ins Model übernehmen und die Seite anzeigen.
        throw new UnsupportedOperationException("Aufgabe - Warenkorb");
    }

    @PostMapping("/actions/add-cocktail")
    public String add(@RequestParam Long cocktailId) {
        // TODO: Aufgabe - Warenkorb: Die Zutaten des gewählten Cocktails hinzufügen und anschließend zurückleiten.
        throw new UnsupportedOperationException("Aufgabe - Warenkorb");
    }

    @PostMapping("/items/{id}/remove")
    public String remove(@PathVariable Long id) {
        // TODO: Aufgabe - Warenkorb: Die gewählte Zutat entfernen und anschließend zurückleiten.
        throw new UnsupportedOperationException("Aufgabe - Warenkorb");
    }

    @PostMapping("/clear")
    public String clear() {
        cart.clear();
        return "redirect:/mvc/cart";
    }
}
