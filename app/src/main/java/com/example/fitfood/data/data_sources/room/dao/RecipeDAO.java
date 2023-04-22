package com.example.fitfood.data.data_sources.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;

import java.util.List;

@Dao
public interface RecipeDAO {
    @Insert
    void insert(RecipeEntity recipeEntity);

    @Query("SELECT * FROM recipes_table WHERE PlansId = :plan")
    LiveData<List<RecipeEntity>> getRecipesByPlan(int plan);

}
