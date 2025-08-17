package com.example.mealmate.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mealmate.models.FavoriteRecipe;
import java.util.List;

@Dao
public interface RecipeDao {
    @Insert
    long insertFavorite(FavoriteRecipe recipe);

    @Query("DELETE FROM favorite_recipes WHERE recipeID = :id")
    void deleteFavorite(String id);

    @Query("SELECT * FROM favorite_recipes")
    List<FavoriteRecipe> getAll();

    @Query("SELECT * FROM favorite_recipes WHERE recipeID = :favouriteName")
    FavoriteRecipe getFavourite(String favouriteName);

    @Query("SELECT * FROM favorite_recipes")
    List<FavoriteRecipe> getAllFavourites();

}
