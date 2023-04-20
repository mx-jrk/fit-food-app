package com.example.fitfood.ui.PlanChoosing;

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
import com.example.fitfood.databinding.FragmentFoodStylQuestionBinding;
import com.example.fitfood.databinding.FragmentPlanChoosingBinding;
import com.example.fitfood.ui.view_models.UserViewModel;

public class PlanChoosingFragment extends Fragment {

    FragmentPlanChoosingBinding binding;
    UserViewModel userViewModel;
    NavHostFragment navHostFragment;
    NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlanChoosingBinding.inflate(inflater, container, false);

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();

        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.thanksTo.setText(binding.thanksTo.getText() + userViewModel.my_user.Name);
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userViewModel.my_user.Plan = "Студенческий план";
                userViewModel.insert();
                navController.navigate(R.id.action_planChoosingFragment_to_homeFragment);
            }
        });
    }
}