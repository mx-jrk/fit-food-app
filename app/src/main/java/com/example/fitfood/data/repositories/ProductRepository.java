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
    private final LiveData<List<ProductEntity>> allProducts;

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
        allProducts = productDAO.getAllProducts();
    }

    public LiveData<List<ProductEntity>> getAllProducts(){
        return allProducts;
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


}
