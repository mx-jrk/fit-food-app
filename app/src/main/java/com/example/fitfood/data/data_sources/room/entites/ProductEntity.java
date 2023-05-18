package com.example.fitfood.data.data_sources.room.entites;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "products_table")
public class ProductEntity {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public int count;
    public boolean selected;
    public boolean generated;
    public String type;

    public ProductEntity(@NonNull String name, int count, boolean selected,boolean generated,  String type) {
        this.name = name;
        this.count = count;
        this.selected = selected;
        this.generated = generated;
        this.type = type;
    }

    //Methods for comparing values in fields
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntity that = (ProductEntity) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}