package com.example.fitfood.data.repositories;

import android.app.Application;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.example.fitfood.data.data_sources.room.dao.PlanDAO;
import com.example.fitfood.data.data_sources.room.dao.RecipeDAO;
import com.example.fitfood.data.data_sources.room.entites.PlanEntity;
import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;
import com.example.fitfood.data.data_sources.room.entites.UserEntity;
import com.example.fitfood.data.data_sources.room.root.PlanDatabase;
import com.example.fitfood.data.data_sources.room.root.ProductDatabase;

import java.util.List;

public class PlanRepository {
    private final PlanDAO planDAO;
    private final LiveData<List<PlanEntity>> allPlans;
    private LiveData<List<RecipeEntity>> allRecipes;
    private LiveData<PlanEntity> plansById;
    private final RecipeDAO recipeDAO;

    private static volatile PlanRepository instance;

    public static synchronized PlanRepository getInstance(Application application) {
        if (instance == null) {
            instance = new PlanRepository(application);
        }
        return instance;
    }

    public PlanRepository(Application application){
        PlanDatabase db = PlanDatabase.getDatabase(application);
        planDAO = db.planDAO();
        allPlans = planDAO.getAllPlans();

        recipeDAO = db.recipeDAO();
    }

    public LiveData<List<RecipeEntity>> getRecipesByPlan(UserEntity user){
         if (allRecipes == null) allRecipes = recipeDAO.getRecipesByPlan(user.PlanId, user.LastChangeDate.split(" ")[0]);
        return allRecipes;
    }

    public LiveData<PlanEntity> getPlansById(int planId){
        if (plansById == null) plansById = planDAO.getPlansById(planId);
        return plansById;
    }



    public LiveData<List<PlanEntity>> getAllPlans(){
        return allPlans;
    }
}
