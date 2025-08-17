package com.example.mealmate.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GroceryListCategorizer {
    public List<Grocery> categorizeIngredients(List<String> ingredients) {
        Map<String, List<String>> categorizedIngredients = new HashMap<>();

        // Categorize each ingredient
        for (String ingredient : ingredients) {
            String category = IngredientCategorizer.categorizeIngredient(ingredient);
            categorizedIngredients
                    .computeIfAbsent(category, k -> new ArrayList<>())
                    .add(ingredient);
        }

        // Convert the categorized map to a list of Grocery objects
        List<Grocery> groceryList = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : categorizedIngredients.entrySet()) {
            groceryList.add(new Grocery(UUID.randomUUID().toString(), entry.getKey(), entry.getValue(),false));
        }

        return groceryList;
    }

}
