package org.example.cocktails.bars.controller;

import jakarta.validation.constraints.*;

import java.util.*;

public class BulkForm {
    private Map<Long, Long> versions = new LinkedHashMap<>();
    @Size(max = 20, message = "Maximal 20 Zeichen")
    private String address;

    public Map<Long, Long> getVersions() {
        return versions;
    }

    public void setVersions(Map<Long, Long> versions) {
        this.versions = versions;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
