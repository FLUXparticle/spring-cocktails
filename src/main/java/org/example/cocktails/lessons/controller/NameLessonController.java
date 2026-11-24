package org.example.cocktails.lessons.controller;

import jakarta.validation.*;
import jakarta.validation.constraints.*;
import org.example.cocktails.lessons.service.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/lessons/names")
public class NameLessonController {
    private final SessionNames names;

    public NameLessonController(SessionNames names) {
        this.names = names;
    }

    @GetMapping
    public String hello(Model model) {
        model.addAttribute("form", new NameForm());
        populateNames(model);
        return "lessons/names";
    }

    @PostMapping
    public String saveName(@Valid @ModelAttribute("form") NameForm form, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            populateNames(model);
            return "lessons/names";
        }
        names.add(form.getName());
        return "redirect:/lessons/names";
    }

    private void populateNames(Model model) {
        model.addAttribute("names", names.all());
    }

    public static class NameForm {
        @NotBlank(message = "Name ist erforderlich.")
        @Size(max = 100, message = "Maximal 100 Zeichen")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
