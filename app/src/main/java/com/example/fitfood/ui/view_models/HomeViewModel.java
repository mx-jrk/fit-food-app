package com.example.fitfood.ui.view_models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fitfood.data.data_sources.room.entites.PlanEntity;
import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;
import com.example.fitfood.data.repositories.PlanRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private PlanRepository planRepository;
    private final LiveData<List<RecipeEntity>> allRecipes;

    public HomeViewModel(Application application){
        super(application);
        PlanRepository planRepository = new PlanRepository(application);
        this.planRepository = planRepository;
        this.allRecipes = planRepository.getAllRecipes();
    }

    public LiveData<List<RecipeEntity>> getAllRecipes(){
        return allRecipes;
    }

}
