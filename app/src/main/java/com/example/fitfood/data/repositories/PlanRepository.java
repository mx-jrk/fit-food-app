package com.example.fitfood.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fitfood.data.data_sources.room.dao.PlanDAO;
import com.example.fitfood.data.data_sources.room.dao.RecipeDAO;
import com.example.fitfood.data.data_sources.room.entites.PlanEntity;
import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;
import com.example.fitfood.data.data_sources.room.entites.UserEntity;
import com.example.fitfood.data.data_sources.room.root.PlanDatabase;

import java.util.List;

public class PlanRepository {
    private final PlanDAO planDAO;
    private final LiveData<List<PlanEntity>> allPlans;

    private LiveData<List<RecipeEntity>> allRecipes;

    private LiveData<PlanEntity> plansById;

    private final RecipeDAO recipeDAO;

    public PlanRepository(Application application){
        PlanDatabase db = PlanDatabase.getDatabase(application);
        planDAO = db.planDAO();
        allPlans = planDAO.getAllPlans();

        recipeDAO = db.recipeDAO();
    }

    //Method of obtaining dishes corresponding to the selected plan from ROOM
    public LiveData<List<RecipeEntity>> getAllRecipesByPlan(int planId){
        if (allRecipes == null) allRecipes = recipeDAO.getAllRecipesByPlan(planId);
        return allRecipes;
    }

    //Method of obtaining dishes corresponding to the selected plan and day of the week from ROOM
    public LiveData<List<RecipeEntity>> getRecipesByPlan(UserEntity user){
        return recipeDAO.getRecipesByPlan(user.PlanId, user.LastChangeDate.split(" ")[0]);
    }

    //Overloaded method of obtaining dishes corresponding to the selected plan and day of the week from ROOM
    public LiveData<List<RecipeEntity>> getRecipesByPlan(int planId, String day){
        return recipeDAO.getRecipesByPlan(planId, day);
    }

    //Method of returning the power plan object by its Id from ROOM
    public LiveData<PlanEntity> getPlansById(int planId){
        if (plansById == null) plansById = planDAO.getPlansById(planId);
        return plansById;
    }

    //Method of obtaining all meal plans from ROOM
    public LiveData<List<PlanEntity>> getAllPlans(){
        return allPlans;
    }
}
