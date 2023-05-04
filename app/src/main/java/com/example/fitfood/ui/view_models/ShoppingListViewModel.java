package com.example.fitfood.ui.view_models;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fitfood.data.data_sources.room.entites.PlanEntity;
import com.example.fitfood.data.data_sources.room.entites.ProductEntity;
import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;
import com.example.fitfood.data.repositories.PlanRepository;
import com.example.fitfood.data.repositories.ProductRepository;
import java.util.List;
import java.util.Objects;

public class ShoppingListViewModel extends AndroidViewModel {
    private ProductRepository productRepository;

    private final LiveData<List<PlanEntity>> allPlans;
    private PlanRepository planRepository;

    private LiveData<List<RecipeEntity>> allRecipes;

    public ShoppingListViewModel(Application application) {
        super(application);
        ProductRepository productRepository = new ProductRepository(application);
        this.productRepository = productRepository;

        PlanRepository planRepository = new PlanRepository(application);
        this.planRepository = planRepository;
        this.allPlans = planRepository.getAllPlans();
    }

    public LiveData<List<ProductEntity>> getAllProductsByType(String type){
        return productRepository.getAllProductsByType(type);
    }

    public LiveData<List<RecipeEntity>> getAllRecipesByPlan(int planId){
        if (allRecipes == null) allRecipes = planRepository.getAllRecipesByPlan(planId);
        return allRecipes;
    }


    public LiveData<List<PlanEntity>> getAllPlans(){
        return allPlans;
    }


    public void insert(ProductEntity product) {
        this.productRepository.insert(product);
    }

    public void update(ProductEntity product) {
        this.productRepository.update(product);
    }

    public void delete(ProductEntity product) {
        this.productRepository.delete(product);
    }

    public void deleteGenerated(){
        productRepository.deleteGenerated();
    }
}