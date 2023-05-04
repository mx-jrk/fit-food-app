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

    private static volatile ProductRepository instance;

    public static synchronized ProductRepository getInstance(Application application) {
        if (instance == null) {
            instance = new ProductRepository(application);
        }
        return instance;
    }
    public ProductRepository(Application application){
        ProductDatabase db = ProductDatabase.getDatabase(application);
        productDAO = db.productDAO();
    }

    public LiveData<List<ProductEntity>> getAllProductsByType(String type){
        return productDAO.getProductsByType(type);
    }

    public void insert(ProductEntity product){
        ProductDatabase.databaseWriteExecutor.execute(()-> productDAO.insert(product));
    }

    public void update(ProductEntity product){
        ProductDatabase.databaseWriteExecutor.execute(()-> productDAO.update(product));
    }

    public void delete(ProductEntity product){
        ProductDatabase.databaseWriteExecutor.execute(()-> productDAO.delete(product));
    }

    public void deleteGenerated(){
        productDAO.deleteGenerated();
    }


}
