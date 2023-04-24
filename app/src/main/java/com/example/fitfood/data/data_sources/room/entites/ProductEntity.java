package com.example.fitfood.data.data_sources.room.entites;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "products_table")
public class ProductEntity {
    @NonNull
    @PrimaryKey
    public String name;
    public int count;
    public boolean selected;
    public boolean generated;

    public ProductEntity(@NonNull String name, int count, boolean selected,boolean generated) {
        this.name = name;
        this.count = count;
        this.selected = selected;
        this.generated = generated;
    }

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