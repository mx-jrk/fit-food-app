package com.example.fitfood.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fitfood.data.data_sources.room.dao.ProductDAO;
import com.example.fitfood.data.data_sources.room.entites.ProductEntity;
import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;
import com.example.fitfood.data.data_sources.room.root.ProductDatabase;

import java.util.List;

public class ProductRepository {
    private final ProductDAO productDAO;

    public ProductRepository(Application application){
        ProductDatabase db = ProductDatabase.getDatabase(application);
        productDAO = db.productDAO();
    }

    // Method of getting all products by day of the week from ROOM
    public LiveData<List<ProductEntity>> getAllProductsByType(String type){
        return productDAO.getProductsByType(type);
    }

    public void insert(ProductEntity product){
        ProductDatabase.databaseWriteExecutor.execute(()-> productDAO.insert(product));
    }

    public void update(ProductEntity product){
       productDAO.update(product);
    }

    public void delete(ProductEntity product){
        ProductDatabase.databaseWriteExecutor.execute(()-> productDAO.delete(product));
    }

    //Method of removing generated products from the ROOM
    public void deleteGenerated(){
        productDAO.deleteGenerated();
    }


}
