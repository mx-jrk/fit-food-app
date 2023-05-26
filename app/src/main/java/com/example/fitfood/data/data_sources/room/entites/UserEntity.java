package com.example.fitfood.data.data_sources.room.entites;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.fitfood.data.DataLoadCallback;
import com.github.mikephil.charting.data.Entry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Entity(tableName = "user_table")
public class UserEntity {
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

    public String LastChangeDate =new Date().toString();

    public int LastChangeDateInt;

    public String Login;

    public String Password;

    public int NormalCalories;

    public String WeightHistory;

    public String EatenCaloriesHistory;

    public String FirebaseId;

    public boolean isLoadedToCloud;

    @Ignore
    public PlanEntity Plan;

    @Ignore
    public List<RecipeEntity> DailyRecipes;

    @Ignore
    List<Entry> EatenCaloriesHistoryList;

    @Ignore
    List<Entry> WeightHistoryList;

    @Ignore
    public DatabaseReference firebaseReference;

    @Ignore
    public FirebaseAuth firebaseAuth;

    @Ignore
    Context context;

    public UserEntity(){}

    public UserEntity(Context context){
        this.context = context;
    }

    //Method of assigning values to fields linked in Firebase
    public void setFirebaseFields(FirebaseAuth firebaseAuth, DatabaseReference databaseReference){
        this.firebaseReference = databaseReference;
        this.firebaseAuth = firebaseAuth;
    }

    //Method of loading data from Firebase using CallBack
    public void downloadDataFromFirebase(DataLoadCallback callback){
       // System.out.println(firebaseReference == null);
        firebaseReference
                .child("Users").
                child(FirebaseId).
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Name = Objects.requireNonNull(snapshot.child("Name").getValue()).toString();
                Height = Integer.parseInt(Objects.requireNonNull(snapshot.child("Height").getValue()).toString());
                Weight = Double.parseDouble(Objects.requireNonNull(snapshot.child("Weight").getValue()).toString());
                Goal = Objects.requireNonNull(snapshot.child("Goal").getValue()).toString();
                WeightGoal = Double.parseDouble(Objects.requireNonNull(snapshot.child("WeightGoal").getValue()).toString());
                Contraindications_s = Objects.requireNonNull(snapshot.child("Contraindications_s").getValue()).toString();
                PlanId = Integer.parseInt(Objects.requireNonNull(snapshot.child("PlanId").getValue()).toString());
                EatenCalories = Integer.parseInt(Objects.requireNonNull(snapshot.child("EatenCalories").getValue()).toString());
                BreakfastEaten = Boolean.parseBoolean(Objects.requireNonNull(snapshot.child("BreakfastEaten").getValue()).toString());
                LunchEaten = Boolean.parseBoolean(Objects.requireNonNull(snapshot.child("LunchEaten").getValue()).toString());
                DinnerEaten = Boolean.parseBoolean(Objects.requireNonNull(snapshot.child("DinnerEaten").getValue()).toString());
                SnackEaten = Boolean.parseBoolean(Objects.requireNonNull(snapshot.child("SnackEaten").getValue()).toString());
                LastChangeDate = Objects.requireNonNull(snapshot.child("LastChangeDate").getValue()).toString();
                LastChangeDateInt = Integer.parseInt(Objects.requireNonNull(snapshot.child("LastChangeDateInt").getValue()).toString());
                Login = Objects.requireNonNull(snapshot.child("Login").getValue()).toString();
                Password = Objects.requireNonNull(snapshot.child("Password").getValue()).toString();
                NormalCalories = Integer.parseInt(Objects.requireNonNull(snapshot.child("NormalCalories").getValue()).toString());
                if (snapshot.child("WeightHistory").getValue() != null)  WeightHistory = Objects.requireNonNull(snapshot.child("WeightHistory").getValue()).toString();
                if (snapshot.child("EatenCaloriesHistory").getValue() != null) EatenCaloriesHistory = Objects.requireNonNull(snapshot.child("EatenCaloriesHistory").getValue()).toString();
                callback.onDataLoaded();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Не удалось загрузить данные из базы. Будут использованы локальные данные", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Method of uploading data to Firebase
    public void uploadDataToFirebase(){
        firebaseReference.child("Users").child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).child("Name").setValue(Name);
        firebaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Height").setValue(Height);
        firebaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Weight").setValue(Weight);
        firebaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Goal").setValue(Goal);
        firebaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("WeightGoal").setValue(WeightGoal);
        firebaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Contraindications_s").setValue(Contraindications_s);
        firebaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("PlanId").setValue(PlanId);
        firebaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("EatenCalories").setValue(EatenCalories);
        firebaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("BreakfastEaten").setValue(BreakfastEaten);
        firebaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("LunchEaten").setValue(LunchEaten);
        firebaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("DinnerEaten").setValue(DinnerEaten);
        firebaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("SnackEaten").setValue(SnackEaten);
        firebaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("LastChangeDate").setValue(LastChangeDate);
        firebaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("LastChangeDateInt").setValue(LastChangeDateInt);
        firebaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Login").setValue(Login);
        firebaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Password").setValue(Password);
        firebaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("NormalCalories").setValue(NormalCalories);
        firebaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("WeightHistory").setValue(WeightHistory);
        firebaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("EatenCaloriesHistory").setValue(EatenCaloriesHistory);
        firebaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("FirebaseId").setValue(FirebaseId);
    }

    //Method of determining suitable meal plans using survey data
    public List<PlanEntity> choosePlans(List<PlanEntity> plans){
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

    //The method of counting calories eaten per day
    public int dailyCalories(){
        int calories = 0;
        for (RecipeEntity dish: DailyRecipes){
            calories += dish.Calories;
        }
        return calories;
    }

    //The method of counting the meals eaten during the day
    public int setEatenDishesCount(){
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

    //Method of resetting values when a new day comes
    public void resetForNewDay(){
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
        EatenCalories = 0;
        BreakfastEaten = false;
        LunchEaten = false;
        DinnerEaten = false;
        SnackEaten = false;
    }

    //Method of obtaining the history of calories eaten as List
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

    //Method of obtaining the history of weight eaten as List
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
