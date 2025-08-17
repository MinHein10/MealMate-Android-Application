package com.example.mealmate.fragment;

import static android.content.ContentValues.TAG;

import static androidx.core.app.ActivityCompat.finishAffinity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mealmate.LoginActivity;
import com.example.mealmate.R;
import com.example.mealmate.adapters.CategoryAdapter;
import com.example.mealmate.adapters.RecipeAdapter;
import com.example.mealmate.databinding.FragmentRecipesBinding;
import com.example.mealmate.models.Category;
import com.example.mealmate.models.Ingredient;
import com.example.mealmate.models.Recipe;
import com.example.mealmate.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import java.util.HashMap;
import java.util.Map;

public class RecipesFragment extends Fragment {

    private FragmentRecipesBinding binding;
    List<Recipe> favoriteRecipes;

    Recipe recipeData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRecipesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
//        binding.recipesList.setAdapter(new RecipeAdapter());
        //Logout Button
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
            loadlistRecipe();
            loadProfile();
    }

    private void signOut() {
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Sign Out", (dialogInterface, i) -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(requireActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    requireActivity().finishAffinity();
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void loadProfile(){
//        User user = new User();
//        user.setName("MH");
//        user.setEmail("mh@gmail.com");
//        binding.profileName.setText(user.getName());
//        binding.profileEmail.setText(user.getEmail());

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("authusers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

//                                Map the document to the User model
                                User user = document.toObject(User.class);

                                // Update the UI with the user's data
                                if (user.getName() != null) {
                                    binding.profileName.setText(user.getName());
                                }
                                if (user.getEmail() != null) {
                                    binding.profileEmail.setText(user.getEmail());
                                }

                                if (user.getImage() != null) {
                                    Glide.with(requireContext())
                                            .load(user.getImage())
                                            .centerCrop()
                                            .placeholder(R.mipmap.ic_launcher)
                                            .into(binding.profileImage);
                                }

                            }
                        } else {
                            Log.w("RecipesFragment", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void loadlistRecipe(){

        Log.d(TAG, "Firebase Recipe Data : ");

        favoriteRecipes = new ArrayList<>();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Recipes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


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

                                favoriteRecipes.add(recipeData);
                                Log.d(TAG, "Recipe Data : "+String.valueOf(favoriteRecipes));

                                binding.recipesList.setAdapter(new RecipeAdapter());
                                RecipeAdapter adapter = (RecipeAdapter) binding.recipesList.getAdapter();
                                if (adapter!=null){
                                    adapter.setRecipeList(favoriteRecipes);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}