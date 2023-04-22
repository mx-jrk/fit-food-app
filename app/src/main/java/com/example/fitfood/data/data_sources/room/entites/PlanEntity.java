    package com.example.fitfood.data.data_sources.room.entites;

    import androidx.annotation.NonNull;
    import androidx.room.ColumnInfo;
    import androidx.room.Entity;
    import androidx.room.PrimaryKey;

    import org.jetbrains.annotations.NotNull;


    @Entity(tableName = "plan_table")
    public class PlanEntity {
        @NonNull
        @PrimaryKey
        @ColumnInfo(name = "id")
        public int id;

        @ColumnInfo(name = "Title")
        public String Title;

        @ColumnInfo(name = "Description")
        public String Description;

        @ColumnInfo(name = "ImageName")
        public String ImageName;

        @NonNull
        @ColumnInfo(name = "AverageCalories")
        public int AverageCalories;

        @ColumnInfo(name = "GoalOfPlan")
        public String GoalOfPlan;

        @ColumnInfo(name = "Contraindications")
        public String Contraindications;

        public PlanEntity(){}

        public PlanEntity(int id, String title, String description, String imageName, int averageCalories, String goalOfPlan, String contraindications) {
            this.id = id;
            Title = title;
            Description = description;
            ImageName = imageName;
            AverageCalories = averageCalories;
            GoalOfPlan = goalOfPlan;
            Contraindications = contraindications;
        }
    }
