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
    @ColumnInfo(name = "Title")
    public String Title;

    @ColumnInfo(name = "Description")
    public String Description;

    @ColumnInfo(name = "Plan")
    public String Plan;

    @ColumnInfo(name = "Calories")
    public Integer Calories;


    @ColumnInfo(name = "Time")
    public String Time;

    @ColumnInfo(name = "ImageName")
    public String ImageName;

    public RecipeEntity(){
    }

    public RecipeEntity(String title, String description, String plan, int calories, String time, String imageName) {
        Title = title;
        Description = description;
        Plan = plan;
        Calories = calories;
        Time = time;
        ImageName = imageName;
    }

    public boolean is_this_time(String time){
        String[] times = Time.split(" ");
        for (String s : times){
            if (Objects.equals(time, s)) return true;
        }
        return false;
    }
}
