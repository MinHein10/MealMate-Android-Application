package com.example.mealmate.models;

import java.util.List;

public class Grocery {

    private String id;
    private String categoryName;
    private List<String> ingredients;

    private boolean check;

    public Grocery(String id, String categoryName, List<String> ingredients, boolean check) {
        this.id = id;
        this.categoryName = categoryName;
        this.ingredients = ingredients;
        this.check = check;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
