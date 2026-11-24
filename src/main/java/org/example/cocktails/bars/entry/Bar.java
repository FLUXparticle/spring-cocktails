package org.example.cocktails.bars.entry;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Bar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name ist erforderlich.")
    @Size(max = 20, message = "Maximal 20 Zeichen")
    private String name;
    @NotBlank(message = "E-Mail ist erforderlich.")
    @Email(message = "Bitte eine gültige E-Mail-Adresse eingeben.")
    private String email;
    @Size(max = 20, message = "Maximal 20 Zeichen")
    private String address;

    public Bar() {
    }

    public Bar(String name, String email, String address) {
        this.name = name;
        this.email = email;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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



    public String snapshot() {
        return "Name: " + name + ", E-Mail: " + email + ", Adresse: " + address;
    }
}
