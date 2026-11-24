package org.example.cocktails.bars.controller;

import jakarta.servlet.http.*;
import jakarta.validation.*;
import org.example.cocktails.bars.service.*;
import org.springframework.dao.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.validation.*;
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
    public String list(@RequestParam(defaultValue = "") String search,
                       @RequestParam(required = false) String sortField, @RequestParam(defaultValue = "ASC") String sortDirection,
                       Model model) {
        // TODO: Aufgabe - Paging: Den GET-Parameter first entgegennehmen und an die Abfrage weiterreichen.
        populateList(search, sortField == null ? "name" : sortField, sortDirection, model);
        model.addAttribute("sortActive", sortField != null);
        var bulk = new BulkForm();
        bulk.setVersions(service.versions());
        model.addAttribute("bulk", bulk);
        return "bars/list";
    }

    private void populateList(String search, String field, String direction, Model model) {
        model.addAttribute("page", queries.find(search, field, direction));
        model.addAttribute("search", search);
        model.addAttribute("sortActive", true);
        model.addAttribute("sortField", field);
        model.addAttribute("sortDirection", direction.toUpperCase(java.util.Locale.ROOT));
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BarForm());
        return "bars/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") BarForm form, BindingResult errors, RedirectAttributes redirect) {
        if (errors.hasErrors()) return "bars/form";
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
    public String update(@PathVariable Long id, @Valid @ModelAttribute("form") BarForm form, BindingResult errors,
                         Model model, HttpServletResponse response, RedirectAttributes redirect) {
        model.addAttribute("barId", id);
        if (errors.hasErrors()) return "bars/form";
        try {
            service.update(form.toBar(id));
        } catch (BarConflictException | OptimisticLockingFailureException ex) {
            // The failed service transaction has already rolled back. Reload in a new transaction.
            var current = service.get(id);
            form.setVersion(BarVersions.get(current));
            model.addAttribute("databaseBar", current);
            model.addAttribute("message", "Zwischenzeitliche Änderung: Vergleichen und Änderungen bewusst zusammenführen.");
            response.setStatus(409);
            return "bars/merge";
        }
        redirect.addFlashAttribute("message", "Bar gespeichert.");
        return "redirect:/bars";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, @RequestParam(required = false) Long version, RedirectAttributes redirect) {
        service.delete(id, version);
        redirect.addFlashAttribute("message", "Bar gelöscht. Die Historie bleibt erhalten.");
        return "redirect:/bars";
    }

    @GetMapping("/{id}/history")
    public String history(@PathVariable Long id, Model model) {
        model.addAttribute("history", service.history(id));
        model.addAttribute("barId", id);
        return "bars/history";
    }

    @PostMapping("/bulk")
    public String bulk(@Valid @ModelAttribute("bulk") BulkForm form, BindingResult errors, Model model,
                       HttpServletResponse response, RedirectAttributes redirect) {
        if (!errors.hasErrors()) {
            try {
                service.bulkUpdate(form.getVersions(), form.getAddress());
                redirect.addFlashAttribute("message", "Alle Bars gemeinsam aktualisiert.");
                return "redirect:/bars";
            } catch (BarConflictException | OptimisticLockingFailureException | BarNotFoundException ex) {
                errors.reject("conflict", "Eine Bar wurde geändert oder gelöscht. Bitte die Liste neu laden und den Datenstand prüfen.");
                response.setStatus(409);
            }
        }
        populateList("", "name", "ASC", model);
        return "bars/list";
    }
}
