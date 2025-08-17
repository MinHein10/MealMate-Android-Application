package com.example.mealmate.models;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
public class Recipe implements Serializable {

    private String id; // Unique ID for the recipe
    private String name; // Name of the recipe
    private String description; // Short description of the recipe
    private String imageUrl; // URL or local path for the recipe image
    private List<String> tags; // Tags for categorization (e.g., "Vegan", "Dessert")
    private List<String> ingredients; // List of ingredients
    private List<String> instructions; // List of preparation steps
    private int preparationTime; // Preparation time in minutes
    private int cookingTime; // Cooking time in minutes
    private int servings; // Number of servings

    // Constructor
    public Recipe(String id, String name, String description, String imageUrl, List<String> tags,
                  List<String> ingredients, List<String> instructions, int preparationTime,
                  int cookingTime, int servings) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.tags = tags;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.preparationTime = preparationTime;
        this.cookingTime = cookingTime;
        this.servings = servings;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public List<String> getIngredients() { return ingredients; }
    public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }

    public List<String> getInstructions() { return instructions; }
    public void setInstructions(List<String> instructions) { this.instructions = instructions; }

    public int getPreparationTime() { return preparationTime; }
    public void setPreparationTime(int preparationTime) { this.preparationTime = preparationTime; }

    public int getCookingTime() { return cookingTime; }
    public void setCookingTime(int cookingTime) { this.cookingTime = cookingTime; }

    public int getServings() { return servings; }
    public void setServings(int servings) { this.servings = servings; }
}


//Dummy Data
//Recipe recipe1 = new Recipe(
//        "1",
//        "Grilled Chicken",
//        "A simple and delicious grilled chicken recipe.",
//        "recipe",
//        Arrays.asList("Dinner", "Healthy", "Protein-rich"),
//        Arrays.asList(
//                "All-purpose Flour 2 1/4 cups",
//                "All-purpose Flour 2 1/4 cups",
//                "All-purpose Flour 2 1/4 cups"
//        ),
//        Arrays.asList(
//                "Marinate chicken with olive oil, garlic, lemon juice, salt, and pepper.",
//                "Preheat grill to medium-high heat.",
//                "Grill chicken for 5-7 minutes per side until cooked through."
//        ),
//        10, // Preparation time
//        15, // Cooking time
//        2   // Servings
//);
//
//Recipe recipe2 = new Recipe(
//        "2",
//        "Chocolate Chip Cookies",
//        "Classic chocolate chip cookies that are soft and chewy.",
//        "",
//        Arrays.asList("Dessert", "Sweet", "Baking"),
//        Arrays.asList(
//                "All-purpose Flour 2 1/4 cups",
//                "All-purpose Flour 2 1/4 cups",
//                "All-purpose Flour 2 1/4 cups"
//        ),
//        Arrays.asList(
//                "Preheat oven to 375°F (190°C).",
//                "In a bowl, mix flour, baking soda, and salt.",
//                "Cream together butter, sugar, and brown sugar until smooth.",
//                "Beat in eggs and vanilla.",
//                "Gradually add dry ingredients to wet ingredients.",
//                "Stir in chocolate chips.",
//                "Drop spoonfuls of dough onto a baking sheet and bake for 10-12 minutes."
//        ),
//        20, // Preparation time
//        12, // Cooking time
//        24  // Servings
//);
//
//Recipe recipe3 = new Recipe(
//        "3",
//        "Vegetable Stir-Fry",
//        "A quick and healthy vegetable stir-fry.",
//        "",
//        Arrays.asList("Lunch", "Vegan", "Quick"),
//        Arrays.asList(
//                "Broccoli 1 cup",
//                "Broccoli 1 cup",
//                "Broccoli 1 cup"
//        ),
//        Arrays.asList(
//                "Heat sesame oil in a wok over medium heat.",
//                "Add garlic and ginger, and sauté for 1 minute.",
//                "Add broccoli, carrots, and bell peppers. Stir-fry for 5-7 minutes.",
//                "Drizzle with soy sauce and toss to coat.",
//                "Serve hot with rice or noodles."
//        ),
//        10, // Preparation time
//        10, // Cooking time
//        2   // Servings
//);
// Add recipes to favoriteRecipes list
//        favoriteRecipes.add(recipe1);
//        favoriteRecipes.add(recipe2);
//        favoriteRecipes.add(recipe3);

