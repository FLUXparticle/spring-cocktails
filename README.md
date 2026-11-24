# Spring – Tag 1

Der erste Kurstag behandelt HTML-Formulare mit Spring MVC, Session-Zustand,
Persistenz mit JPA und H2 sowie Paging, konkurrierende Bearbeitung und Transaktionen.
Der Code verwendet Java 17 oder neuer und Spring Boot 4.2.0.

## Starten

```sh
mvn spring-boot:run
# Alternativ:
mvn package
java -jar target/spring-cocktails-1.0-SNAPSHOT.jar
```

Startseite: <http://localhost:8080/>. Die Bar-Tabelle liegt unter `/bars`.
Die HTML-Seiten sind bewusst schlicht, ohne CSS und Ajax. Anmeldung, externe APIs,
SQLite, Cocktail-MVC, REST-Client und Security Lab gehören nicht zu diesem Stand.

## Branches und Aufgaben

`master` enthält genau einen `Initial Commit`: den vorbereiteten Aufgabenstand.
Die Bar-Grundfunktionen sind vorgegeben. Die Einstiegsaufgaben werden anhand der
Vorlagen im Skript ergänzt. `solution` baut auf demselben Commit auf und stellt
nacheinander diese sieben Lösungen her:

| Schritt | Aufgabe | Ergebnis |
| --- | --- | --- |
| 1 | Greeting | GET `/lessons/hello`, Formular-POST an `/lessons/hello/name`, „Hallo, Name!“ |
| 2 | Repeat | Eigene Demos für Repeat, Error, Echo und Swap |
| 3 | Namensliste | Namen pro Browser-Session sammeln, validieren und nach POST umleiten |
| 4 | Namen in Datenbank speichern | Session-Liste durch Entity und Spring-Data-Repository in H2 ersetzen |
| 5 | Paging | Listenabfragen auf Repository-Methoden mit `Pageable` und `Page` umstellen |
| 6 | Doppelte Bearbeitung erkennen | Versionsfeld mit `@Version`, Getter und Setter ergänzen |
| 7 | Transaktionen | Den gesamten Batch mit `@Transactional("barsTransactionManager")` absichern und den Rollback testen |

### Textfelder

Bei Repeat haben „Oben“ und „Unten“ eigene Formulare mit den POST-Zielen `/top`
und `/bottom`. Zunächst verarbeitet der untere Empfänger nichts: Beide Felder sind
anschließend leer, weil das Model für jeden Request neu aufgebaut wird. Error setzt
unten „Fehler!“, kennt aber den nicht gesendeten oberen Wert nicht. Echo kopiert
auch unten nach oben. Bei Swap werden die beiden Formulare zusammengefasst;
beide Buttons bleiben und senden an `/lessons/swap/both`.

### Namensliste

Die erste Lösung hält eine Liste in einem `@SessionScope`-Bean. Zwei unabhängige
Browser-Sitzungen haben unterschiedliche Listen. Danach ersetzt die JPA-Lösung
unter derselben URL `/lessons/names` diese Liste durch gemeinsam gespeicherte Namen.
H2 läuft weiterhin im Speicher: Die Daten überleben Requests und Sessions, aber
keinen Neustart. Nach erfolgreichem Speichern verhindert POST/Redirect/GET ein
versehentliches erneutes Hinzufügen beim Neuladen.

## Vorgegebene Bar-Umgebung

Beim Start entstehen 26 Bars von „Bar A“ bis „Bar Z“, mit E-Mail-Adressen von
`bar01@example.com` bis `bar26@example.com`. Anlegen, Bearbeiten, Löschen, Suche,
Sortierung, Historie und die gemeinsame Adressänderung sind vorgegeben.
Name und Adresse dürfen höchstens 20 Zeichen enthalten; Name und gültige E-Mail
sind Pflichtfelder. Sortiert wird per GET-Link über Name, E-Mail oder Adresse.

Vor der Paging-Lösung werden alle passenden Bars angezeigt. Danach sind es zehn
pro Seite; „Zurück“ und „Weiter“ bleiben an den Seitengrenzen als Text ohne Link
stehen. Die gemeinsame Adressänderung betrifft stets alle Bars, unabhängig von
Suche oder aktueller Seite.

### Doppelte Bearbeitung vorführen

Eine Bar in zwei Tabs öffnen. Im ersten Tab ändern und speichern, danach im zweiten
Tab veraltete Daten speichern. Ohne Versionsfeld überschreibt die zweite Änderung
die erste unbemerkt. Nach Ergänzen von `@Version` wird ein Konflikt erkannt. Die
vorgegebene Umgebung (`BarVersions`, Formulare und Konfliktseite) funktioniert mit
und ohne Versionsfeld und gehört nicht zur eigentlichen Aufgabe.

### Fehlende Transaktion vorführen

Zuerst die Liste laden, dann z. B. Bar Z in einem zweiten Tab ändern. Anschließend
im ersten Tab die Adresse aller Bars ändern. Nach der Versionsaufgabe entsteht ein
Konflikt; ohne Batch-Transaktion bleiben vorherige Änderungen samt Historie bereits
gespeichert. Mit `@Transactional("barsTransactionManager")` an `bulkUpdate` wird
der gesamte Batch zurückgerollt. Die unabhängig gespeicherte Änderung an Bar Z
bleibt bestehen. Der zugehörige Teilnehmer-Test liegt in der letzten Lösung.

## Vorgegebene Konfiguration

`CocktailWebApplication` liegt im Basispaket `org.example.cocktails` und startet
alle Komponenten. Die Pakete `lessons` und `bars` gliedern die Schulungsphasen;
Controller, Services, Repositories und Datenklassen (`entry`) liegen darunter.

`PersistenceConfiguration` stellt die H2-Datenquelle, die JPA-Persistence-Unit
`bars` und `barsTransactionManager` bereit. `BarConfiguration` ordnet die
Bar-Repositories dieser Datenbank zu. Mit der Datenbank-Aufgabe kommt die analoge
`LessonConfiguration` für die Namensliste hinzu. Diese Konfigurationen sind
vorgegeben und werden im Skript erklärt. Es gibt genau eine Datenbank, ausschließlich
im Speicher. Mit `--bars.seed=false` lassen sich die Beispielbars abschalten.

Die vorhandene E-Mail-Simulation protokolliert nach erfolgreichen Änderungen nur
eine Nachricht; sie versendet keine echten E-Mails. `--bars.email.delay-ms=0`
überspringt ihre Wartezeit. Sie ist Teil der vorgegebenen Bar-Umgebung.

## Prüfen

```sh
mvn verify
# Auf solution nach der Transaktionsaufgabe:
mvn -Dtest=BarTransactionTest test
```

Im Initialstand gibt es noch keine Teilnehmer-Tests. Der Lösungsbranch ergänzt den
Test, der den vollständigen Rollback des Batches einschließlich Historie prüft.
