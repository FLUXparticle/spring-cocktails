package org.example.cocktails.recipes.controller;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class ClientPagesController {
    @GetMapping({"/rest", "/rest/"})
    public String rest() {
        return "forward:/rest/index.html";
    }

}
