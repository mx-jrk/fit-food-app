package com.example.fitfood.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
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
import com.example.fitfood.databinding.FragmentProfileBinding;
import com.example.fitfood.ui.Survey.GoalWeightFragment;
import com.example.fitfood.ui.Survey.NameQuestionFragment;
import com.example.fitfood.ui.Survey.WeightQuestionFragment;
import com.example.fitfood.ui.view_models.UserViewModel;

import java.util.List;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    UserViewModel userViewModel;
    NavHostFragment navHostFragment;
    NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();

        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.name.setText(userViewModel.my_user.Name + "!");
        binding.userName.setText(userViewModel.my_user.Name);

        binding.userWeight.setText(userViewModel.my_user.Weight + "кг");

        binding.userGoalWeight.setText(userViewModel.my_user.WeightGoal + "кг");

        binding.userPlan.setText(userViewModel.my_user.Plan.Title);

        Bundle bundle = new Bundle();
        bundle.putString("source", "profile");

        binding.changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NameQuestionFragment nameQuestionFragment = new NameQuestionFragment();
                nameQuestionFragment.setArguments(bundle);
                navController.navigate(R.id.action_profileFragment_to_nameQuestionFragment, bundle);
            }
        });


        binding.changeWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeightQuestionFragment weightQuestionFragment = new WeightQuestionFragment();
                  weightQuestionFragment.setArguments(bundle);
                navController.navigate(R.id.action_profileFragment_to_weightQuestionFragment, bundle);
            }
        });

        binding.changeGoalWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoalWeightFragment goalWeightFragment = new GoalWeightFragment();
               goalWeightFragment.setArguments(bundle);
                navController.navigate(R.id.action_profileFragment_to_goalWeightFragment, bundle);
            }
        });
    }
}