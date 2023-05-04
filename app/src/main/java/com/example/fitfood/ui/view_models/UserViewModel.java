package com.example.fitfood.ui.view_models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.fitfood.data.data_sources.room.entites.PlanEntity;
import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;
import com.example.fitfood.data.data_sources.room.entites.UserEntity;
import com.example.fitfood.data.repositories.PlanRepository;
import com.example.fitfood.data.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserViewModel extends AndroidViewModel  {
    private UserRepository userRepository;
    private LiveData<UserEntity> user;
    public PlanRepository planRepository;
    private LiveData<List<PlanEntity>> plans;
    public LiveData<List<RecipeEntity>> recipes;
    public LiveData<PlanEntity> plansById;
    public UserEntity my_user;

    public UserViewModel(Application application){
        super(application);
        userRepository = new UserRepository(application);
        planRepository = new PlanRepository(application);

        user = userRepository.getUser();
        plans = planRepository.getAllPlans();
        my_user = new UserEntity();
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

}


