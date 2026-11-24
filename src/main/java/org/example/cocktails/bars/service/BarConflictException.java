package org.example.cocktails.bars.service;

public class BarConflictException extends RuntimeException {
    public BarConflictException() {
        super("Der Bar-Bestand wurde zwischenzeitlich geändert.");
    }

    public BarConflictException(Long id) {
        super("Bar " + id + " wurde zwischenzeitlich geändert.");
    }
}
