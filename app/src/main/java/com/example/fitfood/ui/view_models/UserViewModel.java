package com.example.fitfood.ui.view_models;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.fitfood.data.DataLoadCallback;
import com.example.fitfood.data.data_sources.room.entites.PlanEntity;
import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;
import com.example.fitfood.data.data_sources.room.entites.UserEntity;
import com.example.fitfood.data.repositories.PlanRepository;
import com.example.fitfood.data.repositories.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserViewModel extends AndroidViewModel  {
    private Context context;
    private UserRepository userRepository;
    private LiveData<UserEntity> user;
    private FirebaseDatabase firestoreDataBase;
    private DatabaseReference firestoreReference;
    private FirebaseAuth firebaseAuth;
    public PlanRepository planRepository;
    private LiveData<List<PlanEntity>> plans;
    public LiveData<List<RecipeEntity>> recipes;
    public LiveData<PlanEntity> plansById;
    public UserEntity my_user;
    public boolean isLoaded = false;

    public UserViewModel(Application application){
        super(application);
        userRepository = new UserRepository(application);
        planRepository = new PlanRepository(application);

        user = userRepository.getUser();
        plans = planRepository.getAllPlans();
        my_user = new UserEntity();


        firestoreDataBase = FirebaseDatabase.getInstance();
        firestoreReference = firestoreDataBase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        context = application.getApplicationContext();
    }



    public LiveData<List<PlanEntity>> getAllPlans(){
        return plans;
    }

    public void insert(){
        userRepository.insert(my_user);
    }

    public void update(){
        userRepository.update(my_user);
    }

    public void delete(UserEntity user){
        userRepository.delete(user);
    }

    public LiveData<UserEntity> getUser(){
        return user;
    }

    public LiveData<PlanEntity> getPlansById(int id){
        plansById = planRepository.getPlansById(id);
        return plansById;
    }

    public LiveData<List<RecipeEntity>> getRecipesByPlan(UserEntity user){
        return planRepository.getRecipesByPlan(user);
    }

    public LiveData<List<RecipeEntity>> getRecipesByPlan(int planId, String day){
        return planRepository.getRecipesByPlan(planId, day);
    }

    public void loadDataToFirebaseCloud(){
        System.out.println(firebaseAuth.getCurrentUser().getUid());
        firestoreReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Name").setValue(my_user.Name);
        firestoreReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Height").setValue(my_user.Height);
        firestoreReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Weight").setValue(my_user.Weight);
        firestoreReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Goal").setValue(my_user.Goal);
        firestoreReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("WeightGoal").setValue(my_user.WeightGoal);
        firestoreReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Contraindications_s").setValue(my_user.Contraindications_s);
        firestoreReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("PlanId").setValue(my_user.PlanId);
        firestoreReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("EatenCalories").setValue(my_user.EatenCalories);
        firestoreReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("BreakfastEaten").setValue(my_user.BreakfastEaten);
        firestoreReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("LunchEaten").setValue(my_user.LunchEaten);
        firestoreReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("DinnerEaten").setValue(my_user.DinnerEaten);
        firestoreReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("SnackEaten").setValue(my_user.SnackEaten);
        firestoreReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("LastChangeDate").setValue(my_user.LastChangeDate);
        firestoreReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("LastChangeDateInt").setValue(my_user.LastChangeDateInt);
        firestoreReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Login").setValue(my_user.Login);
        firestoreReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Password").setValue(my_user.Password);
        firestoreReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("NormalCalories").setValue(my_user.NormalCalories);
        firestoreReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("WeightHistory").setValue(my_user.WeightHistory);
        firestoreReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("EatenCaloriesHistory").setValue(my_user.EatenCaloriesHistory);
        firestoreReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("FirebaseId").setValue(my_user.FirebaseId);
        isLoaded = true;
    }

    public void uploadDataToFirebaseCloud(DataLoadCallback callback){
        System.out.println(firebaseAuth.getCurrentUser().getUid());
        firestoreReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                my_user.Name = snapshot.child("Name").getValue().toString();
                my_user.Height = Integer.parseInt(snapshot.child("Height").getValue().toString());
                my_user.Weight = Double.parseDouble(snapshot.child("Weight").getValue().toString());
                my_user.Goal = snapshot.child("Goal").getValue().toString();
                my_user.WeightGoal = Double.parseDouble(snapshot.child("WeightGoal").getValue().toString());
                my_user.Contraindications_s = snapshot.child("Contraindications_s").getValue().toString();
                my_user.PlanId = Integer.parseInt(snapshot.child("PlanId").getValue().toString());
                my_user.EatenCalories = Integer.parseInt(snapshot.child("EatenCalories").getValue().toString());
                my_user.BreakfastEaten = Boolean.parseBoolean(snapshot.child("BreakfastEaten").getValue().toString());
                my_user.LunchEaten = Boolean.parseBoolean(snapshot.child("LunchEaten").getValue().toString());
                my_user.DinnerEaten = Boolean.parseBoolean(snapshot.child("DinnerEaten").getValue().toString());
                my_user.SnackEaten = Boolean.parseBoolean(snapshot.child("SnackEaten").getValue().toString());
                my_user.LastChangeDate = snapshot.child("LastChangeDate").getValue().toString();
                my_user.LastChangeDateInt = Integer.parseInt(snapshot.child("LastChangeDateInt").getValue().toString());
                my_user.Login = snapshot.child("Login").getValue().toString();
                my_user.Password = snapshot.child("Password").getValue().toString();
                my_user.NormalCalories = Integer.parseInt(snapshot.child("NormalCalories").getValue().toString());
                if (snapshot.child("WeightHistory").getValue() != null)  my_user.WeightHistory = snapshot.child("WeightHistory").getValue().toString();
                if (snapshot.child("EatenCaloriesHistory").getValue() != null) my_user.EatenCaloriesHistory = snapshot.child("EatenCaloriesHistory").getValue().toString();
                callback.onDataLoaded();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Не удалось загрузить данные из базы. Будут использованы локальные данные", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


