package com.example.mealmate.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_recipes")
public class FavoriteRecipe {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String recipeID;

    public FavoriteRecipe(String recipeID) {
        this.recipeID = recipeID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }
}
