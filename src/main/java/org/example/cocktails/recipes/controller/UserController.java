package org.example.cocktails.recipes.controller;

import org.example.cocktails.recipes.service.*;
import org.springframework.security.core.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
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
        // TODO: Aufgabe - Kühlschrank: Benutzername und Kühlschrankbestand ins Model übernehmen und die Seite anzeigen.
        throw new UnsupportedOperationException("Aufgabe - Kühlschrank");
    }

    @PostMapping("/fridge")
    public String change(@RequestParam Long id, @RequestParam String action) {
        // TODO: Aufgabe - Kühlschrank: Hinzufügen oder Entfernen verarbeiten und zum Kühlschrank zurückleiten.
        throw new UnsupportedOperationException("Aufgabe - Kühlschrank");
    }

    @GetMapping("/possible")
    public String possible(Model model) {
        // TODO: Aufgabe - Kühlschrank: Die mit dem eigenen Bestand mixbaren Cocktails anzeigen.
        throw new UnsupportedOperationException("Aufgabe - Kühlschrank");
    }

}
