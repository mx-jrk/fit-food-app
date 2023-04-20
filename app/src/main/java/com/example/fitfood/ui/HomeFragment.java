package com.example.fitfood.ui;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitfood.R;
import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;
import com.example.fitfood.databinding.FragmentHomeBinding;
import com.example.fitfood.ui.view_models.HomeViewModel;

import java.util.List;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    HomeViewModel homeViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getAllRecipes().observe(getViewLifecycleOwner(), new Observer<List<RecipeEntity>>() {
            @Override
            public void onChanged(List<RecipeEntity> recipeEntities) {
                RecipeEntity dailyRation;
                for (int i = 0; i < recipeEntities.size(); i++){
                    dailyRation = recipeEntities.get(i);
                    if (dailyRation.is_this_time("Завтрак")){
                        binding.breakfastTitle.setText(dailyRation.Title);
                        binding.breakfastDescription.setText(dailyRation.Description);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            binding.breakfastImage.setImageResource(getResources().getIdentifier(dailyRation.ImageName, "drawable", view.getContext().getOpPackageName()));
                        }
                        continue;
                    }
                    else if (dailyRation.is_this_time("Обед")){
                        binding.lunchTitle.setText(dailyRation.Title);
                        binding.lunchDescription.setText(dailyRation.Description);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            binding.lunchImage.setImageResource(getResources().getIdentifier(dailyRation.ImageName, "drawable", view.getContext().getOpPackageName()));
                        }
                        continue;
                    }
                    else if (dailyRation.is_this_time("Ужин")){
                        binding.dinnerTitle.setText(dailyRation.Title);
                        binding.dinnerDescription.setText(dailyRation.Description);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            binding.dinnerImage.setImageResource(getResources().getIdentifier(dailyRation.ImageName, "drawable", view.getContext().getOpPackageName()));
                        }
                        continue;
                    }

                }
            }
        });
    }
}