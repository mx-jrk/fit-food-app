package com.example.fitfood.ui.view_models;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fitfood.data.DataLoadCallback;
import com.example.fitfood.data.data_sources.room.entites.PlanEntity;
import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;
import com.example.fitfood.data.data_sources.room.entites.UserEntity;
import com.example.fitfood.data.repositories.PlanRepository;
import com.example.fitfood.data.repositories.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel  {
    @SuppressLint("StaticFieldLeak")
    private final Context context;

    private final UserRepository userRepository;
    private final LiveData<UserEntity> user;
    public UserEntity my_user;

    public PlanRepository planRepository;
    private final LiveData<List<PlanEntity>> plans;
    public LiveData<PlanEntity> plansById;


    public UserViewModel(Application application){
        super(application);
        context = application.getApplicationContext();

        userRepository = new UserRepository(application);
        planRepository = new PlanRepository(application);

        user = userRepository.getUser();

        plans = planRepository.getAllPlans();

        my_user = new UserEntity(context);
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

    public void uploadDataToFirebase(){
        my_user.uploadDataToFirebase();
    }

    public void downloadDataFromFirebase(DataLoadCallback callback){
       my_user.downloadDataFromFirebase(callback);
    }
}


