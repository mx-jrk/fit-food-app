package com.example.fitfood.data.data_sources.room.entites;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.fitfood.data.repositories.PlanRepository;
import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Entity(tableName = "user_table")
public class UserEntity {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String Name;

    public int Height;

    public double Weight;

    public String Goal;

    public double WeightGoal;

    public String Contraindications_s;

    public int PlanId;

    public int EatenCalories = 0;

    public boolean BreakfastEaten = false;

    public boolean LunchEaten = false;

    public boolean DinnerEaten = false;

    public boolean SnackEaten = false;

    public int PlanCycle = 1;

    public String LastChangeDate =new Date().toString();

    public int LastChangeDateInt;

    public String Login;

    public String Password;

    public int NormalCalories;

    public String WeightHistory;

    public String EatenCaloriesHistory;

    public String FirebaseId;

    @Ignore
    public PlanEntity Plan;

    @Ignore
    public List<RecipeEntity> DailyRecipes;

    @Ignore
    List<Entry> EatenCaloriesHistoryList;

    @Ignore
    List<Entry> WeightHistoryList;


    public UserEntity(){}


    public List<PlanEntity> choose_plans(List<PlanEntity> plans){
        String[] Contraindications = Contraindications_s.split("\n");

        if (Contraindications.length == 0) return plans;

        List<PlanEntity> good_plans = new ArrayList<>();
        String[] planContraindications;
        int countContraindications;

        for (PlanEntity plan : plans){
            if (!plan.GoalOfPlan.contains(Goal.toLowerCase(Locale.ROOT))) continue;
            if (Goal.contains("Набор массы") && plan.AverageCalories < (NormalCalories + 500)) continue;
            if ((Goal.contains("Сушка") || Goal.contains("Похудение")) && plan.AverageCalories > (NormalCalories - 500)) continue;
            if (Goal.contains("Поддержание массы") && Math.abs(NormalCalories - plan.AverageCalories) > 500) continue;

            countContraindications = 0;
            if (plan.Contraindications == null){
                good_plans.add(plan);
                continue;
            }
            planContraindications = plan.Contraindications.split("\n");

            for (String contraindication : Contraindications){
                if (isValueInArray(contraindication, planContraindications)) countContraindications++;
            }

            if (countContraindications <= 2) good_plans.add(plan);
        }

        return good_plans;
    }

    public int dailyCalories(){
        int calories = 0;
        for (RecipeEntity dish: DailyRecipes){
            calories += dish.Calories;
        }
        return calories;
    }

    public int dishesEaten(){
        int count = 0;
        if (BreakfastEaten) count++;
        if (LunchEaten) count++;
        if (DinnerEaten) count++;
        if (SnackEaten) count++;
        return count;
    }

    private boolean isValueInArray(String value, String[] array){
        for (String v: array){
            if (v.contains(value)) return true;
        }
        return false;
    }

    public void resetForNewDay(List<RecipeEntity> recipeEntities){
        if (WeightHistory == null) WeightHistory = String.valueOf(Weight);
        else {
            String[] weightHistoryArr = WeightHistory.split(" ");
            if (weightHistoryArr.length >= 8){

                StringBuilder newWeightHistory = new StringBuilder(weightHistoryArr[weightHistoryArr.length - 7]);
                for (int i = weightHistoryArr.length - 6; i < weightHistoryArr.length; i++) newWeightHistory.append(" ").append(weightHistoryArr[i]);
                WeightHistory = newWeightHistory.toString();
            }
            else {
                WeightHistory += " " + Weight;
            }
        }
        if (EatenCaloriesHistory == null) EatenCaloriesHistory = String.valueOf(EatenCalories);
        else {
            String[] caloriesHistoryArr = EatenCaloriesHistory.split(" ");
            if (caloriesHistoryArr.length >= 8){
                StringBuilder newCaloriesHostiry = new StringBuilder(caloriesHistoryArr[caloriesHistoryArr.length - 7]);
                for (int i = caloriesHistoryArr.length - 6; i < caloriesHistoryArr.length; i++) newCaloriesHostiry.append(" ").append(caloriesHistoryArr[i]);
                EatenCaloriesHistory = newCaloriesHostiry.toString();
            }
            else {
                EatenCaloriesHistory += " " + EatenCalories;
            }
        }
        DailyRecipes = recipeEntities;
        EatenCalories = 0;
        BreakfastEaten = false;
        LunchEaten = false;
        DinnerEaten = false;
        SnackEaten = false;
    }

    public List<Entry> getEatenCaloriesHistoryAsList() {
        if(EatenCaloriesHistory == null) return new ArrayList<>();
        if (EatenCaloriesHistoryList == null) {
            EatenCaloriesHistoryList = new ArrayList<>();
            String[] history = EatenCaloriesHistory.split(" ");
            for (int i = 0; i < history.length; i++) {
                EatenCaloriesHistoryList.add(new Entry(i + 1, Integer.parseInt(history[i])));
            }
           // EatenCaloriesHistoryList.add(new Entry(history.length, EatenCalories));
        }
        System.out.println(EatenCaloriesHistory);
        return EatenCaloriesHistoryList;
    }

    public List<Entry> getWeightHistoryAsList() {
        if(WeightHistory == null) return new ArrayList<>();
        if (WeightHistoryList == null) {
            WeightHistoryList = new ArrayList<>();
            String[] history = WeightHistory.split(" ");
            for (int i = 0; i < history.length; i++) {
                WeightHistoryList.add(new Entry(i + 1, Float.parseFloat(history[i])));
            }
        }
        System.out.println(WeightHistoryList.size());
        return WeightHistoryList;
    }

}
