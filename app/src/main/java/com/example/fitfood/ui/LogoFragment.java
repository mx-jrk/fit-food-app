package com.example.fitfood.ui;

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
import com.example.fitfood.data.data_sources.room.entites.PlanEntity;
import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;
import com.example.fitfood.data.data_sources.room.entites.UserEntity;
import com.example.fitfood.data.repositories.PlanRepository;
import com.example.fitfood.databinding.FragmentHomeBinding;
import com.example.fitfood.databinding.FragmentLogoBinding;
import com.example.fitfood.ui.view_models.UserViewModel;

import java.util.List;

public class LogoFragment extends Fragment {

    FragmentLogoBinding binding;
    NavHostFragment navHostFragment;
    NavController navController;
    UserViewModel userViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogoBinding.inflate(inflater, container, false);

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //First Launch checking
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        userViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity user) {
                if (user == null && userViewModel.my_user.Plan == null){
                    NavController navController = navHostFragment.getNavController();
                    navController.navigate(R.id.loginOrSignupFragment);
                }
                else {
                    userViewModel.getPlansById(user.PlanId).observe(getViewLifecycleOwner(), new Observer<PlanEntity>() {
                        @Override
                        public void onChanged(PlanEntity plan) {
                            user.Plan = plan;
                        }
                    });
                    userViewModel.getRecipesByPlan(user).observe(getViewLifecycleOwner(), new Observer<List<RecipeEntity>>() {
                        @Override
                        public void onChanged(List<RecipeEntity> recipeEntities) {
                            user.DailyRecipes = recipeEntities;
                            if (user.DailyRecipes != null && user.PlanId != 0){
                                userViewModel.my_user = user;
                                navController.navigate(R.id.action_logoFragment_to_homeFragment);
                            }
                        }
                    });
                }
            }
        });
    }

}
