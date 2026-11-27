package org.example.security.controller;

import org.springframework.context.annotation.*;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class DemoPagesController {
    @GetMapping("/")
    public String index() {
        return "redirect:/good-user.html";
    }

    @GetMapping("/good-user.html")
    public String user(Model model) {
        model.addAttribute("admin", false);
        model.addAttribute("username", "employee");
        return "good";
    }

    @GetMapping("/good-admin.html")
    public String admin(Model model) {
        model.addAttribute("admin", true);
        model.addAttribute("username", "admin");
        return "good";
    }

    @GetMapping("/demo-assets/{name}")
    @ResponseBody
    public ResponseEntity<Resource> asset(@PathVariable String name) {
        if (!name.equals("good.js") && !name.equals("good.css")) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(name.endsWith(".js") ? "text/javascript" : "text/css"))
                .body(new ClassPathResource("security-demo/" + name));
    }
}
