package com.example.mealmate;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mealmate.databinding.ActivityAddNewRecipeBinding;
import com.example.mealmate.models.Ingredient;
import com.example.mealmate.models.Recipe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AddNewRecipeActivity extends AppCompatActivity {

    ActivityAddNewRecipeBinding binding;
    boolean isRecipeEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnSaveRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecipeEdit){
                    Recipe updateRecipe = (Recipe) getIntent().getSerializableExtra("recipe");
                    updateRecipeInDatabase(updateRecipe);
                }else{
                    getData();
                }
            }
        });


        binding.btnBackRecipe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddNewRecipeActivity.this,MainActivity2.class));
            }
        });


//        Render Existing Recipe Data Section for updating
        isRecipeEdit = getIntent().getBooleanExtra("isRecipeEdit",false);
        if(isRecipeEdit){
            Recipe recipeupdatedata = (Recipe) getIntent().getSerializableExtra("recipe");
             binding.etRecipeName.setText(recipeupdatedata.getName());
             binding.etImageUrl.setText(recipeupdatedata.getImageUrl());
            binding.etRecipeDescription.setText(recipeupdatedata.getDescription());
            binding.etServings.setText(String.valueOf(recipeupdatedata.getServings()));
            binding.etPrepTime.setText(String.valueOf(recipeupdatedata.getPreparationTime()));
            binding.etCookingTime.setText(String.valueOf(recipeupdatedata.getCookingTime()));
//            // Convert list to comma-separated string
            String tags = String.join(", ", recipeupdatedata.getTags());
            String ingredients = String.join(", ", recipeupdatedata.getIngredients());
            String instructions = String.join(", ", recipeupdatedata.getInstructions());

            binding.etTags.setText(tags);
            binding.etIngredients.setText(ingredients);
            binding.etInstructions.setText(instructions);
            binding.btnSaveRecipe.setText("Update Recipe");
            binding.tvSectionTitle.setText("Update Your Recipe");
        }

    }

    private void getData(){

        // Retrieve data from the input fields
        String recipeName = binding.etRecipeName.getText().toString().trim();
        String imageUrl = binding.etImageUrl.getText().toString().trim();
        String description = binding.etRecipeDescription.getText().toString().trim();
        String tags = binding.etTags.getText().toString().trim();
        String servingsStr = binding.etServings.getText().toString().trim();
        String prepTimeStr = binding.etPrepTime.getText().toString().trim();
        String cookingTimeStr = binding.etCookingTime.getText().toString().trim();
        String ingredients = binding.etIngredients.getText().toString().trim();
        String instructions = binding.etInstructions.getText().toString().trim();

        // Validate inputs
        if (recipeName.isEmpty()) {
            binding.etRecipeName.setError("Recipe name is required");
            binding.etRecipeName.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            binding.etRecipeDescription.setError("Description is required");
            binding.etRecipeDescription.requestFocus();
            return;
        }

        if (ingredients.isEmpty()) {
            binding.etIngredients.setError("At least one ingredient is required");
            binding.etIngredients.requestFocus();
            return;
        }

        if (instructions.isEmpty()) {
            binding.etInstructions.setError("At least one instruction is required");
            binding.etInstructions.requestFocus();
            return;
        }

        if (servingsStr.isEmpty() || !servingsStr.matches("\\d+")) {
            binding.etServings.setError("Valid servings number is required");
            binding.etServings.requestFocus();
            return;
        }

        if (prepTimeStr.isEmpty() || !prepTimeStr.matches("\\d+")) {
            binding.etPrepTime.setError("Valid preparation time is required");
            binding.etPrepTime.requestFocus();
            return;
        }

        if (cookingTimeStr.isEmpty() || !cookingTimeStr.matches("\\d+")) {
            binding.etCookingTime.setError("Valid cooking time is required");
            binding.etCookingTime.requestFocus();
            return;
        }

        // Convert numeric fields
        int servings = Integer.parseInt(servingsStr);
        int prepTime = Integer.parseInt(prepTimeStr);
        int cookingTime = Integer.parseInt(cookingTimeStr);

        // Convert comma-separated fields into lists
        List<String> tagList = tags.isEmpty() ? new ArrayList<>() : Arrays.asList(tags.split("\\s*,\\s*"));
        List<String> ingredientList = Arrays.asList(ingredients.split("\\s*,\\s*"));
        List<String> instructionList = Arrays.asList(instructions.split("\\s*,\\s*"));

        // Create a Recipe object
        Recipe newRecipe = new Recipe(
                UUID.randomUUID().toString(), // Uniq0ue ID
                recipeName,
                description,
                imageUrl,
                tagList,
                ingredientList,
                instructionList,
                prepTime,
                cookingTime,
                servings
        );

        Ingredient newIngredient = new Ingredient(
                UUID.randomUUID().toString(),
                ingredientList,
                null
                );

        // Save the recipe to the database
        saveToDatabase(newRecipe);
        saveIngredientToDatabase(newIngredient);
        startActivity(new Intent(this,MainActivity2.class));
        finish();
    }

    private void saveIngredientToDatabase(Ingredient newIngredient) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Ingredient")
                .add(newIngredient)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error" + e);
                    }
                });
    }

    /**
     * Saves the given recipe to the database.
     * @param recipe The Recipe object to save.
     */
    private void saveToDatabase(Recipe recipe) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Recipes")
                .add(recipe)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(AddNewRecipeActivity.this, "Added Recipe Successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the form
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNewRecipeActivity.this, "Failed to add recipe: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateRecipeInDatabase(Recipe recipe) {


        // Retrieve data from the input fields
        String recipeName = binding.etRecipeName.getText().toString().trim();
        String imageUrl = binding.etImageUrl.getText().toString().trim();
        String description = binding.etRecipeDescription.getText().toString().trim();
        String tags = binding.etTags.getText().toString().trim();
        String servingsStr = binding.etServings.getText().toString().trim();
        String prepTimeStr = binding.etPrepTime.getText().toString().trim();
        String cookingTimeStr = binding.etCookingTime.getText().toString().trim();
        String ingredients = binding.etIngredients.getText().toString().trim();
        String instructions = binding.etInstructions.getText().toString().trim();

        // Validate inputs
        if (recipeName.isEmpty()) {
            binding.etRecipeName.setError("Recipe name is required");
            binding.etRecipeName.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            binding.etRecipeDescription.setError("Description is required");
            binding.etRecipeDescription.requestFocus();
            return;
        }

        if (ingredients.isEmpty()) {
            binding.etIngredients.setError("At least one ingredient is required");
            binding.etIngredients.requestFocus();
            return;
        }

        if (instructions.isEmpty()) {
            binding.etInstructions.setError("At least one instruction is required");
            binding.etInstructions.requestFocus();
            return;
        }

        if (servingsStr.isEmpty() || !servingsStr.matches("\\d+")) {
            binding.etServings.setError("Valid servings number is required");
            binding.etServings.requestFocus();
            return;
        }

        if (prepTimeStr.isEmpty() || !prepTimeStr.matches("\\d+")) {
            binding.etPrepTime.setError("Valid preparation time is required");
            binding.etPrepTime.requestFocus();
            return;
        }

        if (cookingTimeStr.isEmpty() || !cookingTimeStr.matches("\\d+")) {
            binding.etCookingTime.setError("Valid cooking time is required");
            binding.etCookingTime.requestFocus();
            return;
        }

        // Convert numeric fields
        int servings = Integer.parseInt(servingsStr);
        int prepTime = Integer.parseInt(prepTimeStr);
        int cookingTime = Integer.parseInt(cookingTimeStr);

        // Convert comma-separated fields into lists
        List<String> tagList = tags.isEmpty() ? new ArrayList<>() : Arrays.asList(tags.split("\\s*,\\s*"));
        List<String> ingredientList = Arrays.asList(ingredients.split("\\s*,\\s*"));
        List<String> instructionList = Arrays.asList(instructions.split("\\s*,\\s*"));

        // Create a Recipe object
        Recipe updatenewdatarecipe = new Recipe(
                recipe.getId(),
                recipeName,
                description,
                imageUrl,
                tagList,
                ingredientList,
                instructionList,
                prepTime,
                cookingTime,
                servings
        );


        FirebaseFirestore db = FirebaseFirestore.getInstance();


        // Ensure the recipe has a valid ID before attempting to update
        if (recipe.getId() == null || recipe.getId().isEmpty()) {
            Toast.makeText(this, "Recipe ID is missing. Cannot update.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.w(TAG, "Update Data: " + updatenewdatarecipe.getName());

        db.collection("Recipes")
                .document(recipe.getId()) // Use the recipe's ID to update the specific document
                .set(updatenewdatarecipe) // Overwrite the existing data with the new recipe object
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddNewRecipeActivity.this, "Updated Recipe Successfully!", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity after updating
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNewRecipeActivity.this, "Failed to update recipe: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        startActivity(new Intent(this,MainActivity2.class));
        finish();
    }

}



//Testing Dummy Data

//Chocolate Lava Cake

//https://www.cookingclassy.com/wp-content/uploads/2022/02/molten-lava-cake-17.jpg

//A decadent dessert with a gooey chocolate center,
// perfect for impressing guests or satisfying a sweet craving.

//Dessert, Chocolate, Baking
//2,10,12

//100g dark chocolate, 100g butter,
// 2 eggs, 50g sugar, 30g flour,
// 1 tsp vanilla extract, Pinch of salt

//Preheat oven to 220°C, Melt chocolate and butter together,
// Whisk eggs and sugar until fluffy, Add melted chocolate mixture,
// Fold in flour and vanilla extract, Pour batter into greased ramekins,
// Bake for 10-12 minutes, Let cool slightly and serve with ice cream or whipped cream
