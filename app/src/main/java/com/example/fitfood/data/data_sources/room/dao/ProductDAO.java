package com.example.fitfood.data.data_sources.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitfood.data.data_sources.room.entites.ProductEntity;

import java.util.List;

@Dao
public interface ProductDAO {
    @Insert
    void insert(ProductEntity product);

    @Delete
    void delete(ProductEntity product);

    @Update
    void update(ProductEntity product);

    @Query("SELECT * FROM products_table WHERE type = :type ORDER BY selected ASC")
    LiveData<List<ProductEntity>> getProductsByType(String type);

    @Query("DELETE FROM products_table WHERE generated = 1")
    void deleteGenerated();
}
