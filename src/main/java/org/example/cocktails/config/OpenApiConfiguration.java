package org.example.cocktails.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Cocktails – REST-Aufgabe", version = "1.0", description = """
        Aufgabe: Die TODOs in RecipesRestController durch Aufrufe der vorhandenen Services ersetzen.
        Der Controller liefert JSON statt HTML und Redirects. Beispiele stammen aus cocktails.db;
        bei Listen sind Auszüge ausdrücklich gekennzeichnet. IDs und Rezeptmengen sind echt.
        Auf master sind die Aufgabenmethoden noch nicht implementiert: Execute erreicht dort
        eine UnsupportedOperationException (HTTP 500). Die dokumentierten Erfolgsantworten
        beschreiben die zu implementierende Lösung und sind keine simulierten Antworten.

        Katalog: öffentlich. Kühlschrank: je angemeldetem Benutzer, nur im Arbeitsspeicher.
        Vorläufig wird der vorhandene Session-Login verwendet: /login in einem zweiten Tab
        öffnen (test/test oder demo/demo), danach zu Swagger zurückkehren. Der Browser sendet
        das Session-Cookie automatisch; kein Cookie muss in Authorize eingetragen werden.
        Danach GET /api/session ausführen, um den CSRF-Cookie zu laden. Swagger sendet ihn
        bei PATCH automatisch als Header X-XSRF-TOKEN. Nach erneutem Login oder Logout
        GET /api/session wiederholen. JWT ist eine spätere, getrennte Aufgabe und noch nicht aktiv.
        Einen REST-Warenkorb gibt es nicht.
        """))
@SecurityScheme(name = "session", type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.COOKIE, paramName = "JSESSIONID",
        description = "Session nach Anmeldung über /login. Der Browser sendet das Cookie automatisch; JWT folgt später.")
public class OpenApiConfiguration {
}
