    package com.example.fitfood.data.data_sources.room.entites;

    import androidx.annotation.NonNull;
    import androidx.room.ColumnInfo;
    import androidx.room.Entity;
    import androidx.room.PrimaryKey;


    @Entity(tableName = "plan_table")
    public class PlanEntity {
        @NonNull
        @PrimaryKey
        @ColumnInfo(name = "Title")
        public String Title;

        @ColumnInfo(name = "Description")
        public String Description;

        @ColumnInfo(name = "ImageName")
        public String ImageName;

        @ColumnInfo(name = "AverageCalories")
        public String AverageCalories;

        public PlanEntity(){}

        public PlanEntity(@NonNull String title, String description, String imageName, String averageCalories) {
            Title = title;
            Description = description;
            ImageName = imageName;
            AverageCalories = averageCalories;
        }
    }
