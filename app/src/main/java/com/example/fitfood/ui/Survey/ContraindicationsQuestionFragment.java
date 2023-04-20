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
import com.example.fitfood.databinding.FragmentContraindicationsQuestionBinding;
import com.example.fitfood.databinding.FragmentGoalWeightBinding;
import com.example.fitfood.ui.view_models.UserViewModel;

public class ContraindicationsQuestionFragment extends Fragment {

    FragmentContraindicationsQuestionBinding binding;
    UserViewModel userViewModel;
    NavHostFragment navHostFragment;
    NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentContraindicationsQuestionBinding.inflate(inflater, container, false);

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();

        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.contraindications.addTextChangedListener(new TextWatcher() {
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
                userViewModel.my_user.Contraindications_s = binding.contraindications.getText().toString();
                navController.navigate(R.id.action_contraindicationsQuestionFragment_to_preferencesQuestionFragment);
            }
        });
    }

}