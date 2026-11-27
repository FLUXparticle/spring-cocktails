# Spring – Tag 1

Der erste Kurstag behandelt HTML-Formulare mit Spring MVC, Session-Zustand,
Persistenz mit JPA und H2 sowie konkurrierende Bearbeitung und Transaktionen.
Der Code verwendet Java 17 oder neuer und Spring Boot 4.2.0.

## Starten

```sh
mvn spring-boot:run
# Alternativ:
mvn package
java -jar target/spring-cocktails-1.0-SNAPSHOT.jar
```

Startseite: <http://localhost:8080/>. Die Bar-Tabelle liegt unter `/bars`.
Die HTML-Seiten sind bewusst schlicht, ohne CSS und Ajax. Cocktail-MVC mit
SQLite und Session-Login sowie die REST-API mit Swagger sind als Aufgaben
vorbereitet. Der JavaScript-Client mit JWT-Login ist vorgegeben. Das Security Lab läuft als
eigene Application.

## Branches und Aufgaben

`master` enthält den vorbereiteten Aufgabenstand und einen zusätzlichen Commit,
der die Bar-Umgebung vereinfacht und in bestehende Lösungen gemergt werden kann.
Die Bar-Grundfunktionen sind vorgegeben. Die Einstiegsaufgaben werden anhand der
Vorlagen im Skript ergänzt. `solution` baut auf demselben Commit auf und stellt
nacheinander die Lösungen zu den folgenden Aufgaben her:

| Schritt | Aufgabe | Ergebnis |
| --- | --- | --- |
| 1 | Greeting | GET `/lessons/hello`, Formular-POST an `/lessons/hello/name`, „Hallo, Name!“ |
| 2 | Repeat | Eigene Demos für Repeat, Error, Echo und Swap |
| 3 | Namensliste | Namen pro Browser-Session sammeln, validieren und nach POST umleiten |
| 4 | Doppelte Bearbeitung erkennen | Versionsfeld mit `@Version`, Getter und Setter ergänzen |
| 5 | Transaktionen | Bar und Historie mit `@Transactional` gemeinsam speichern und den Rollback testen |
| 6 | Validierung | Feldregeln, Controller-Validierung und Formularmeldungen für Bars ergänzen |

### Textfelder

Bei Repeat haben „Oben“ und „Unten“ eigene Formulare mit den POST-Zielen `/top`
und `/bottom`. Zunächst verarbeitet der untere Empfänger nichts: Beide Felder sind
anschließend leer, weil das Model für jeden Request neu aufgebaut wird. Error setzt
unten „Fehler!“, kennt aber den nicht gesendeten oberen Wert nicht. Echo kopiert
auch unten nach oben. Bei Swap werden die beiden Formulare zusammengefasst;
beide Buttons bleiben und senden an `/lessons/swap/both`.

### Namensliste

Die Namensliste unter `/lessons/names` bleibt in einem `@SessionScope`-Bean.
Zwei unabhängige Browser-Sitzungen haben unterschiedliche Listen. Eine Umstellung
auf eine Datenbank ist nicht Teil der Aufgaben. Nach erfolgreichem Speichern
verhindert POST/Redirect/GET ein versehentliches erneutes Hinzufügen beim Neuladen.

## Vorgegebene Bar-Umgebung

Beim Start entstehen 26 Bars von „Bar A“ bis „Bar Z“, mit E-Mail-Adressen von
`bar01@example.com` bis `bar26@example.com`. Anlegen, Bearbeiten, Löschen, Suche
und Historie sind vorgegeben.
Die Suche durchsucht Name, E-Mail und Adresse. Validierung wird erst in der
letzten Aufgabe ergänzt; der Aufgabenstand enthält noch keine Feldprüfungen.

Alle passenden Bars werden in einer Liste angezeigt.

### Doppelte Bearbeitung vorführen

Eine Bar in zwei Tabs öffnen. Im ersten Tab ändern und speichern, danach im zweiten
Tab veraltete Daten speichern. Ohne Versionsfeld überschreibt die zweite Änderung
die erste unbemerkt. Nach Ergänzen von `@Version` wird ein Konflikt erkannt. Die
vorgegebene Umgebung (`BarVersions` und Formulare) funktioniert mit
und ohne Versionsfeld und gehört nicht zur eigentlichen Aufgabe.

### Fehlende Transaktion vorführen

Nach der Versionsaufgabe eine Bar in zwei Tabs öffnen. Im ersten Tab ändern und
speichern, anschließend im zweiten Tab veraltete Daten speichern. `update` speichert
absichtlich zuerst den Historieneintrag und danach die Bar. Die zweite Speicherung
scheitert am Versionskonflikt. Ohne gemeinsame Transaktion bleibt dadurch ein
Historieneintrag für eine Änderung stehen, die nie gespeichert wurde.

Die Aufgabe besteht darin, `update` mit `@Transactional` zu versehen. Dann wird
auch der Historieneintrag zurückgerollt. Die erfolgreiche Änderung aus dem ersten
Tab bleibt bestehen. Der Teilnehmer-Test soll genau diesen Zusammenhang prüfen.

## Vorgegebene Konfiguration

`BarsApplication` liegt im Basispaket `org.example.bars`. Spring Boot findet dort
Controller, Services, Entities und Repositories automatisch und konfiguriert JPA
samt TransactionManager. Eine eigene Persistence-Konfiguration ist nicht nötig;
für eine Transaktion genügt `@Transactional`.

`DatabaseConfiguration` im Paket `org.example.bars.config` stellt die H2-Datenquelle als `DataSource`-Bean
bereit. Die Datenbank läuft ausschließlich im Speicher. Beim direkten Start von
`BarsApplication` ist damit bereits die richtige Datenbank gewählt; Profile oder
Startparameter sind nicht erforderlich. EntityManagerFactory und TransactionManager
konfiguriert Spring Boot weiterhin automatisch.
Die Beispielbars werden beim Start automatisch angelegt, sofern die Tabelle leer ist.

`CocktailWebApplication` bleibt unter `org.example.cocktails` und startet einen
eigenen Anwendungskontext. Die beiden Anwendungen scannen ausschließlich ihr
jeweiliges Basispaket. Der Cocktail-Teil verwendet seine SQLite-Datenbank,
ohne die JPA-Konfiguration der Bars zu beeinflussen. Die Anwendungen
werden einzeln gestartet; für gleichzeitigen Betrieb benötigen sie unterschiedliche
HTTP-Ports, etwa `--server.port=8081` für die zweite Anwendung.

Maven und das ausführbare JAR starten standardmäßig `BarsApplication`. In der IDE
kann die gewünschte Application direkt gestartet werden. Bei Teilnehmer-Tests
liegen die Klassen ebenfalls unter `org.example.bars`, damit `@SpringBootTest`
die richtige Application automatisch findet.

## Prüfen

```sh
mvn verify
# Auf solution nach der Transaktionsaufgabe:
mvn -Dtest=BarTransactionTest test
```

Im Initialstand gibt es noch keine Teilnehmer-Tests. Der Lösungsbranch ergänzt
`BarTransactionTest`: Ein veraltetes Update scheitert am Versionskonflikt; sowohl
der Datenstand der Bar als auch die Anzahl der Historieneinträge bleiben unverändert.

## Vereinfachung der Bar-Umgebung

Gespeichert wird mit `save`, ohne explizites `flush`. Beim Bearbeiten geht die
Entity mit der im Formular mitgesendeten Version an das Repository. JPA übernimmt
die Versionsprüfung; der Anwendungscode vergleicht keine Versionsnummern.
Beim Löschen wird die Bar geladen und mit `delete` entfernt. Die Version aus
dem geöffneten Formular wird dabei nicht geprüft; die Demonstration veralteter
Formulardaten findet beim Bearbeiten statt.
`BarVersions` überträgt die optionale Version lediglich, damit
der Aufgabenstand auch ohne Versionsfeld läuft.

Ein Versionskonflikt löst eine `OptimisticLockingFailureException` aus und wird
über die Standard-Fehlerseite von Spring angezeigt. Eine eigene Fehlerbehandlung,
die Zusammenführungsoberfläche und die asynchrone E-Mail-Simulation entfallen.
Die Historie bleibt erhalten, um den Rollback einer Transaktion sichtbar zu machen.

Die Lösungen auf `solution` bauen auf dem vereinfachten Aufgabenstand auf.
Die Transaktionsaufgabe ergänzt `@Transactional` an `update` und den zugehörigen
Rollback-Test. Die Paging-Aufgabe entfällt.

Die Einstiegsaufgaben liegen unter `org.example.cocktails.lessons` und werden mit
`CocktailWebApplication` gestartet. Die Namensliste verwendet ausschließlich die
Session. Bars und Namensliste verwenden getrennte Anwendungskontexte.

Konfigurationsklassen liegen im jeweiligen `config`-Paket, Advice-Klassen im
`advice`-Paket, sofern solche Klassen benötigt werden.

### Aufgabe: Validierung

Zum Schluss werden die Bar-Formulare validiert: Name ist erforderlich und höchstens
20 Zeichen lang, E-Mail ist erforderlich und gültig, Adresse höchstens 20 Zeichen.
Die Aufgabe ergänzt Bean-Validation-Constraints an `BarForm` und `Bar`, `@Valid`
und `BindingResult` im Controller sowie Fehlermeldungen und Browser-Prüfungen im
HTML. Bei Fehlern bleiben die Eingaben im Formular erhalten; gespeichert wird erst
bei gültigen Daten. Im Service gibt es keine zusätzliche Methodenvalidierung.

## Milk-Aufgabe

`MilkServiceTest` startet den Spring-Kontext mit dem Profil `test`, injiziert
`MilkService` und prüft in beiden Varianten die Milchmenge `38` cl. Ein Webserver
wird nicht gestartet. Ausführen mit:

```sh
mvn -Dtest=MilkServiceTest test
```

Die Tests sind bereits vollständig. Auf `master` scheitern sie absichtlich an den
TODOs in `MilkService`, `CocktailService` und `RemoteCocktailRepository`.
Aufgabe der Teilnehmer ist es, diese Methoden zu implementieren. Alle anderen
Dateien sind vorgegeben. Die Zeitmessung dient nur der Sichtprüfung, es gibt
keine Zeit-Assertions.

Die Datenklassen und die Schnittstelle `CocktailRepository` stammen aus `freeze`.
Außerhalb des Profils `test` ist sie ein Spring-Data-JPA-Repository. Im Profil
`test` wird stattdessen `RemoteCocktailRepository` verwendet. Die vorgegebene
`recipes.config.RemoteConfiguration` stellt ausschließlich in diesem Profil
einen WebClient für `https://cocktails.fluxparticle.com` bereit.

Zuerst werden Cocktails mit "Milk" im Namen gesucht und deren Details nacheinander
abgerufen. Anschließend werden dieselben Detailabfragen parallel über
`CocktailService.findByIdAsync` gestartet. `@EnableAsync` steht bereits an
`CocktailWebApplication`; die Teilnehmer ergänzen `@Async` an der Service-Methode.
Der Aufruf aus `MilkService` geht durch den Spring-Proxy. Erst alle Abfragen
starten, danach die Futures abwarten und die Mengen der Zutat "Milch" summieren.
Die Repository-Aufrufe sind synchron (`WebClient` mit `block()`); die
Parallelisierung übernimmt Spring mit `@Async`. Erwartete Laufzeiten sind grob
vier beziehungsweise zwei Sekunden und hängen vom Remote-Server ab.
Eine Aufgabe zu Fehlerabbruch oder strukturierter Nebenläufigkeit gibt es nicht.

## MVC-Aufgaben: Cocktails, Zutaten und Kühlschrank

`CocktailWebApplication` direkt starten und `/mvc` öffnen. Hierfür sind keine
Profile oder Startparameter nötig. `org.example.cocktails.config.DatabaseConfiguration`
stellt den unveränderten SQLite-Katalog aus `freeze` bereit; JPA liest die bestehenden
Tabellen, ohne sie zu erzeugen oder zu verändern. Bars bleibt eine eigene Application
mit H2 im Speicher.

Die HTML-Seiten und Repository-Schnittstellen sind vorgegeben. Die Methoden in
`CocktailController`, `UserController`, `CocktailService` und `FridgeService`
enthalten die Aufgaben-TODOs aus `dev`:

- **Cocktails:** Liste, Rezeptdetails und Suche über Cocktail- und Zutatennamen.
- **Zutaten:** Liste und Details mit den zugehörigen Cocktails.
- **Kühlschrank:** Zutatenbestand des angemeldeten Benutzers anzeigen und ändern;
  die mit diesem Bestand möglichen Cocktails bestimmen.
- **Kühlschrank synchronisieren:** In `FridgeStore` Lesen inklusive Snapshot und
  Schreiben mit einem `ReentrantReadWriteLock` schützen.

`SecurityConfiguration` liegt in `org.example.cocktails.config` und schützt
`/mvc/user/**`. Spring stellt die einfache Standard-Loginseite bereit. Zugangsdaten:
`test` / `test` oder `demo` / `demo`. Katalog und Menü bleiben öffentlich.
Schreibende MVC-Formulare enthalten über Thymeleaf automatisch den CSRF-Token.
Dieser Stand verwendet Session-Login; JWT und REST werden hier noch nicht ergänzt.

Im `FridgeService` ist eine `Map<String, FridgeStore>` vorgegeben. Die ebenfalls
vorgegebene Methode `getCurrentFridge()` liefert direkt den Store des angemeldeten
Benutzers. Die Teilnehmer müssen keine Benutzernamen weiterreichen. Jeder Store
enthält nur die Zutaten-IDs eines Benutzers. Alle Kühlschränke liegen ausschließlich
im Speicher und sind nach einem Neustart leer. Es gibt weder einen Remote-Kühlschrank
noch ein Remote-Profil. Das bestehende Profil `test` gehört nur zur Milk-Aufgabe;
die SQLite-Konfiguration ist dort ausgeschaltet.

`FridgeStoreTest` bleibt als Teilnehmer-Test vorgegeben. Er führt paralleles Lesen,
Hinzufügen und Entfernen aus. Vor der Synchronisierungsaufgabe kann er mit einem
Nebenläufigkeitsfehler scheitern; nach Ergänzen der Locks muss er bestehen:

```sh
mvn -Dtest=FridgeStoreTest test
```

Auf `master` sind die fachlichen MVC-Methoden absichtlich noch nicht implementiert.
Der Menü- und Login-Aufruf funktionieren bereits; beim Öffnen einer Aufgaben-Seite
wird zunächst die jeweilige `UnsupportedOperationException` ausgelöst.
Die TODOs der bisherigen Milk-Aufgabe bleiben unverändert erhalten.

## Warenkorb-Aufgabe

Unter `/mvc/cart` wird ein Zutaten-Warenkorb pro Browser-Session aufgebaut.
`CartService` ist bereits mit `@SessionScope` versehen und hält die Zutaten-IDs
in einem `LinkedHashSet`, sodass eine Zutat höchstens einmal vorkommt. Ein Login
ist nicht erforderlich. Der Warenkorb wird weder in der Datenbank noch im
benutzerbezogenen Kühlschrank gespeichert und berücksichtigt keine Mengen.

HTML, Menü-Link, Konstruktoren und `CartView` sind vorgegeben. Die Aufgaben-TODOs
in `CartController` und `CartService` stammen aus `dev`:

- Den Warenkorbinhalt und die Cocktail-Auswahl für die Anzeige zusammenstellen.
- Beim Hinzufügen eines Cocktails dessen Rezept laden und alle Zutaten-IDs sammeln.
- Einzelne Zutaten aus dem Warenkorb entfernen.
- Nach Änderungen per Redirect wieder zum Warenkorb wechseln.

Das Leeren des Warenkorbs ist als einfaches Beispiel bereits implementiert – im
Service mit `ingredientIds.clear()`, im Controller mit anschließendem Redirect.
Alle übrigen Aufgabenmethoden sind noch TODOs. Nach der Implementierung lässt
sich die Session-Trennung mit zwei unabhängigen Browser-Sitzungen beobachten.
Es wird kein REST-Warenkorb vorbereitet; JWT und JavaScript-Client sind spätere
Themen.

## REST-Aufgabe mit Swagger

`CocktailWebApplication` starten. Swagger UI liegt unter `/swagger-ui/index.html`
(auch über `/swagger-ui.html` erreichbar), der generierte Vertrag unter
`/v3/api-docs`. Das MVC-Menü enthält einen Link. Eingebunden ist
`springdoc-openapi-starter-webmvc-ui` in Version 3.2.1 für Spring Boot 4.2.

`RecipesRestController` ist vorbereitet. Die acht Aufgabenmethoden unter `/api`
enthalten TODOs und werfen zunächst `UnsupportedOperationException`. Die Aufgabe
besteht darin, die vorhandenen Services aufzurufen und deren DTOs direkt als JSON
zurückzugeben. Keine Templates und keine Redirects verwenden. Swagger dokumentiert
die Zielantworten; die Beispielwerte werden nicht als echte Antwort vorgetäuscht.

| Methode | Pfad | Aufgabe |
| --- | --- | --- |
| GET | `/api/cocktails` | Alle Cocktails als Kurzansicht |
| GET | `/api/cocktails/{id}` | Vollständiges Rezept |
| GET | `/api/cocktails/search?query=Milch` | Suche über Cocktail- und Zutatennamen |
| GET | `/api/ingredients` | Zutatenliste |
| GET | `/api/ingredients/{id}` | Zutat mit ihren Cocktails |
| GET | `/api/fridge/ingredients` | Eigener Zutatenbestand |
| PATCH | `/api/fridge/ingredients/{id}` | `{ "inFridge": true }` bzw. false; Erfolg mit 204 ohne Body |
| GET | `/api/fridge/possible` | Mit dem eigenen Bestand mögliche Cocktails |

Die OpenAPI-Annotationen beschreiben Parameter, Statuscodes und JSON-Strukturen.
`RecipesApiExamples` enthält Dokumentationsbeispiele direkt aus `cocktails.db`:
Milkshake hat ID 24, Milch die Zutaten-ID 29. Das vollständige Beispielrezept
enthält Erdbeeren (27), Vanilleeis (30), Barsirup Erdbeere (13, 2 cl) und Milch
(29, 15 cl). Listen-Auszüge sind in Swagger als solche gekennzeichnet. Die Suche
nach Milch und die Zutat Milch sind mit ihren vollständigen Treffern dokumentiert.

Die JWT-Anmeldung ist vorgegeben. Die fachlichen REST-Aufgaben bleiben TODOs:

1. `POST /api/auth/login` in Swagger mit test/test oder demo/demo ausführen.
2. Den Token unter **Authorize** eintragen; Swagger sendet den Bearer-Header.
3. `GET /api/auth/me` als WhoAmI-Demo aufrufen.
4. Die acht Methoden in `RecipesRestController` implementieren und prüfen.

Für die API werden keine Session-Cookies und keine CSRF-Tokens verwendet. Der
MVC-Login unter `/login` bleibt davon unabhängig. Der vorgegebene JavaScript-Client
liegt unter `/rest/` und verwendet eine eigene Anmeldung unter `/rest/#/login`.
Er hält den Token ausschließlich im Arbeitsspeicher und verwirft ihn beim Neuladen
oder Abmelden. Es gibt keinen REST-Warenkorb und keine Refresh-Tokens.

JWT-Konfiguration und Token-Erzeugung stammen aus SocialDrinks. Das Demo-Secret
ist unverändert übernommen; ein Kommentar nennt die übliche Ablage über
Umgebungsvariablen oder Secret Manager. Tokens sind mit HS256 signiert, eine Stunde
gültig und enthalten Benutzername (`sub`) sowie Rollen (`scope`).

Die Benutzer test/test und demo/demo sind für MVC und JWT vorgegeben. `JwtAuthTest`
prüft ausschließlich die bereitgestellte Authentifizierung und läuft auch vor der
REST-Aufgabe: `mvn -Dtest=JwtAuthTest test`. Die bestehenden Milk- und
FridgeStore-Tests bleiben im Aufgabenstand absichtlich noch nicht erfolgreich.

## Sicherheitsdemonstration: Notizen, Stored XSS und CSRF

Die Demo läuft in der eigenen `org.example.security.SecurityApplication`, getrennt
von `CocktailWebApplication`. `SecurityApplication.main()` direkt in der IDE starten:
Ohne Profil sind CSRF, CSP und HTML-Escaping eingeschaltet; die Demo ist auf
`http://localhost:8080/` erreichbar. Das Profil `security-demo` schaltet gezielt
auf den verwundbaren Unterrichtsstand um. Die Lab-Konfiguration liegt unter
`src/main/resources/security/` und wird nur von dieser Application geladen.
Die Cocktail-Anwendung enthält keine Lab-Controller, Lab-Benutzer oder Lab-Filterkette.
Das Lab benötigt keine Datenbank; deren Auto-Konfiguration ist für diese Application
wegen der gemeinsam verwendeten Maven-Abhängigkeiten ausgeschaltet.
Die Notizen und die Hacker-Nachweise liegen nur im Speicher; ein Neustart leert sie.
Das CSS-Beispiel für die erste HTML-/CSS-Injection kommt aus dem Kursskript und ist
hier bewusst nicht vorgegeben.

### Starten und Links zum Kopieren

Im Projektverzeichnis zunächst bauen:

```sh
mvn package
```

Guter Server auf **8080**, bewusst verwundbarer Übungsstand:

```sh
java -Dloader.main=org.example.security.SecurityApplication -cp target/spring-cocktails-1.0-SNAPSHOT.jar org.springframework.boot.loader.launch.PropertiesLauncher --spring.profiles.active=security-demo
```

Alternativ in der IDE `SecurityApplication` starten und das Programmargument
`--spring.profiles.active=security-demo` setzen. Das normale JAR und
`mvn spring-boot:run` starten weiterhin `BarsApplication`.

Hacker-Server in einem zweiten Terminal auf **1337**:

```sh
java --add-modules jdk.httpserver -cp target/classes org.example.security.hacker.HackerServer
```

Auch direkt über `HackerServer.main()` in der IDE startbar. Er verwendet ausschließlich
JDK-Klassen, startet kein Spring und bindet nur an die Loopback-Adresse. Port und
Ziel-Origin sind bei Bedarf mit `--port=1337 --good-origin=http://localhost:8080`
änderbar. Auf dem guten Server muss dann `demo.security.attacker-origin` dieselbe
Angreifer-Origin enthalten. Beide Server verwenden den Hostnamen **localhost**;
`localhost` und `127.0.0.1` im Browser nicht mischen.

- [Benutzernotiz](http://localhost:8080/good-user.html) — **employee / Secret123**
- [Angreiferseite](http://localhost:1337/evil/csrf-attack.html)
- [Adminübersicht](http://localhost:8080/good-admin.html) — **admin / Secret123**
- [Gesammelte Nachweise](http://localhost:1337/evil/loot)

Die Konten `employee` und `admin` existieren ausschließlich in der SecurityApplication. Der Benutzername
für eine Notiz stammt immer aus Spring Security, niemals aus einem Request-Parameter.
Ein normaler Benutzer darf nur seine eigene Notiz lesen/schreiben; `/notes/all`
ist auch im verwundbaren Modus ausschließlich für `ROLE_ADMIN` zugänglich.

### Ablauf der Vorführung

1. **HTML-/CSS-Injection:** Benutzerseite öffnen, als `employee` anmelden und den
   CSS-/HTML-Text aus dem Kursskript als Notiz speichern. Danach die Adminseite
   öffnen und als `admin` anmelden. Die Übersicht verwendet absichtlich `innerHTML`;
   deshalb wird der gespeicherte HTML-Inhalt interpretiert. Das demonstriert zunächst
   HTML-/CSS-Injection, noch keine JavaScript-Ausführung.
2. **Stored XSS ohne CSRF:** Auf der Angreiferseite wird ein JavaScript-Payload im
   Feld „Vorbereiteter Payload“ als Text angezeigt. Diesen kopieren und als
   `employee` manuell in die Notiz einfügen und speichern. Anschließend als `admin`
   die Übersicht laden. Das eingeschleuste `onerror`-Skript schickt einen sichtbaren
   Demo-Marker an `/evil/collect`. `/evil/loot` zeigt Zeit, Origin und Inhalt.
3. **CSRF → Stored XSS:** Wieder als **employee** anmelden. Dann auf der fremden
   Angreiferseite „Cocktail sichern (Formular)“ klicken. Das fremde Formular sendet
   ohne CSRF-Token eine Notizänderung an den guten Server. Danach als **admin**
   anmelden und die Notizen laden. Der gespeicherte Payload wird ausgeführt und
   erzeugt einen weiteren Marker beim Hacker-Server.
4. **CORS-Variante:** „JSON-Variante (CORS)“ verwendet stattdessen einen
   credentialed JSON-`fetch`. Diese Variante benötigt zusätzlich die absichtlich
   pauschale CORS-Freigabe für jede Origin. Mit gesperrter Origin schlägt
   sie beim Preflight fehl.
5. **Lösungen einzeln zeigen:** Die Schalter unten einzeln aktivieren und den
   jeweiligen Schritt wiederholen. Bei jeder Admin-Aktualisierung kann eine
   verwundbare Notiz erneut ausgeführt werden. Für eine neue Vorführung die Notiz
   als Benutzer durch normalen Text ersetzen; für leere Nachweise den Hacker-Server
   neu starten.

Alle Tabs desselben Browserprofils teilen sich die Anmeldung. Nach der Admin-Anmeldung
ist daher auch die Benutzerseite als Admin authentifiziert. Für den nächsten Angriff
zuerst wieder als `employee` anmelden. Der Name des tatsächlich angemeldeten Benutzers
wird auf den Seiten angezeigt. Das Formular-Ergebnis liegt in einem fremden versteckten
Iframe und ist für die Angreiferseite nicht lesbar. `X-Frame-Options` kann dessen
**Darstellung** verhindern, obwohl die serverseitige Änderung bereits stattgefunden
hat. Die Formularseite behauptet deshalb nur „abgeschickt“, nicht „erfolgreich“.

Es werden **keine Passwörter, JWTs oder Sitzungscookies übertragen**. Der Nachweis
enthält nur den Marker und die URL der Adminseite. `SECURITYSESSION` bleibt HttpOnly.
Trotzdem kann XSS JavaScript innerhalb der Admin-Origin ausführen; HttpOnly beseitigt
also nicht die XSS-Lücke. Der Hacker-Server ignoriert empfangene Cookie- und
Authorization-Header und speichert nur den explizit gesendeten Demo-Body.

### Schutzmaßnahmen zum schrittweisen Aktivieren

Im Profil `security-demo` sind die folgenden Dev-Einstellungen absichtlich unsicher:

```properties
demo.security.csrf-enabled=false
demo.security.csp-enabled=false
demo.security.escape-notes=false
demo.security.allow-any-origin=true
```

Das entspricht dem Unterrichtsfall „Dev-Konfiguration versehentlich ausgerollt“.
Dieses Profil nicht außerhalb der lokalen Übung einsetzen. Die sicheren Defaults in
`DemoSettings` und die Ausnahmen in `DemoSecurityConfiguration` sind im Code kommentiert.
Der vollständige Lösungsstand ist ein zweites Profil, das **nach** `security-demo`
aktiviert wird:

```sh
java -Dloader.main=org.example.security.SecurityApplication -cp target/spring-cocktails-1.0-SNAPSHOT.jar org.springframework.boot.loader.launch.PropertiesLauncher --spring.profiles.active=security-demo,security-demo-secure
```

Einzelne Lösungen lassen sich unabhängig über Kommandozeilenparameter vorführen:

```sh
# Nur CSRF: legitimes Speichern funktioniert, das fremde Formular scheitert.
java -Dloader.main=org.example.security.SecurityApplication -cp target/spring-cocktails-1.0-SNAPSHOT.jar org.springframework.boot.loader.launch.PropertiesLauncher --spring.profiles.active=security-demo --demo.security.csrf-enabled=true

# Nur CSP: die Notiz kann gespeichert werden, Inline-Code und Inline-CSS werden blockiert.
java -Dloader.main=org.example.security.SecurityApplication -cp target/spring-cocktails-1.0-SNAPSHOT.jar org.springframework.boot.loader.launch.PropertiesLauncher --spring.profiles.active=security-demo --demo.security.csp-enabled=true

# Nur HTML-Escaping: der Admin sieht den Payload als Text.
java -Dloader.main=org.example.security.SecurityApplication -cp target/spring-cocktails-1.0-SNAPSHOT.jar org.springframework.boot.loader.launch.PropertiesLauncher --spring.profiles.active=security-demo --demo.security.escape-notes=true

# Nur CORS-Korrektur: die fremde JSON-Variante wird nicht zugelassen.
java -Dloader.main=org.example.security.SecurityApplication -cp target/spring-cocktails-1.0-SNAPSHOT.jar org.springframework.boot.loader.launch.PropertiesLauncher --spring.profiles.active=security-demo --demo.security.allow-any-origin=false
```

#### CSP

Der aktuelle Spring-Security-Stil ist bereits in `DemoSecurityConfiguration` eingebaut:

```java
http.headers(headers -> headers.contentSecurityPolicy(csp -> csp.policyDirectives(
        "default-src 'self'; script-src 'self'; style-src 'self'; img-src 'self'; " +
        "connect-src 'self'; object-src 'none'; base-uri 'self'; " +
        "frame-ancestors 'self'; form-action 'self'")));
```

- `default-src 'self'`: Standard für die jeweiligen Ressourcen-Direktiven.
- `script-src 'self'`: eigene Skriptdateien, keine Inline-Skripte oder `onerror`-Handler.
- `style-src 'self'`: eigene CSS-Dateien, keine eingeschleusten `<style>`-Blöcke.
- `img-src 'self'`: Bilder nur von der eigenen Origin; ein wirklich benötigtes CDN
  müsste hier explizit ergänzt werden, z. B. `img-src 'self' https://cdn.example.com`.
- `connect-src 'self'`: auch `fetch` zum Hacker-Server wird blockiert.
- `object-src 'none'`: keine Plugin-/Object-Inhalte.
- `base-uri`, `frame-ancestors` und `form-action` begrenzen weitere Einbettungs- und
  Navigationsmöglichkeiten. CSP ersetzt das korrekte Escaping nicht.

Die Demo-Seiten verwenden externe eigene JS-/CSS-Dateien und funktionieren daher auch
mit der strengen CSP. Die Policy wird nicht ungeprüft auf AngularJS oder Swagger UI
übertragen. Dokumentation: <https://developer.mozilla.org/de/docs/Web/HTTP/Guides/CSP>.

#### HTML-Ausgabe und Escaping

`DemoNoteController.forHtml` enthält die Lösung:

```java
return HtmlUtils.htmlEscape(text);
```

Sie wird mit `escape-notes=true` für die Admin-HTML-Ausgabe aktiviert. Der gespeicherte
Originaltext und der persönliche Editor bleiben unverändert; beim Bearbeiten ist
`textarea.value` bereits ein Textkontext. Nicht pauschal sämtliche JSON-Antworten
escapen und nicht mehrfach escapen. Alternativ lässt sich der unsichere
`innerHTML`-Aufbau in `good.js` durch DOM-Knoten mit `textContent` ersetzen.
Thymeleaf verwendet für sichere Textausgabe weiterhin `th:text`, nicht `th:utext`.

#### CSRF und Cookies

Unsere Authentifizierung ist **cookie-basiert**, deshalb im Lösungsstand:

```java
http.csrf(csrf -> csrf.spa());
```

Spring Security richtet damit die SPA-Integration ein. JavaScript lädt zuerst
`GET /security-demo/session`, liest **XSRF-TOKEN** und sendet **X-XSRF-TOKEN** bei Änderungen.
Das ist der Headername des `CookieCsrfTokenRepository`; `X-CSRF-TOKEN` ist ein anderer
üblicher Name, aber nicht dessen Standard. Nach Login/Logout wird der Token erneuert.
Thymeleaf-Formulare verwenden weiterhin `_csrf`. Die Notizseiten unterstützen dies
bereits, damit sie im Lösungsstand weiter funktionieren.

```properties
server.servlet.session.cookie.http-only=true
server.servlet.session.cookie.same-site=Lax
```

`SameSite=Lax` oder `Strict` sind zusätzliche Schutzmaßnahmen. Sie blockieren diese
Vorführung zwischen **localhost:1337 und localhost:8080 nicht**, weil unterschiedliche
Ports unterschiedliche Origins, aber hier dieselbe Site ergeben. Cookies sind zudem
nicht nach Port getrennt. Mit anderen Hostnamen sähe der Versuch anders aus. Unter
HTTPS wäre zusätzlich `server.servlet.session.cookie.secure=true` sinnvoll.
Nur für tatsächlich stateless APIs, die ausschließlich explizit gesetzte Bearer-Header
akzeptieren und keine Cookies als Authentifizierung verwenden, ist das Abschalten
von CSRF eine andere mögliche Konfiguration. Ein Cookie-JWT benötigt ebenfalls Schutz.

#### CORS

Die Demo verwendet die Konfiguration im Security-Layer. Der Unterrichtsfall lautet:
Jemand hat zur Entwicklung jede Origin zugelassen; diese Einstellung ist versehentlich
auf dem Live-Server gelandet. Die unsichere Variante kombiniert
`setAllowedOriginPatterns(List.of("*"))` mit `allowCredentials(true)`. Sie vertraut
also jeder Origin, nicht speziell dem Hacker-Server. Spring spiegelt die anfragende
Origin im Antwort-Header; die Wildcard steht nicht im `Access-Control-Allow-Origin`-
Header. Die Lösung entfernt die pauschale Freigabe. Bei einem echten getrennten Frontend würde man ausschließlich dessen
vertrauenswürdige Origin erlauben, etwa:

```java
config.setAllowedOrigins(List.of("https://app.example.com"));
config.setAllowedMethods(List.of("GET", "POST"));
config.setAllowedHeaders(List.of("Content-Type", "X-XSRF-TOKEN"));
config.setAllowCredentials(true);
```

CORS ist kein Ersatz für CSRF. Ein normales fremdes HTML-Formular braucht keinen
JSON-Preflight. Der konfigurierte Spring-CORS-Filter kann dessen expliziten fremden
Origin-Header ebenfalls ablehnen, dennoch bleibt die Tokenprüfung die eigenständige
CSRF-Lösung.

### Prüfung der Demonstration

Das Security Lab wird als lokale, harmlose Schulungsdemonstration im Browser
untersucht. Zusätzliche Infrastruktur- und Regressionstests gehören nicht zu den
Teilnehmeraufgaben und sind aus dem Testverzeichnis entfernt.
