package com.example.mealmate.fragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mealmate.adapters.CategoryAdapter;
import com.example.mealmate.adapters.GroceryAdapter;
import com.example.mealmate.databinding.FragmentGroceryBinding;
import com.example.mealmate.geotag.ActivityGeoTag;
import com.example.mealmate.models.Category;
import com.example.mealmate.models.Grocery;
import com.example.mealmate.models.GroceryListCategorizer;
import com.example.mealmate.models.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GroceryFragment extends Fragment {

    private FragmentGroceryBinding binding;
    List<Grocery> favoriteRecipes;
    List<Category> favoriteCategory;

    Grocery groceryData;
    Category categoryData;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGroceryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadCategories();
        loadGroceryList();

        binding.btnGoGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), ActivityGeoTag.class);
                startActivity(intent);
            }
        });
    }


    private void loadGroceryList() {
        favoriteRecipes = new ArrayList<>();


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Recipes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                List<String> ingredients = (List<String>) document.get("ingredients");

                                // Categorize ingredients
                                List<Grocery> groceryList = new GroceryListCategorizer().categorizeIngredients(ingredients);

                                // Merge categorized groceries into the main list
                                for (Grocery grocery : groceryList) {
                                    mergeGrocery(favoriteRecipes, grocery);
                                }

                                binding.groceryRecyclerView.setAdapter(new GroceryAdapter(requireContext()));
                                GroceryAdapter adapter = (GroceryAdapter) binding.groceryRecyclerView.getAdapter();
                                if (adapter!=null){
                                    adapter.setGroceryList(favoriteRecipes);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    private void mergeGrocery(List<Grocery> categorizedGroceryList, Grocery newGrocery) {
        for (Grocery existingGrocery : categorizedGroceryList) {
            if (existingGrocery.getCategoryName().equals(newGrocery.getCategoryName())) {
                // Add ingredients to the existing category
                existingGrocery.getIngredients().addAll(newGrocery.getIngredients());
                return;
            }
        }
        // If the category doesn't exist, add it as a new one
        categorizedGroceryList.add(newGrocery);
    }

    private void loadCategories() {
        favoriteCategory = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Category")
                        .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            for (QueryDocumentSnapshot document : task.getResult()){
                                                String id = document.getId();
                                                String name = document.getString("name") != null ? document.getString("name") : "Unknown Recipe";
                                                String description = document.getString("description") != null ? document.getString("description") : "No description available";

                                                categoryData = new Category(
                                                    id,
                                                    name,
                                                    description
                                                );
                                                favoriteCategory.add(categoryData);
                                                binding.categoryShowLists.setAdapter(new CategoryAdapter());
                                                CategoryAdapter adapter = (CategoryAdapter) binding.categoryShowLists.getAdapter();
                                                if (adapter!=null){
                                                    adapter.setCategoryList(favoriteCategory);
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }
                                        }else{
                                            Log.w(TAG, "Error getting documents.", task.getException());
                                        }
                                    }
                                });

//        favoriteCategory.add(new Category("1","vegetables","",""));
//        favoriteCategory.add(new Category("2","grains","",""));
//        favoriteCategory.add(new Category("3","proteins","",""));
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}