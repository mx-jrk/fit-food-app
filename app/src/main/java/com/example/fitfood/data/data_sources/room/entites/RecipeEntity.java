package com.example.fitfood.data.data_sources.room.entites;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Entity(tableName = "recipes_table")
public class RecipeEntity {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "Title")
    public String Title;

    @ColumnInfo(name = "Description")
    public String Description;

    @NonNull
    @ColumnInfo(name = "PlansId")
    public int PlansId;

    @NonNull
    @ColumnInfo(name = "Calories")
    public int Calories;

    @NonNull
    @ColumnInfo(name = "Protein")
    public int Protein;

    @NonNull
    @ColumnInfo(name = "Fats")
    public int Fats;

    @NonNull
    @ColumnInfo(name = "Carbohydrates")
    public int Carbohydrates;

    @ColumnInfo(name = "Time")
    public String Time;

    @ColumnInfo(name = "ImageName")
    public String ImageName;

    @ColumnInfo(name = "CookingMethod")
    public String CookingMethod;

    public RecipeEntity(){
    }

    public RecipeEntity(int id, String title, String description, int plansId, int calories, int protein, int fats, int carbohydrates, String time, String imageName, String cookingMethod) {
        this.id = id;
        Title = title;
        Description = description;
        PlansId = plansId;
        Calories = calories;
        Protein = protein;
        Fats = fats;
        Carbohydrates = carbohydrates;
        Time = time;
        ImageName = imageName;
        CookingMethod = cookingMethod;
    }

    public boolean is_this_time(String time){
        String[] times = Time.split(" ");
        for (String s : times){
            if (Objects.equals(time, s)) return true;
        }
        return false;
    }
}
