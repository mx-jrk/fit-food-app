package com.example.fitfood.data.data_sources.room.entites;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products_table")
public class ProductEntity {
    @NonNull
    @PrimaryKey
    public String name;
    public int count;
    public boolean selected;

    public ProductEntity(@NonNull String name, int count, boolean selected) {
        this.name = name;
        this.count = count;
        this.selected = selected;
    }
}