package com.example.fitfood.ui;

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


        List<RecipeEntity>  recipeEntities= userViewModel.my_user.DailyRecipes;


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


    }
}