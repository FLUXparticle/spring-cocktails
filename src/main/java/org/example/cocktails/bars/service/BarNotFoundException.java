package org.example.cocktails.bars.service;

public class BarNotFoundException extends RuntimeException {
    public BarNotFoundException(Long id) {
        super("Bar " + id + " existiert nicht mehr.");
    }
}
