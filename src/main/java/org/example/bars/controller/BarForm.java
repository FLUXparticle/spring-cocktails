package org.example.bars.controller;

import org.example.bars.entry.*;
import org.example.bars.service.*;

/**
 * Only the fields that may be submitted by an editor are bound.
 */
public class BarForm {
    private String name;
    private String email;
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
