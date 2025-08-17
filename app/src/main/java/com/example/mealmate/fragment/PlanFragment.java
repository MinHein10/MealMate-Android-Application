package com.example.mealmate.fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.mealmate.adapters.RecipeAdapter;
import com.example.mealmate.databinding.FragmentPlanBinding;
import com.example.mealmate.models.FavoriteRecipe;
import com.example.mealmate.models.Recipe;
import com.example.mealmate.room.RecipeRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PlanFragment extends Fragment {

    private FragmentPlanBinding binding;
    RecipeRepository recipeRepository;
    Recipe recipeData;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPlanBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        loadFavorites();
    }



    private void loadFavorites() {
        recipeRepository = new RecipeRepository(requireActivity().getApplication());
        List<FavoriteRecipe> favoriteRecipes = recipeRepository.getAllFavorites();

        if (favoriteRecipes.isEmpty()) {
            Toast.makeText(requireContext(), "No Favourites", Toast.LENGTH_SHORT).show();
            binding.rvFavourites.setVisibility(View.GONE);
            binding.noFavourites.setVisibility(View.VISIBLE);

            binding.rvLastWeek.setVisibility(View.GONE);
            binding.noLastWeekItems.setVisibility(View.VISIBLE);

        } else{
            // Detail Implementation for Favorite Meal Plans
            binding.rvFavourites.setLayoutManager(new GridLayoutManager(requireContext(), 2));

            List<Recipe> recipes = new ArrayList<>();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Recipes")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    for (FavoriteRecipe favouriteRecipe : favoriteRecipes) {
                                        if (document.getId().equals(favouriteRecipe.getRecipeID())){

                                            Log.w(TAG, "Fav Data" + favouriteRecipe);

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
                                            recipeData = new Recipe(
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
                                            recipes.add(recipeData);
                                            Log.w(TAG, "Fav Data" + recipes);
                                        }
                                    }
                                }

                                binding.rvFavourites.setVisibility(View.VISIBLE);
                                binding.noFavourites.setVisibility(View.GONE);

                                binding.rvLastWeek.setVisibility(View.GONE);
                                binding.noLastWeekItems.setVisibility(View.VISIBLE);

                                binding.rvFavourites.setAdapter(new RecipeAdapter());
                                RecipeAdapter adapter = (RecipeAdapter) binding.rvFavourites.getAdapter();
                                if (adapter != null) {
                                    adapter.setRecipeList(recipes);
                                    adapter.notifyDataSetChanged();
                                }

                            }else{
                                binding.rvFavourites.setVisibility(View.GONE);
                                binding.noFavourites.setVisibility(View.VISIBLE);

                                binding.rvLastWeek.setVisibility(View.GONE);
                                binding.noLastWeekItems.setVisibility(View.VISIBLE);

                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    }