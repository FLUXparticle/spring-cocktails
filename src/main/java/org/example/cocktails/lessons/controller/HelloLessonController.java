package org.example.cocktails.lessons.controller;

import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/lessons/hello")
public class HelloLessonController {
    @GetMapping
    public String hello(Model model) {
        model.addAttribute("name", "Welt");
        return "lessons/hello";
    }

    @PostMapping("/name")
    public String greet(@RequestParam String name, Model model) {
        model.addAttribute("name", name);
        return "lessons/hello";
    }
}
