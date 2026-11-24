package org.example.cocktails.bars.controller;

import jakarta.validation.constraints.*;
import org.example.cocktails.bars.entry.*;
import org.example.cocktails.bars.service.*;

/**
 * Only the fields that may be submitted by an editor are bound.
 */
public class BarForm {
    @NotBlank(message = "Name ist erforderlich.")
    @Size(max = 20, message = "Maximal 20 Zeichen")
    private String name;
    @NotBlank(message = "E-Mail ist erforderlich.")
    @Email(message = "Bitte eine gültige E-Mail-Adresse eingeben.")
    private String email;
    @Size(max = 20, message = "Maximal 20 Zeichen")
    private String address;
    private Long version;

    public BarForm() {
    }

    public BarForm(Bar bar) {
        name = bar.getName();
        email = bar.getEmail();
        address = bar.getAddress();
        version = BarVersions.get(bar);
    }

    public Bar toBar(Long id) {
        Bar bar = new Bar(name, email, address);
        bar.setId(id);
        BarVersions.set(bar, version);
        return bar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
