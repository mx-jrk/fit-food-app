package com.example.fitfood.data.data_sources.room.entites;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.fitfood.data.repositories.PlanRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    public String Contraindications_s;

    public int PlanId;

    public int EatenCalories = 0;

    public boolean BreakfastEaten = false;

    public boolean LunchEaten = false;

    public boolean DinnerEaten = false;

    public boolean SnackEaten = false;

    public int PlanCycle = 1;

    public String LastChangeDate =new Date().toString();

    public String Login;

    public String Password;

    public int NormalCalories;

    @Ignore
    public String[] Contraindications;

    @Ignore
    public PlanEntity Plan;

    @Ignore
    public List<RecipeEntity> DailyRecipes;

    public UserEntity(){}


    public List<PlanEntity> choose_plans(List<PlanEntity> plans){
        Contraindications = Contraindications_s.split("\n");

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

}
