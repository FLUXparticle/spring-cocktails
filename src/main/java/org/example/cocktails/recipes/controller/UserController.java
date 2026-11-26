package org.example.cocktails.recipes.controller;

import org.example.cocktails.recipes.service.*;
import org.springframework.security.core.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mvc/user")
public class UserController {
    private final FridgeService fridge;

    public UserController(FridgeService fridge) {
        this.fridge = fridge;
    }

    @GetMapping("/fridge")
    public String fridge(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("fridgeModels", fridge.getFridgeIngredients());
        return "fridge";
    }

    @PostMapping("/fridge")
    public String change(@RequestParam Long id, @RequestParam String action) {
        if (!action.equals("add") && !action.equals("remove"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unbekannte Aktion.");
        fridge.updateIngredientStatus(id, action.equals("add"));
        return "redirect:/mvc/user/fridge";
    }

    @GetMapping("/possible")
    public String possible(Model model) {
        model.addAttribute("cocktails", fridge.getPossibleCocktails());
        return "possible";
    }

}
