package org.example.security.hacker;

import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * A separate, dependency-free, loopback-only classroom attacker. No Spring bootstrap.
 */
public final class HackerServer implements AutoCloseable {
    private final HttpServer server;
    private final ExecutorService workers = Executors.newFixedThreadPool(4);
    private final List<Capture> captures = new CopyOnWriteArrayList<>();
    private final String goodOrigin;

    private HackerServer(int port, String goodOrigin) throws IOException {
        validateOrigin(goodOrigin);
        this.goodOrigin = goodOrigin;
        server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), port), 0);
        server.setExecutor(workers);
        server.createContext("/", this::handle);
        server.start();
    }

    public static HackerServer start(int port, String goodOrigin) throws IOException {
        return new HackerServer(port, goodOrigin);
    }

    public static void main(String[] args) throws Exception {
        int port = 1337;
        String goodOrigin = "http://localhost:8080";
        for (String arg : args) {
            if (arg.startsWith("--port=")) port = Integer.parseInt(arg.substring(7));
            else if (arg.startsWith("--good-origin=")) goodOrigin = arg.substring(14);
            else throw new IllegalArgumentException("Argumente: --port=1337 --good-origin=http://localhost:8080");
        }
        if (port < 1 || port > 65535) throw new IllegalArgumentException("Ungültiger Port.");
        var app = start(port, goodOrigin);
        Runtime.getRuntime().addShutdownHook(new Thread(app::close));
        System.out.println("Angreiferseite: http://localhost:" + app.port() + "/evil/csrf-attack.html");
        System.out.println("Nachweise: http://localhost:" + app.port() + "/evil/loot");
        if (port >= 6665 && port <= 6669)
            System.out.println("Browser sperren diesen IRC-Port häufig. Alternative: --port=9090 und demo.security.attacker-origin entsprechend setzen.");
    }

    private static void respond(HttpExchange exchange, int code, String type, String body) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", type + "; charset=UTF-8");
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        exchange.getResponseHeaders().set("X-Content-Type-Options", "nosniff");
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(code, code == 204 ? -1 : bytes.length);
        if (code != 204) exchange.getResponseBody().write(bytes);
    }

    private static String quote(String value) {
        StringBuilder out = new StringBuilder("\"");
        for (char c : value.toCharArray()) {
            switch (c) {
                case '"' -> out.append("\\\"");
                case '\\' -> out.append("\\\\");
                case '\n' -> out.append("\\n");
                case '\r' -> out.append("\\r");
                case '\t' -> out.append("\\t");
                default -> {
                    if (c < 32 || c == '<' || c == '>') out.append(String.format("\\u%04x", (int) c));
                    else out.append(c);
                }
            }
        }
        return out.append('"').toString();
    }

    private static void validateOrigin(String value) {
        URI origin = URI.create(value);
        if (!"http".equals(origin.getScheme()) || !("localhost".equals(origin.getHost()) || "127.0.0.1".equals(origin.getHost()))
                || origin.getPort() < 1 || origin.getPort() > 65535 || origin.getRawUserInfo() != null
                || (origin.getRawPath() != null && !origin.getRawPath().isEmpty()) || origin.getRawQuery() != null || origin.getFragment() != null)
            throw new IllegalArgumentException("Die Ziel-Origin muss eine lokale HTTP-Origin mit Port sein.");
    }

    public int port() {
        return server.getAddress().getPort();
    }

    private void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            if (path.equals("/evil/collect")) {
                collect(exchange);
                return;
            }
            if (!method.equals("GET")) {
                respond(exchange, 405, "text/plain", "Nur GET erlaubt.");
                return;
            }
            switch (path) {
                case "/", "/evil/csrf-attack.html" -> resource(exchange, "csrf-attack.html", "text/html");
                case "/evil/attack.js" -> resource(exchange, "attack.js", "text/javascript");
                case "/evil/evil.css" -> resource(exchange, "evil.css", "text/css");
                case "/evil/config" ->
                        respond(exchange, 200, "application/json", "{\"goodOrigin\":" + quote(goodOrigin) + ",\"attackerOrigin\":" + quote("http://localhost:" + port()) + "}");
                case "/evil/status" ->
                        respond(exchange, 200, "application/json", "{\"captures\":" + captures.size() + "}");
                case "/evil/loot" -> {
                    String body = captures.stream().map(c -> "{\"timestamp\":" + quote(c.timestamp()) + ",\"origin\":" + quote(c.origin()) + ",\"body\":" + quote(c.body()) + "}")
                            .collect(java.util.stream.Collectors.joining(",", "[", "]"));
                    respond(exchange, 200, "application/json", body);
                }
                default -> respond(exchange, 404, "text/plain", "Nicht gefunden.");
            }
        } finally {
            exchange.close();
        }
    }

    private void collect(HttpExchange exchange) throws IOException {
        String origin = exchange.getRequestHeaders().getFirst("Origin");
        if (origin != null && !origin.equals(goodOrigin)) {
            respond(exchange, 403, "text/plain", "Unerwartete Origin.");
            return;
        }
        if (origin != null) exchange.getResponseHeaders().set("Access-Control-Allow-Origin", goodOrigin);
        if (exchange.getRequestMethod().equals("OPTIONS")) {
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
            respond(exchange, 204, "text/plain", "");
            return;
        }
        if (!exchange.getRequestMethod().equals("POST")) {
            respond(exchange, 405, "text/plain", "Nur POST erlaubt.");
            return;
        }
        byte[] bytes = exchange.getRequestBody().readNBytes(8193);
        if (bytes.length > 8192 || captures.size() >= 100) {
            respond(exchange, 413, "text/plain", "Demo-Limit erreicht. Server für neue Vorführung neu starten.");
            return;
        }
        String body = new String(bytes, StandardCharsets.UTF_8);
        captures.add(new Capture(Instant.now().toString(), origin == null ? "" : origin, body));
        System.out.println("Demo-Nachweis empfangen (" + bytes.length + " Bytes), insgesamt " + captures.size());
        respond(exchange, 200, "application/json", "{\"status\":\"recorded\",\"entries\":" + captures.size() + "}");
    }

    private void resource(HttpExchange exchange, String name, String contentType) throws IOException {
        try (InputStream input = HackerServer.class.getResourceAsStream("/hacker/" + name)) {
            if (input == null) {
                respond(exchange, 404, "text/plain", "Ressource fehlt. Zuerst mvn compile ausführen.");
                return;
            }
            respond(exchange, 200, contentType, new String(input.readAllBytes(), StandardCharsets.UTF_8));
        }
    }

    @Override
    public void close() {
        server.stop(0);
        workers.shutdownNow();
    }

    private record Capture(String timestamp, String origin, String body) {
    }
}
