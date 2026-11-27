package org.example.cocktails.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
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
        POST /api/auth/login mit {"username":"test","password":"test"} liefert ein JWT.
        Den zurückgegebenen Token unter Authorize eintragen; Swagger sendet ihn als
        Authorization: Bearer … . Das JWT bleibt im Arbeitsspeicher, nicht in Cookies.
        GET /api/auth/me zeigt Benutzer und Berechtigungen des geprüften Tokens.
        Katalog und Kühlschrank bleiben Aufgaben-TODOs, die Authentifizierung ist vorgegeben.
        Die API ist stateless und benötigt keinen CSRF-Token. Der MVC-Login unter /login
        verwendet weiterhin eine separate HTTP-Session und CSRF für Formulare.
        Einen REST-Warenkorb gibt es nicht.
        """))
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT",
        description = "Token aus POST /api/auth/login hier ohne das Präfix Bearer eintragen.")
public class OpenApiConfiguration {
}
