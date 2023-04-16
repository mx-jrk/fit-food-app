package com.example.fitfood.ui.view_models;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.fitfood.data.data_sources.room.entites.ProductEntity;
import com.example.fitfood.data.repositories.ProductRepository;
import java.util.List;

public class ShoppingListViewModel extends AndroidViewModel {
    private final LiveData<List<ProductEntity>> allProducts;
    private ProductRepository productRepository;

    public ShoppingListViewModel(Application application) {
        super(application);
        ProductRepository productRepository = new ProductRepository(application);
        this.productRepository = productRepository;
        this.allProducts = productRepository.getAllProducts();
    }

    public LiveData<List<ProductEntity>> getAllProducts() {
        return this.allProducts;
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
}