package org.example.cocktails.recipes.entry;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

@Entity
public class Instruction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonAlias("amount")
    private Integer amountCL;

    @ManyToOne
    private Ingredient ingredient;

    protected Instruction() {
        // Required by JAXB
    }

    public Integer getAmountCL() {
        return amountCL;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (amountCL != null) {
            sb.append(amountCL);
            sb.append("cl ");
        }
        sb.append(ingredient.getName());

        return sb.toString();
    }

}
