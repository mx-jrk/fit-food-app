package com.example.fitfood.ui.view_models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fitfood.data.data_sources.room.entites.ProductEntity;
import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;
import com.example.fitfood.data.repositories.PlanRepository;
import com.example.fitfood.data.repositories.ProductRepository;

import java.util.List;

public class ShoppingListViewModel extends AndroidViewModel {
    private final ProductRepository productRepository;

    private final PlanRepository planRepository;

    private LiveData<List<RecipeEntity>> allRecipes;

    public ShoppingListViewModel(Application application) {
        super(application);
        this.productRepository = new ProductRepository(application);

        this.planRepository = new PlanRepository(application);
    }

    public LiveData<List<ProductEntity>> getAllProductsByType(String type){
        return productRepository.getAllProductsByType(type);
    }

    public LiveData<List<RecipeEntity>> getAllRecipesByPlan(int planId){
        if (allRecipes == null) allRecipes = planRepository.getAllRecipesByPlan(planId);
        return allRecipes;
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