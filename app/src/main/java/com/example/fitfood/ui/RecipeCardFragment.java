package com.example.fitfood.ui;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitfood.R;
import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;
import com.example.fitfood.databinding.FragmentRecipeCardBinding;
import com.example.fitfood.ui.view_models.UserViewModel;

public class RecipeCardFragment extends Fragment {

    FragmentRecipeCardBinding binding;

    NavHostFragment navHostFragment;
    NavController navController;

    UserViewModel userViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipeCardBinding.inflate(inflater, container, false);

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);


        return binding.getRoot();
    }

    @SuppressLint("DiscouragedApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        RecipeEntity recipe = userViewModel.my_user.DailyRecipes.get(getArguments().getInt("time"));

        binding.recipeTitle.setText(recipe.Title);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.recipeImage.setImageResource(getResources().getIdentifier(recipe.ImageName, "drawable", view.getContext().getOpPackageName()));
        }

        binding.recipeDescription.setText(recipe.Description);

        binding.recipeCalories.setText(String.valueOf(recipe.Calories));

        binding.recipeProtein.setText(String.valueOf(recipe.Protein));

        binding.recipeFats.setText(String.valueOf(recipe.Fats));

        binding.recipeCarbohydrates.setText(String.valueOf(recipe.Carbohydrates));

        binding.recipe.setText(recipe.CookingMethod);

        binding.eatenBtn.setOnClickListener(view1 -> {
            switch (getArguments().getInt("time")){
                case 0:
                    if (!userViewModel.my_user.BreakfastEaten) userViewModel.my_user.EatenCalories += recipe.Calories;
                    userViewModel.my_user.BreakfastEaten = true;
                    break;

                case 1:
                    if (!userViewModel.my_user.LunchEaten) userViewModel.my_user.EatenCalories += recipe.Calories;
                    userViewModel.my_user.LunchEaten = true;
                    break;

                case 2:
                    if (!userViewModel.my_user.DinnerEaten) userViewModel.my_user.EatenCalories += recipe.Calories;
                    userViewModel.my_user.DinnerEaten = true;
                    break;

                case 3:
                    if (!userViewModel.my_user.SnackEaten) userViewModel.my_user.EatenCalories += recipe.Calories;
                    userViewModel.my_user.SnackEaten = true;
                    break;
            }
            navController.navigate(R.id.action_recipeCardFragment_to_homeFragment);
        });
    }
}