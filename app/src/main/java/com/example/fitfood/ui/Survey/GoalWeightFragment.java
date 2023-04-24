package com.example.fitfood.ui.Survey;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fitfood.R;
import com.example.fitfood.databinding.FragmentGoalQuestionBinding;
import com.example.fitfood.databinding.FragmentGoalWeightBinding;
import com.example.fitfood.ui.view_models.UserViewModel;

public class GoalWeightFragment extends Fragment {
    FragmentGoalWeightBinding binding;
    UserViewModel userViewModel;
    NavHostFragment navHostFragment;
    NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGoalWeightBinding.inflate(inflater, container, false);

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();

        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) binding.nextBtn.setEnabled(true);
                else binding.nextBtn.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    userViewModel.my_user.WeightGoal = Integer.parseInt(binding.weight.getText().toString());
                    if (userViewModel.getUser().getValue() == null){
                        navController.navigate(R.id.action_goalWeightFragment_to_contraindicationsQuestionFragment);
                    }
                    else {
                        navController.navigate(R.id.action_goalWeightFragment_to_profileFragment);
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Вы ввели не число!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}