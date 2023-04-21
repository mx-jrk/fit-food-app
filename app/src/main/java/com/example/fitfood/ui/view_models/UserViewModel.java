package com.example.fitfood.ui.view_models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.fitfood.data.data_sources.room.entites.UserEntity;
import com.example.fitfood.data.repositories.UserRepository;

public class UserViewModel extends AndroidViewModel  {
    private UserRepository userRepository;
    private LiveData<UserEntity> user;
    public UserEntity my_user;

    public UserViewModel(Application application){
        super(application);
        userRepository = new UserRepository(application);
        user = userRepository.getUser();
        my_user = new UserEntity();
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

}
