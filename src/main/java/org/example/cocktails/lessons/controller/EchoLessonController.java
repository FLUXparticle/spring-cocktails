package org.example.cocktails.lessons.controller;

import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/lessons/echo")
public class EchoLessonController {
    @GetMapping
    public String form(Model model) {
        model.addAttribute("top", "");
        model.addAttribute("bottom", "");
        return "lessons/echo";
    }

    @PostMapping("/top")
    public String top(@RequestParam String top, Model model) {
        model.addAttribute("top", top);
        model.addAttribute("bottom", top);
        return "lessons/echo";
    }

    @PostMapping("/bottom")
    public String bottom(@RequestParam String bottom, Model model) {
        model.addAttribute("top", bottom);
        model.addAttribute("bottom", bottom);
        return "lessons/echo";
    }
}
