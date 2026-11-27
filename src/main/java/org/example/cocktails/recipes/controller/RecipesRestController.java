package org.example.cocktails.recipes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.example.cocktails.recipes.entry.ApiModels.*;
import org.example.cocktails.recipes.service.CocktailService;
import org.example.cocktails.recipes.service.FridgeService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RecipesRestController {
    private final CocktailService catalog;
    private final FridgeService fridge;

    public RecipesRestController(CocktailService catalog, FridgeService fridge) {
        this.catalog = catalog;
        this.fridge = fridge;
    }

    @GetMapping("/cocktails")
    @Operation(tags = "Cocktails", summary = "Alle Cocktails anzeigen",
            description = "Die vollständige Liste aus catalog.cocktails() als JSON zurückgeben. Keine Rezeptdetails.")
    @ApiResponse(responseCode = "200", description = "Cocktail-Liste; Beispiel ist ein Auszug aus dem Katalog.",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CocktailSummary.class)),
                    examples = @ExampleObject(name = "Katalog-Auszug", value = RecipesApiExamples.COCKTAILS)))
    public List<CocktailSummary> cocktails() {
        // TODO: Aufgabe - REST-API: Die Cocktail-Liste des Services direkt als JSON zurückgeben.
        throw new UnsupportedOperationException("Aufgabe - REST-API");
    }

    @GetMapping("/cocktails/{id}")
    @Operation(tags = "Cocktails", summary = "Ein Cocktailrezept anzeigen",
            description = "catalog.cocktail(id) zurückgeben, einschließlich aller Zutaten und Mengen in cl.")
    @ApiResponse(responseCode = "200", description = "Vollständiges Rezept für Milkshake (ID 24).",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CocktailDetail.class),
                    examples = @ExampleObject(value = RecipesApiExamples.COCKTAIL)))
    @ApiResponse(responseCode = "404", description = "Kein Cocktail mit dieser ID vorhanden.", content = @Content)
    public CocktailDetail cocktail(@Parameter(description = "Cocktail-ID, zum Beispiel Milkshake", example = "24") @PathVariable Long id) {
        // TODO: Aufgabe - REST-API: Das Rezept zur ID vom Service laden und zurückgeben.
        throw new UnsupportedOperationException("Aufgabe - REST-API");
    }

    @GetMapping("/cocktails/search")
    @Operation(tags = "Cocktails", summary = "Nach Cocktail- und Zutatennamen suchen",
            description = "catalog.search(query) verwenden. Groß-/Kleinschreibung ignorieren und doppelte Treffer vermeiden. Ein leerer Suchbegriff liefert alle Cocktails.")
    @ApiResponse(responseCode = "200", description = "Vollständige Trefferliste für query=Milch.",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CocktailSummary.class)),
                    examples = @ExampleObject(value = RecipesApiExamples.SEARCH)))
    public List<CocktailSummary> search(@Parameter(description = "Teil eines Cocktail- oder Zutatennamens", example = "Milch") @RequestParam String query) {
        // TODO: Aufgabe - REST-API: Den GET-Parameter query an die Suche des Services übergeben.
        throw new UnsupportedOperationException("Aufgabe - REST-API");
    }

    @GetMapping("/ingredients")
    @Operation(tags = "Zutaten", summary = "Alle Zutaten anzeigen", description = "catalog.ingredients() als JSON-Liste zurückgeben.")
    @ApiResponse(responseCode = "200", description = "Alle Zutaten; Beispiel zeigt die vier Zutaten für Milkshake als Auszug.",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = IngredientView.class)),
                    examples = @ExampleObject(name = "Zutaten-Auszug", value = RecipesApiExamples.INGREDIENTS)))
    public List<IngredientView> ingredients() {
        // TODO: Aufgabe - REST-API: Die Zutatenliste des Services zurückgeben.
        throw new UnsupportedOperationException("Aufgabe - REST-API");
    }

    @GetMapping("/ingredients/{id}")
    @Operation(tags = "Zutaten", summary = "Eine Zutat mit ihren Cocktails anzeigen",
            description = "catalog.ingredient(id) zurückgeben. Die Antwort enthält ID, Name und alle Cocktails mit dieser Zutat.")
    @ApiResponse(responseCode = "200", description = "Zutat Milch (ID 29) mit allen zugehörigen Cocktails.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = IngredientDetail.class),
                    examples = @ExampleObject(value = RecipesApiExamples.INGREDIENT)))
    @ApiResponse(responseCode = "404", description = "Keine Zutat mit dieser ID vorhanden.", content = @Content)
    public IngredientDetail ingredient(@Parameter(description = "Zutaten-ID, zum Beispiel Milch", example = "29") @PathVariable Long id) {
        // TODO: Aufgabe - REST-API: Die Zutat mit den zugehörigen Cocktails vom Service zurückgeben.
        throw new UnsupportedOperationException("Aufgabe - REST-API");
    }

    @GetMapping("/fridge/ingredients")
    @Operation(tags = "Kühlschrank", summary = "Zutatenbestand des angemeldeten Benutzers anzeigen",
            description = "fridge.getFridgeIngredients() zurückgeben. Für jede Katalog-Zutat zeigt inFridge, ob sie im eigenen Kühlschrank liegt.")
    @SecurityRequirement(name = "session")
    @ApiResponse(responseCode = "200", description = "Alle Zutaten mit Bestand; Beispiel ist ein Auszug mit allen Milkshake-Zutaten im Kühlschrank.",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FridgeIngredient.class)),
                    examples = @ExampleObject(name = "Bestands-Auszug", value = RecipesApiExamples.FRIDGE)))
    @ApiResponse(responseCode = "401", description = "Zuerst über /login anmelden.", content = @Content)
    public List<FridgeIngredient> fridge() {
        // TODO: Aufgabe - REST-API: Den Bestand des angemeldeten Benutzers vom FridgeService zurückgeben.
        throw new UnsupportedOperationException("Aufgabe - REST-API");
    }

    @PatchMapping("/fridge/ingredients/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(tags = "Kühlschrank", summary = "Eine Zutat hinzufügen oder entfernen",
            description = "fridge.updateIngredientStatus(id, request.inFridge()) aufrufen. true fügt hinzu, false entfernt. Keine Benutzer-ID annehmen: Der Service wählt den eigenen Kühlschrank. Erfolg: HTTP 204 ohne Antwortkörper. Vor Execute einmal GET /api/session ausführen (CSRF).")
    @SecurityRequirement(name = "session")
    @ApiResponse(responseCode = "204", description = "Bestand geändert; kein Antwortkörper.", content = @Content)
    @ApiResponse(responseCode = "400", description = "inFridge fehlt, ist null oder der JSON-Body ist ungültig.", content = @Content)
    @ApiResponse(responseCode = "401", description = "Nicht angemeldet.", content = @Content)
    @ApiResponse(responseCode = "403", description = "CSRF-Token fehlt/ist ungültig oder die Berechtigung fehlt.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Keine Zutat mit dieser ID vorhanden.", content = @Content)
    public void updateFridge(@Parameter(description = "Zutaten-ID, zum Beispiel Milch", example = "29") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = IngredientStatus.class),
                            examples = {@ExampleObject(name = "Milch hinzufügen", value = "{\"inFridge\":true}"),
                                    @ExampleObject(name = "Milch entfernen", value = "{\"inFridge\":false}")}))
            @Valid @RequestBody IngredientStatus request) {
        // TODO: Aufgabe - REST-API: Den JSON-Body an den FridgeService übergeben; danach ohne Body mit HTTP 204 antworten.
        throw new UnsupportedOperationException("Aufgabe - REST-API");
    }

    @GetMapping("/fridge/possible")
    @Operation(tags = "Kühlschrank", summary = "Mit dem eigenen Bestand mögliche Cocktails anzeigen",
            description = "fridge.getPossibleCocktails() zurückgeben. Milkshake ist möglich, wenn die Zutaten 13, 27, 29 und 30 vorhanden sind; nur Milch (29) genügt nicht.")
    @SecurityRequirement(name = "session")
    @ApiResponse(responseCode = "200", description = "Mögliche Cocktails; Beispiel bei Bestand {13,27,29,30}.",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CocktailSummary.class)),
                    examples = @ExampleObject(value = RecipesApiExamples.POSSIBLE)))
    @ApiResponse(responseCode = "401", description = "Zuerst anmelden.", content = @Content)
    public List<CocktailSummary> possible() {
        // TODO: Aufgabe - REST-API: Die mit dem eigenen Kühlschrankbestand möglichen Cocktails zurückgeben.
        throw new UnsupportedOperationException("Aufgabe - REST-API");
    }

    @GetMapping("/session")
    @Operation(tags = "Session (vorgegeben)", summary = "Anmeldestatus prüfen und CSRF-Cookie laden",
            description = "Hilfsmethode ist bereits implementiert und gehört nicht zu den Aufgaben-TODOs. Nach Login/Logout ausführen. Swagger liest den Cookie XSRF-TOKEN und sendet ihn bei PATCH als X-XSRF-TOKEN. Keine JWT-Ausstellung.")
    @ApiResponse(responseCode = "200", description = "Aktueller Anmeldestatus; auch ohne Login aufrufbar.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SessionView.class),
                    examples = {@ExampleObject(name = "Angemeldet", value = "{\"authenticated\":true,\"username\":\"test\"}"),
                            @ExampleObject(name = "Nicht angemeldet", value = "{\"authenticated\":false,\"username\":null}")}))
    public SessionView session(@Parameter(hidden = true) Authentication auth, @Parameter(hidden = true) CsrfToken csrf) {
        csrf.getToken();
        boolean authenticated = auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
        return new SessionView(authenticated, authenticated ? auth.getName() : null);
    }

    public record IngredientStatus(@NotNull @Schema(description = "Zutat im eigenen Kühlschrank vorhanden?", example = "true", requiredMode = Schema.RequiredMode.REQUIRED) Boolean inFridge) {
    }

    public record SessionView(boolean authenticated, String username) {
    }
}
