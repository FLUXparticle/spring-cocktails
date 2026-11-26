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
        var view = cart.getCart();
        model.addAttribute("ingredients", view.ingredients());
        model.addAttribute("cocktails", view.cocktails());
        return "cart";
    }

    @PostMapping("/actions/add-cocktail")
    public String add(@RequestParam Long cocktailId) {
        cart.addCocktail(cocktailId);
        return "redirect:/mvc/cart";
    }

    @PostMapping("/items/{id}/remove")
    public String remove(@PathVariable Long id) {
        cart.removeIngredient(id);
        return "redirect:/mvc/cart";
    }

    @PostMapping("/clear")
    public String clear() {
        cart.clear();
        return "redirect:/mvc/cart";
    }
}
