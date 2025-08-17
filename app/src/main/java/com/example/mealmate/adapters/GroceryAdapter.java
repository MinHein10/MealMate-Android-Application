package com.example.mealmate.adapters;

import static com.example.mealmate.databinding.ItemRecipeBinding.inflate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealmate.ActivitySendMessage;
import com.example.mealmate.databinding.GroceryItemBinding;
import com.example.mealmate.databinding.ItemRecipeBinding;
import com.example.mealmate.geotag.ActivityGeoTag;
import com.example.mealmate.models.Grocery;
import com.example.mealmate.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.GroceryHolder> {
    private List<Grocery> groceryList = new ArrayList<>();
    private final Context context;

    public GroceryAdapter(Context context) {
        this.context = context; // Pass the context when creating the adapter
    }

    public void setGroceryList(List<Grocery> groceryList) {
        this.groceryList = groceryList;
    }

    @NonNull
    @Override
    public GroceryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroceryHolder(GroceryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull GroceryHolder holder, int position) {
        Grocery grocery = groceryList.get(position);
//        holder.onBind(grocery);
        holder.onBind(grocery, context);
    }

    @Override
    public int getItemCount() {
        return groceryList.size();
    }

    public static class GroceryHolder extends RecyclerView.ViewHolder {

        GroceryItemBinding binding;

        public GroceryHolder(@NonNull GroceryItemBinding groceryItemBinding) {
            super(groceryItemBinding.getRoot());
            binding = groceryItemBinding;
        }

        public void onBind(Grocery grocery, Context context) {
            // Set category name
            binding.groceryCategory.setText(grocery.getCategoryName());

            binding.btnFindGroceryStore.setOnClickListener(v -> {
                Intent intent = new Intent(context, ActivityGeoTag.class);
                intent.putStringArrayListExtra("ingredients", new ArrayList<>(grocery.getIngredients()));
                context.startActivity(intent);
            });

            binding.btnSendList.setOnClickListener(v -> {
                Intent intent = new Intent(context, ActivitySendMessage.class);
                intent.putStringArrayListExtra("ingredients", new ArrayList<>(grocery.getIngredients()));
                context.startActivity(intent);
            });


            // Build ingredients list
            StringBuilder ingredientsBuilder = new StringBuilder();
            for (String ingredient : grocery.getIngredients()) {
                ingredientsBuilder.append("- ").append(ingredient).append("\n");
            }
            binding.groceryIngredients.setText(ingredientsBuilder.toString().trim());

            binding.groceryItemCheckbox.setChecked(grocery.isCheck());

            updateIngredientStyle(binding.groceryIngredients,grocery.isCheck());

            binding.groceryItemCheckbox.setOnCheckedChangeListener(((buttonView, isChecked) -> {
                grocery.setCheck(isChecked);
                updateIngredientStyle(binding.groceryIngredients,isChecked);
            }));
        }

        private void updateIngredientStyle(TextView groceryIngredients, boolean check) {
            if (check){
                groceryIngredients.setPaintFlags(groceryIngredients.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                groceryIngredients.setTextColor(Color.GRAY); // Optional: Change color to gray
            }
            else{
                groceryIngredients.setPaintFlags(groceryIngredients.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                groceryIngredients.setTextColor(Color.BLACK);
            }
        }
    }
}
