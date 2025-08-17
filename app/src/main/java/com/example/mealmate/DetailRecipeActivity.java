package com.example.mealmate;

import static android.content.ContentValues.TAG;

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

import com.bumptech.glide.Glide;
import com.example.mealmate.databinding.ActivityDetailRecipeBinding;
import com.example.mealmate.models.FavoriteRecipe;
import com.example.mealmate.models.Recipe;
import com.example.mealmate.room.RecipeRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DetailRecipeActivity extends AppCompatActivity {

    private ActivityDetailRecipeBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Recipe recipedata = (Recipe) getIntent().getSerializableExtra("recipe");
//        Log.w(TAG, "Related Recipe ID: " + recipedata.getId());
        loadSpecificRecipe(recipedata.getId());
    }

    private void loadSpecificRecipe(String recipeId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Access the specific document by its ID
        db.collection("Recipes").document(recipeId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                // Extract recipe data
                                String id = document.getId();
                                String name = document.getString("name") != null ? document.getString("name") : "Unknown Recipe";
                                String description = document.getString("description") != null ? document.getString("description") : "No description available";
                                String imageUrl = document.getString("imageUrl") != null ? document.getString("imageUrl") : "";

                                // Retrieve array fields
                                List<String> tags = (List<String>) document.get("tags");
                                List<String> ingredients = (List<String>) document.get("ingredients");
                                List<String> instructions = (List<String>) document.get("instructions");

                                int preparationTime = document.getLong("preparationTime") != null ? document.getLong("preparationTime").intValue() : 0;
                                int cookingTime = document.getLong("cookingTime") != null ? document.getLong("cookingTime").intValue() : 0;
                                int servings = document.getLong("servings") != null ? document.getLong("servings").intValue() : 0;

                                // Create the Recipe object
                                Recipe specificRecipe = new Recipe(
                                        id,
                                        name,
                                        description,
                                        imageUrl,
                                        tags != null ? tags : new ArrayList<>(), // Fallback to empty list
                                        ingredients != null ? ingredients : new ArrayList<>(), // Fallback to empty list
                                        instructions != null ? instructions : new ArrayList<>(), // Fallback to empty list
                                        preparationTime,
                                        cookingTime,
                                        servings
                                );
                                Log.w(TAG, "recipe exists with ID: " + specificRecipe);

                                // Display the recipe details (e.g., update UI or pass to a detail page)
                                showRecipeDetails(specificRecipe);

                            } else {
                                Log.w(TAG, "No such recipe exists with ID: " + recipeId);
                            }
                        } else {
                            Log.w(TAG, "Error fetching recipe.", task.getException());
                        }
                    }
                });
    }

    private void showRecipeDetails(Recipe recipe) {
        // Example: Update TextViews or ImageView with recipe details
        binding.recipeTitle.setText(recipe.getName());
        binding.recipeDescription.setText(recipe.getDescription());
        binding.recipeTags.setText("Tags: " + String.join(", ", recipe.getTags()));
        binding.recipeIngredients.setText("Ingredients:\n" + String.join("\n", recipe.getIngredients()));
        binding.recipeInstructions.setText("Instructions:\n" + String.join("\n", recipe.getInstructions()));

        binding.btnDetailBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailRecipeActivity.this,MainActivity2.class));
            }
        });

        binding.btnUpdateRecipe.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(binding.getRoot().getContext(), AddNewRecipeActivity.class);
                intent.putExtra("recipe",recipe);
                intent.putExtra("isRecipeEdit",true);
                binding.getRoot().getContext().startActivity(intent);
            }
        });

        binding.btnDeleteRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                AlertDialog dialog = new AlertDialog.Builder(DetailRecipeActivity.this).setTitle("Delete Recipe").setMessage("Are you sure you want to delete for " + recipe.getName()+" ?").setPositiveButton("Delete",(dialogInterface, i)->{
                    db.collection("Recipes")
                            .document(recipe.getId()) // Use the recipe's ID to update the specific document
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(DetailRecipeActivity.this, "Deleted Recipe Successfully!", Toast.LENGTH_SHORT).show();
                                    finish(); // Close the activity after updating
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DetailRecipeActivity.this, "Failed to delete recipe: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                    startActivity(new Intent(DetailRecipeActivity.this,MainActivity2.class));
                    finish();
                }).setNegativeButton("Cancel",(dialogInterface,i)->{
                    dialogInterface.dismiss();
                }).show();

            }
        });

//        Favorite & un-favorite
        checkFavorite(recipe);
        binding.btnRecipeFavorite.setOnClickListener(view->favouriteRecipe(recipe));

        // Load the image into an ImageView (using Glide or Picasso)
        Glide.with(this)
                .load(recipe.getImageUrl())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.recipeImage);
    }

    private void checkFavorite(Recipe recipe) {
        RecipeRepository repository = new RecipeRepository(getApplication());
        boolean isFavorite = repository.isFavorite(recipe.getId());
        if (isFavorite){
            binding.btnRecipeFavorite.setColorFilter(getResources().getColor(R.color.purple_500));
        }else{
            binding.btnRecipeFavorite.setColorFilter(getResources().getColor(R.color.black));
        }
    }

    private void favouriteRecipe(Recipe recipe) {
        RecipeRepository repository = new RecipeRepository(getApplication());
        boolean isFavorite = repository.isFavorite(recipe.getId());

        if (isFavorite){
            repository.delete(new FavoriteRecipe(recipe.getId()));
            binding.btnRecipeFavorite.setColorFilter(getResources().getColor(R.color.black));
        }else{
            repository.insert(new FavoriteRecipe(recipe.getId()));
            binding.btnRecipeFavorite.setColorFilter(getResources().getColor(R.color.purple_500));
        }
    }


}