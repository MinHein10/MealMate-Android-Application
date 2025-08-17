//package com.example.mealmate.adapters;
//
//import static com.example.mealmate.databinding.ItemRecipeBinding.inflate;
//
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.mealmate.databinding.GroceryItemBinding;
//import com.example.mealmate.databinding.ItemRecipeBinding;
//import com.example.mealmate.models.Grocery;
//import com.example.mealmate.models.Recipe;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class BackupGroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.GroceryHolder> {
//    List<Recipe> recipeList = new ArrayList<>();
//
//    public void setRecipeList(List<Recipe> recipeList){
//        this.recipeList = recipeList;
//    }
//
//    @NonNull
//    @Override
//
//
//    public GroceryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new GroceryHolder(GroceryItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull GroceryAdapter.GroceryHolder holder, int position) {
//        Recipe recipe = recipeList.get(position);
//        holder.onBind(recipe);
//    }
//
//    @Override
//    public int getItemCount() {
//        return recipeList.size();
//    }
//
//    public static class GroceryHolder extends RecyclerView.ViewHolder{
//
//        GroceryItemBinding binding;
//        public GroceryHolder(@NonNull GroceryItemBinding groceryitemView) {
//            super(groceryitemView.getRoot());
//            binding = groceryitemView;
//        }
//
//        public void onBind(Recipe recipe) {
//
//            StringBuilder ingredientsBuilder = new StringBuilder();
//            for (String ingredient : recipe.getIngredients()) {
//                ingredientsBuilder.append("- ").append(ingredient).append("\n");
//            }
//            binding.groceryIngredients.setText(ingredientsBuilder.toString().trim());
//        }
//    }
//}
