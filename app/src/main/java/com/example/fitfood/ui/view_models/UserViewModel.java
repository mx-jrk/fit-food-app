package com.example.fitfood.ui.view_models;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.example.fitfood.data.DataLoadCallback;
import com.example.fitfood.data.data_sources.room.entites.PlanEntity;
import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;
import com.example.fitfood.data.data_sources.room.entites.UserEntity;
import com.example.fitfood.data.repositories.PlanRepository;
import com.example.fitfood.data.repositories.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;
import java.util.Date;
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

    public void setFirebaseFields(FirebaseAuth firebaseAuth, DatabaseReference databaseReference){
        my_user.setFirebaseFields(firebaseAuth, databaseReference);
    }

    public void resetForNewDay(){
        my_user.resetForNewDay();
    }

    //Method for loading data from a local database into a User object
    public void setUserFields(LifecycleOwner lifecycleOwner, FirebaseAuth firebaseAuth, DatabaseReference databaseReference, DataLoadCallback callback){
        getUser().observe(lifecycleOwner, user -> {
            if (user == null){
                callback.onDataLoaded();
            }
            else {
                my_user = user;
                my_user.setFirebaseFields(firebaseAuth, databaseReference);
                if (my_user.isLoadedToCloud && hasConnection(context)) downloadDataFromFirebase(()-> setUserRecipesAndPlan(lifecycleOwner, callback));
                else setUserRecipesAndPlan(lifecycleOwner, callback);
            }
        });
    }

    //Method for setting recipe and plan fields in the User object
    public void setUserRecipesAndPlan(LifecycleOwner lifecycleOwner, DataLoadCallback callback){
        getPlansById(my_user.PlanId).observe(lifecycleOwner, plan -> {
            my_user.Plan = plan;

            getRecipesByPlan(my_user.PlanId, new Date().toString().split(" ")[0]).observe(lifecycleOwner, todayRecipes -> {
                my_user.DailyRecipes = todayRecipes;

                if (my_user.LastChangeDateInt != 0 && Calendar.getInstance().get(Calendar.DAY_OF_YEAR) != my_user.LastChangeDateInt) resetForNewDay();
                callback.onDataLoaded();
            });
        });
    }

    private boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        return wifiInfo != null && wifiInfo.isConnected();
    }
}


