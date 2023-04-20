package com.example.fitfood.data.data_sources.room.entites;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "user_table")
public class UserEntity {
    @NonNull
    @PrimaryKey
    public int id;

    public String Name;

    public int Height;

    public int Weight;

    public String Goal;

    public int WeightGoal;

    public String ActivityLevel;

    public String TrainingFrequency;

    public String Contraindications_s;

    public String Preferences_s;

    public String FoodStyle;

    public String Plan;

    public int EatenCalories = 0;

    public boolean BreakfastEaten = false;

    public boolean LunchEaten = false;

    public boolean DinnerEaten = false;

    public boolean SnackEaten = false;

    public int PlanCycle = 1;

    public String LastChangeDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    public String Login;

    public String Password;

    @Ignore
    public String[] Contraindications;

    @Ignore
    public String[] Preferences;

    public UserEntity(){}

    public UserEntity(int id, String name, int height, int weight, String goal, int weightGoal,
                      String activityLevel, String trainingFrequency, String contraindications_s,
                      String preferences_s, String foodStyle, String plan, int eatenCalories,
                      boolean breakfastEaten, boolean lunchEaten, boolean dinnerEaten,
                      boolean snackEaten, int planCycle, String lastChangeDate,
                      String login, String password) {
        this.id = id;
        Name = name;
        Height = height;
        Weight = weight;
        Goal = goal;
        WeightGoal = weightGoal;
        ActivityLevel = activityLevel;
        TrainingFrequency = trainingFrequency;
        Contraindications_s = contraindications_s;
        Preferences_s = preferences_s;
        FoodStyle = foodStyle;
        Plan = plan;
        EatenCalories = eatenCalories;
        BreakfastEaten = breakfastEaten;
        LunchEaten = lunchEaten;
        DinnerEaten = dinnerEaten;
        SnackEaten = snackEaten;
        PlanCycle = planCycle;
        LastChangeDate = lastChangeDate;
        Login = login;
        Password = password;

        Contraindications = Contraindications_s.split(" ");
        Preferences = Preferences_s.split(" ");
    }
}
