package com.example.mealmate.adapters;

import static com.example.mealmate.databinding.ItemRecipeBinding.bind;
import static com.example.mealmate.databinding.ItemRecipeBinding.inflate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealmate.AddNewRecipeActivity;
import com.example.mealmate.DetailRecipeActivity;
import com.example.mealmate.MainActivity2;
import com.example.mealmate.R;
import com.example.mealmate.databinding.ItemRecipeBinding;
import com.example.mealmate.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder>
{

    List<Recipe> recipeList = new ArrayList<>();
    Context context;

    public void setRecipeList(List<Recipe> recipeList){
        this.recipeList = recipeList;
    }
    @NonNull
    @Override

    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        return new RecipeHolder(inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, int position)
    {
        Recipe recipe = recipeList.get(position);
        holder.onBind(recipe);
    }

    @Override
    public int getItemCount(){
        return recipeList.size();
    }

    public static class RecipeHolder extends RecyclerView.ViewHolder
    {

        ItemRecipeBinding binding;
        public RecipeHolder(@NonNull ItemRecipeBinding itemView)
        {

            super(itemView.getRoot());
            binding = itemView;

        }

        public void onBind(Recipe recipe){
            binding.cardTextRecipe.setText(recipe.getName());
//            binding.cardTextDescription.setText(recipe.getDescription());

            // Load the image into an ImageView (using Glide or Picasso)
            Glide
                    .with(binding.getRoot().getContext())
                    .load(recipe.getImageUrl())
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(binding.cardImageRecipe);

            binding.getRoot().setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(binding.getRoot().getContext(), DetailRecipeActivity.class);
                    intent.putExtra("recipe",recipe);
                    binding.getRoot().getContext().startActivity(intent);
                }
            });

            StringBuilder ingredientsBuilder = new StringBuilder();
            for (String ingredient : recipe.getIngredients())
            {
                ingredientsBuilder.append("- ").append(ingredient).append("\n");
            }
            binding.cardTextDescription.setText(ingredientsBuilder.toString().trim()); // Assuming you added cardTextIngredients
        }
    }
}
