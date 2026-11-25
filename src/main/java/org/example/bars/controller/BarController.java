package org.example.bars.controller;

import org.example.bars.service.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

@Controller
@RequestMapping("/bars")
public class BarController {
    private final BarService service;
    private final BarQueryService queries;

    public BarController(BarService service, BarQueryService queries) {
        this.service = service;
        this.queries = queries;
    }

    @GetMapping({"", "/"})
    public String list(@RequestParam(defaultValue = "") String search, Model model) {
        model.addAttribute("bars", queries.find(search));
        model.addAttribute("search", search);
        return "bars/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BarForm());
        return "bars/form";
    }

    @PostMapping
    public String create(@ModelAttribute("form") BarForm form, RedirectAttributes redirect) {
        service.create(form.toBar(null));
        redirect.addFlashAttribute("message", "Bar angelegt.");
        return "redirect:/bars";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("form", new BarForm(service.get(id)));
        model.addAttribute("barId", id);
        return "bars/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("form") BarForm form,
                         Model model, RedirectAttributes redirect) {
        model.addAttribute("barId", id);
        service.update(form.toBar(id));
        redirect.addFlashAttribute("message", "Bar gespeichert.");
        return "redirect:/bars";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        service.delete(id);
        redirect.addFlashAttribute("message", "Bar gelöscht. Die Historie bleibt erhalten.");
        return "redirect:/bars";
    }

    @GetMapping("/{id}/history")
    public String history(@PathVariable Long id, Model model) {
        model.addAttribute("history", service.history(id));
        model.addAttribute("barId", id);
        return "bars/history";
    }

}
