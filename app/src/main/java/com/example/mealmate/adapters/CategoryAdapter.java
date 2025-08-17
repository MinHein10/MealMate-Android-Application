package com.example.mealmate.adapters;

import static com.example.mealmate.databinding.ItemRecipeBinding.bind;
import static com.example.mealmate.databinding.ItemRecipeBinding.inflate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealmate.R;
import com.example.mealmate.databinding.ItemRecipeBinding;
import com.example.mealmate.models.Category;

import java.util.List;
import java.util.zip.Inflater;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    List<Category> categoryList;

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryHolder(inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.onBind(category);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder{

        private ItemRecipeBinding binding;

        public CategoryHolder(@NonNull ItemRecipeBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void onBind(Category category){
             binding.cardTextRecipe.setText(category.getName());
             binding.cardTextDescription.setText(category.getDescription());

             if (category.getName().equalsIgnoreCase("Meat & Seafood")){
                 binding.cardImageRecipe.setImageResource(R.drawable.meatseadfood);
             }else if(category.getName().equalsIgnoreCase("Produce")){
                 binding.cardImageRecipe.setImageResource(R.drawable.produce);
             }else if (category.getName().equalsIgnoreCase("Dairy")){
                 binding.cardImageRecipe.setImageResource(R.drawable.dairy);
             }else if (category.getName().equalsIgnoreCase("Baking & Spices")){
                 binding.cardImageRecipe.setImageResource(R.drawable.baking);
             }else if (category.getName().equalsIgnoreCase("Pasta & Grains")){
                 binding.cardImageRecipe.setImageResource(R.drawable.pasta);
             }else if (category.getName().equalsIgnoreCase("Oils & Condiments")){
                 binding.cardImageRecipe.setImageResource(R.drawable.oil);
             }else{
                 binding.cardImageRecipe.setImageResource(R.drawable.mealmate);
             }
        }
    }
}
