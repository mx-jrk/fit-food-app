package com.example.fitfood.ui.Survey;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.fitfood.R;
import com.example.fitfood.databinding.FragmentGoalQuestionBinding;
import com.example.fitfood.databinding.FragmentWeightQuestionBinding;
import com.example.fitfood.ui.view_models.UserViewModel;

public class GoalQuestionFragment extends Fragment {

    FragmentGoalQuestionBinding binding;

    UserViewModel userViewModel;

    NavHostFragment navHostFragment;
    NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGoalQuestionBinding.inflate(inflater, container, false);

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.goal.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton selectedRadioButton = radioGroup.findViewById(i);

            binding.nextBtn.setEnabled(true);

            binding.nextBtn.setOnClickListener(view1 -> {
                userViewModel.my_user.Goal = selectedRadioButton.getText().toString();
                navController.navigate(R.id.action_goalQuestionFragment_to_goalWeightFragment);
            });
        });
    }
}
