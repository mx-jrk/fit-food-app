package com.example.fitfood.ui.view_models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.example.fitfood.data.DataLoadCallback;
import com.example.fitfood.data.data_sources.room.entites.ProductEntity;
import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;
import com.example.fitfood.data.repositories.PlanRepository;
import com.example.fitfood.data.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    //The method of bulkhead shopping list
    public void updateProductsForNewDay(LifecycleOwner lifecycleOwner, int planId, DataLoadCallback callback){
        deleteGenerated();
        planRepository.getRecipesByPlan(planId, getDayOfWeek()).observe(lifecycleOwner, todayRecipes -> {
            parseRecipes(todayRecipes, "today");

            planRepository.getRecipesByPlan(planId, getNextDayOfWeek(getDayOfWeek())).observe(lifecycleOwner, tomorrowRecipes -> {
                parseRecipes(tomorrowRecipes, "tomorrow");

                planRepository.getAllRecipesByPlan(planId).observe(lifecycleOwner, weekRecipes -> {
                    parseRecipes(weekRecipes, "week");
                    callback.onDataLoaded();
                });
            });
        });
    }

    private String getDayOfWeek(){
        return new Date().toString().split(" ")[0];
    }

    private String getNextDayOfWeek(String dayOfWeek) {
        if (Objects.equals(dayOfWeek, "Sun")) return "Mon";
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < days.length; i++){
            if (days[i].equals(dayOfWeek)) return days[i + 1];
        }
        return dayOfWeek;
    }

    //Method of filling the product database
    private void parseRecipes(List<RecipeEntity> recipeEntities, String type){
        String[] products;
        List<ProductEntity> productEntityList = new ArrayList<>();
        ProductEntity generatedProduct;
        for (RecipeEntity recipe : recipeEntities) {
            if (recipe.Products == null) continue;
            products = recipe.Products.split("\n");
            for (String product : products) {
                generatedProduct = new ProductEntity(product.split(": ")[0], Integer.parseInt(product.split(": ")[1].trim()), false, true, type);
                if (productEntityList.contains(generatedProduct)) {
                    productEntityList.get(productEntityList.indexOf(generatedProduct)).count++;
                } else {
                    productEntityList.add(generatedProduct);
                }
            }
        }
        for (ProductEntity product : productEntityList) {
            insert(product);
        }
    }
}