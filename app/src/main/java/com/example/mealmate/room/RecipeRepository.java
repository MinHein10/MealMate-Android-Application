package com.example.mealmate.room;

import android.app.Application;

import com.example.mealmate.models.FavoriteRecipe;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class RecipeRepository {
    private com.example.mealmate.room.RecipeDao recipeDao;

    public RecipeRepository(Application application) {
        RecipeDatabase database = RecipeDatabase.getInstance(application);
        recipeDao = database.favoriteDao();
    }

    public long insert(FavoriteRecipe recipe){
        Future<Long> future = RecipeDatabase.databaseWriteExecutor.submit(()->recipeDao.insertFavorite(recipe));
        try{
            return future.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
            Thread.currentThread().interrupt();
            return -1;
        }
    }

    public void delete(FavoriteRecipe recipe){
        RecipeDatabase.databaseWriteExecutor.submit(()->recipeDao.deleteFavorite(recipe.getRecipeID()));
    }

    public boolean isFavorite(String favoriteRecipe){
        Future<Boolean> future = RecipeDatabase.databaseWriteExecutor.submit(()->recipeDao.getFavourite(favoriteRecipe)!=null);
        try{
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public List<FavoriteRecipe> getAllFavorites(){
        Future<List<FavoriteRecipe>> future = RecipeDatabase.databaseWriteExecutor.submit(()->recipeDao.getAllFavourites());
        try{
            return future.get();
        }catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

}
