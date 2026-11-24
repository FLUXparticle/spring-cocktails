package org.example.cocktails.lessons.controller;

import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/lessons/error")
public class ErrorLessonController {
    @GetMapping
    public String form(Model model) {
        model.addAttribute("top", "");
        model.addAttribute("bottom", "");
        return "lessons/error";
    }

    @PostMapping("/top")
    public String top(@RequestParam String top, Model model) {
        model.addAttribute("top", top);
        model.addAttribute("bottom", top);
        return "lessons/error";
    }

    @PostMapping("/bottom")
    public String bottom(@RequestParam String bottom, Model model) {
        model.addAttribute("top", "");
        model.addAttribute("bottom", "Fehler!");
        return "lessons/error";
    }
}
