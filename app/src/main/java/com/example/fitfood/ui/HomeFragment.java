package com.example.fitfood.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fitfood.R;
import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;
import com.example.fitfood.data.data_sources.room.entites.UserEntity;
import com.example.fitfood.databinding.FragmentHomeBinding;
import com.example.fitfood.ui.PlanChoosing.PlanCardFragment;
import com.example.fitfood.ui.PlanChoosing.PlanChoosingFragment;
import com.example.fitfood.ui.Survey.WeightQuestionFragment;
import com.example.fitfood.ui.view_models.HomeViewModel;
import com.example.fitfood.ui.view_models.UserViewModel;

import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    HomeViewModel homeViewModel;
    NavHostFragment navHostFragment;
    NavController navController;
    UserViewModel userViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        List<RecipeEntity>  recipeEntities = userViewModel.my_user.DailyRecipes;


        binding.breakfastTitle.setText(recipeEntities.get(0).Title);
        binding.breakfastDescription.setText(recipeEntities.get(0).Description);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.breakfastImage.setImageResource(getResources().getIdentifier(recipeEntities.get(0).ImageName,  "drawable", view.getContext().getOpPackageName()));
        }

        binding.lunchTitle.setText(recipeEntities.get(1).Title);
        binding.lunchDescription.setText(recipeEntities.get(1).Description);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.lunchImage.setImageResource(getResources().getIdentifier(recipeEntities.get(1).ImageName,  "drawable", view.getContext().getOpPackageName()));
        }

        binding.dinnerTitle.setText(recipeEntities.get(2).Title);
        binding.dinnerDescription.setText(recipeEntities.get(2).Description);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.dinnerImage.setImageResource(getResources().getIdentifier(recipeEntities.get(2).ImageName,  "drawable", view.getContext().getOpPackageName()));
        }

        binding.snackTile.setText(recipeEntities.get(3).Title);
        binding.snackDescription.setText(recipeEntities.get(3).Description);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.snackImage.setImageResource(getResources().getIdentifier(recipeEntities.get(3).ImageName,  "drawable", view.getContext().getOpPackageName()));
        }

        binding.changePlanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_homeFragment_to_planChoosingFragment);
            }
        });



        binding.watchPlanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("plan", userViewModel.my_user.PlanId);
                bundle.putBoolean("is_first", false);
                PlanCardFragment planCardFragment = new PlanCardFragment();
                planCardFragment.setArguments(bundle);
                navController.navigate(R.id.action_homeFragment_to_planCardFragment, bundle);
            }
        });

        binding.changePlanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("is_first", false);
                PlanChoosingFragment planChoosingFragment = new PlanChoosingFragment();
                planChoosingFragment.setArguments(bundle);
                navController.navigate(R.id.action_homeFragment_to_planChoosingFragment, bundle);
            }
        });


        binding.changeWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("source", "home");
                WeightQuestionFragment weightQuestionFragment = new WeightQuestionFragment();
                weightQuestionFragment.setArguments(bundle);
                navController.navigate(R.id.action_homeFragment_to_weightQuestionFragment, bundle);
            }
        });


        setEatenDishes();
        setEatenCalories();
        setButtons();

        if( (userViewModel.my_user.Goal.contains("Сушка") || userViewModel.my_user.Goal.contains("Похудение"))
                && (userViewModel.my_user.Weight <= userViewModel.my_user.WeightGoal)) binding.weightTv.setText("Цель достигнута!");
        else  if  (userViewModel.my_user.Goal.contains("Набор массы")
                && (userViewModel.my_user.Weight >= userViewModel.my_user.WeightGoal)) binding.weightTv.setText("Цель достигнута!");
        else  binding.weightTv.setText("Осталось: " + String.format("%.2f",Math.abs(userViewModel.my_user.WeightGoal - userViewModel.my_user.Weight)));
        binding.weightPb.setMax((int) Math.round(Math.max(userViewModel.my_user.WeightGoal, userViewModel.my_user.Weight)));
        binding.weightPb.setProgress((int) Math.round(Math.min(userViewModel.my_user.WeightGoal, userViewModel.my_user.Weight)));

        binding.breakfastEatenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userViewModel.my_user.BreakfastEaten){
                    userViewModel.my_user.BreakfastEaten = false;
                    userViewModel.my_user.EatenCalories -= userViewModel.my_user.DailyRecipes.get(0).Calories;
                    binding.breakfastEatenBtn.setText("Записать приём пищи");
                    binding.breakfastEatenBtn.setBackground(getResources().getDrawable(R.drawable.roundstyle));
                }
                else {
                    userViewModel.my_user.BreakfastEaten = true;
                    userViewModel.my_user.EatenCalories += userViewModel.my_user.DailyRecipes.get(0).Calories;
                    binding.breakfastEatenBtn.setText("Приём пищи записан");
                    binding.breakfastEatenBtn.setBackground(getResources().getDrawable(R.drawable.roundstyle_blue));
                }
                setEatenCalories();
                setEatenDishes();
            }
        });

        binding.lunchEatenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userViewModel.my_user.LunchEaten){
                    userViewModel.my_user.LunchEaten = false;
                    userViewModel.my_user.EatenCalories -= userViewModel.my_user.DailyRecipes.get(1).Calories;
                    binding.lunchEatenBtn.setText("Записать приём пищи");
                    binding.lunchEatenBtn.setBackground(getResources().getDrawable(R.drawable.roundstyle));
                }
                else {
                    userViewModel.my_user.LunchEaten = true;
                    userViewModel.my_user.EatenCalories += userViewModel.my_user.DailyRecipes.get(1).Calories;
                    binding.lunchEatenBtn.setText("Приём пищи записан");
                    binding.lunchEatenBtn.setBackground(getResources().getDrawable(R.drawable.roundstyle_blue));
                }
                setEatenDishes();
                setEatenCalories();
            }
        });

        binding.dinnerEatenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userViewModel.my_user.DinnerEaten){
                    userViewModel.my_user.DinnerEaten = false;
                    userViewModel.my_user.EatenCalories -= userViewModel.my_user.DailyRecipes.get(2).Calories;
                    binding.dinnerEatenBtn.setText("Записать приём пищи");
                    binding.dinnerEatenBtn.setBackground(getResources().getDrawable(R.drawable.roundstyle));
                }
                else{
                    userViewModel.my_user.DinnerEaten = true;
                    userViewModel.my_user.EatenCalories += userViewModel.my_user.DailyRecipes.get(2).Calories;
                    binding.dinnerEatenBtn.setText("Приём пищи записан");
                    binding.dinnerEatenBtn.setBackground(getResources().getDrawable(R.drawable.roundstyle_blue));
                }
                setEatenDishes();
                setEatenCalories();
            }
        });

        binding.snackEatenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userViewModel.my_user.SnackEaten){
                    userViewModel.my_user.SnackEaten = false;
                    userViewModel.my_user.EatenCalories -= userViewModel.my_user.DailyRecipes.get(3).Calories;
                    binding.snackEatenBtn.setText("Записать приём пищи");
                    binding.snackEatenBtn.setBackground(getResources().getDrawable(R.drawable.roundstyle));
                }
                else {
                    userViewModel.my_user.SnackEaten = true;
                    userViewModel.my_user.EatenCalories += userViewModel.my_user.DailyRecipes.get(3).Calories;
                    binding.snackEatenBtn.setText("Приём пищи записан");
                    binding.snackEatenBtn.setBackground(getResources().getDrawable(R.drawable.roundstyle_blue));
                }
                setEatenDishes();
                setEatenCalories();
            }
        });

        binding.watchBreakfastRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRecipeCard(0);
            }
        });

        binding.watchLunchRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRecipeCard(1);
            }
        });

        binding.watchDinnerRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRecipeCard(2);
            }
        });

        binding.watchSnackRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRecipeCard(3);
            }
        });


    }

    private void setButtons(){
        if (userViewModel.my_user.BreakfastEaten) {
            binding.breakfastEatenBtn.setText("Приём пищи записан");
            binding.breakfastEatenBtn.setBackground(getResources().getDrawable(R.drawable.roundstyle_blue));
        }
        if (userViewModel.my_user.LunchEaten) {
            binding.lunchEatenBtn.setText("Приём пищи записан");
            binding.lunchEatenBtn.setBackground(getResources().getDrawable(R.drawable.roundstyle_blue));
        }
        if (userViewModel.my_user.DinnerEaten) {
            binding.dinnerEatenBtn.setText("Приём пищи записан");
            binding.dinnerEatenBtn.setBackground(getResources().getDrawable(R.drawable.roundstyle_blue));
        }
        if (userViewModel.my_user.SnackEaten) {
            binding.snackEatenBtn.setText("Приём пищи записан");
            binding.snackEatenBtn.setBackground(getResources().getDrawable(R.drawable.roundstyle_blue));
        }
    }

    private void goToRecipeCard(int time){
        Bundle bundle = new Bundle();
        bundle.putInt("time", time);
        RecipeCardFragment weightQuestionFragment = new RecipeCardFragment();
        weightQuestionFragment.setArguments(bundle);
        navController.navigate(R.id.action_homeFragment_to_recipeCardFragment, bundle);
    }

    private void setEatenDishes(){
        binding.eatenTv.setText(userViewModel.my_user.dishesEaten() + " из " + userViewModel.my_user.DailyRecipes.size());
        binding.eatenPb.setMax(userViewModel.my_user.DailyRecipes.size());
        binding.eatenPb.setProgress(userViewModel.my_user.dishesEaten());
    }

    private void setEatenCalories(){
        binding.caloriesTv.setText(userViewModel.my_user.EatenCalories + " из " + userViewModel.my_user.dailyCalories());
        binding.caloriesPb.setMax(userViewModel.my_user.dailyCalories());
        binding.caloriesPb.setProgress(userViewModel.my_user.EatenCalories);
    }
}