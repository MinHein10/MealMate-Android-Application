package com.example.mealmate.models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngredientCategorizer {
    private static final Map<String, List<String>> CATEGORY_KEYWORDS = new HashMap<>();

    static {
        CATEGORY_KEYWORDS.put("Meat & Seafood", Arrays.asList("pancetta", "chicken", "fish", "shrimp", "beef", "pork"));
        CATEGORY_KEYWORDS.put("Produce", Arrays.asList("garlic", "onion", "tomato", "sweet potatoes", "broccoli", "carrot", "avocado", "cilantro"));
        CATEGORY_KEYWORDS.put("Dairy", Arrays.asList("cheese", "butter", "buttermilk", "milk", "Parmesan"));
        CATEGORY_KEYWORDS.put("Baking & Spices", Arrays.asList("salt", "pepper", "sugar", "flour", "baking powder", "baking soda", "cinnamon", "turmeric", "chili powder", "garam masala"));
        CATEGORY_KEYWORDS.put("Pasta & Grains", Arrays.asList("spaghetti", "quinoa", "rice"));
        CATEGORY_KEYWORDS.put("Oils & Condiments", Arrays.asList("olive oil", "tahini", "coconut milk", "maple syrup", "lemon juice"));
        CATEGORY_KEYWORDS.put("Other", Arrays.asList("eggs", "buttermilk", "water"));
    }

    public static String categorizeIngredient(String ingredient) {
        for (Map.Entry<String, List<String>> entry : CATEGORY_KEYWORDS.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (ingredient.toLowerCase().contains(keyword.toLowerCase())) {
                    return entry.getKey(); // Return the category name
                }
            }
        }
        return "Other"; // Default category if no match found
    }
}
