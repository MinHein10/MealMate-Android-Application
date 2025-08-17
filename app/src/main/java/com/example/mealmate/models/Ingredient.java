package com.example.mealmate.models;

import java.util.List;

public class Ingredient {

    private String id;

    private List<String> name; // Name of the ingredient
    private String quantity; // Quantity (e.g., "2 cups", "1 tbsp")

    public Ingredient(String id, List<String> name, String quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
