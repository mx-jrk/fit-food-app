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
import com.example.fitfood.databinding.FragmentHeightQuestionBinding;
import com.example.fitfood.databinding.FragmentWeightQuestionBinding;
import com.example.fitfood.ui.view_models.UserViewModel;

import java.util.Objects;


public class WeightQuestionFragment extends Fragment {


    FragmentWeightQuestionBinding binding;

    UserViewModel userViewModel;

    NavHostFragment navHostFragment;
    NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeightQuestionBinding.inflate(inflater, container, false);

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.nextBtn.setEnabled(charSequence.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.nextBtn.setOnClickListener(view1 -> {
                userViewModel.my_user.Weight = Double.parseDouble(binding.weight.getText().toString().replace(',', '.'));
                userViewModel.my_user.NormalCalories = (int) Math.round(userViewModel.my_user.Weight * 33);

                if (getArguments() == null){
                    navController.navigate(R.id.action_weightQuestionFragment_to_goalQuestionFragment);
                }
                else if (Objects.equals(getArguments().getString("source"), "profile")){
                    navController.navigate(R.id.action_weightQuestionFragment_to_profileFragment);
                }
                else if (Objects.equals(getArguments().getString("source"), "home")){
                    navController.navigate(R.id.action_weightQuestionFragment_to_homeFragment);
                }
        });
    }
}