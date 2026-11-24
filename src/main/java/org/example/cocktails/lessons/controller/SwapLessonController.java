package org.example.cocktails.lessons.controller;

import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/lessons/swap")
public class SwapLessonController {
    @GetMapping
    public String form(Model model) {
        model.addAttribute("top", "");
        model.addAttribute("bottom", "");
        return "lessons/swap";
    }

    @PostMapping("/both")
    public String both(@RequestParam String top, @RequestParam String bottom, Model model) {
        model.addAttribute("top", bottom);
        model.addAttribute("bottom", top);
        return "lessons/swap";
    }
}
