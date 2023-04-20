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

import com.example.fitfood.R;
import com.example.fitfood.databinding.FragmentActivityQuestionBinding;
import com.example.fitfood.databinding.FragmentFoodStylQuestionBinding;
import com.example.fitfood.ui.view_models.UserViewModel;


public class FoodStylQuestionFragment extends Fragment {

    FragmentFoodStylQuestionBinding binding;
    UserViewModel userViewModel;
    NavHostFragment navHostFragment;
    NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFoodStylQuestionBinding.inflate(inflater, container, false);

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();

        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.foodStyle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton selectedRadioButton = radioGroup.findViewById(i);
                binding.nextBtn.setEnabled(true);
                binding.nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userViewModel.my_user.FoodStyle = selectedRadioButton.getText().toString();
                      //  userViewModel.my_user
                        navController.navigate(R.id.action_foodStylQuestionFragment_to_planChoosingFragment);
                    }
                });
            }
        });
    }
}