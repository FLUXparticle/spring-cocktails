package org.example.cocktails.lessons.controller;

import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/lessons/repeat")
public class RepeatLessonController {
    @GetMapping
    public String form(Model model) {
        model.addAttribute("top", "");
        model.addAttribute("bottom", "");
        return "lessons/repeat";
    }

    @PostMapping("/top")
    public String top(@RequestParam String top, Model model) {
        model.addAttribute("top", top);
        model.addAttribute("bottom", top);
        return "lessons/repeat";
    }

    @PostMapping("/bottom")
    public String bottom() {
        return "lessons/repeat";
    }
}
