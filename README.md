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
vorbereitet. Ein JavaScript-Client und das Security Lab gehören nicht zu diesem Stand.

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

JWT ist weiterhin eine spätere Aufgabe. Vorläufig verwendet der Kühlschrank den
bestehenden Session-Login. Die Zugriffsregeln und CSRF-Konfiguration sind vorgegeben:

1. In einem zweiten Tab über `/login` mit test/test oder demo/demo anmelden.
2. Zu Swagger zurückkehren; der Browser sendet das Session-Cookie automatisch.
3. Den bereits implementierten Hilfsendpunkt `GET /api/session` ausführen. Er lädt
   den CSRF-Cookie und zeigt den Anmeldestatus an.
4. Bei PATCH sendet Swagger den CSRF-Cookie automatisch als Header `X-XSRF-TOKEN`.
   Nach Login/Logout Schritt 3 wiederholen.

Unangemeldete Kühlschrank-Aufrufe liefern 401 statt einer HTML-Weiterleitung.
Der MVC-Login und dessen CSRF-Schutz bleiben erhalten. Es gibt keine JWT-Ausstellung
und keinen REST-Warenkorb. Die vollständigen fachlichen Services stehen auf
`solution`; nach Übernahme der Vorbereitung kann der REST-Controller darauf aufbauen.
