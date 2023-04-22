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
import com.example.fitfood.ui.view_models.UserViewModel;

import java.util.Locale;

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
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Contraindications = "";
                if (binding.pig.isChecked()){
                    Contraindications += binding.pig.getText().toString().toLowerCase(Locale.ROOT);
                }

                if (binding.carbohydrate.isChecked()){
                    if (Contraindications.length() == 0) Contraindications += binding.carbohydrate.getText().toString().toLowerCase(Locale.ROOT);
                    else Contraindications += "\n" + binding.carbohydrate.getText().toString().toLowerCase(Locale.ROOT);
                }

                if (binding.fats.isChecked()){
                    if (Contraindications.length() == 0) Contraindications += binding.fats.getText().toString().toLowerCase(Locale.ROOT);
                    else Contraindications += "\n" + binding.fats.getText().toString().toLowerCase(Locale.ROOT);
                }

                if (binding.sugar.isChecked()){
                    if (Contraindications.length() == 0) Contraindications += binding.sugar.getText().toString().toLowerCase(Locale.ROOT);
                    else Contraindications += "\n" + binding.sugar.getText().toString().toLowerCase(Locale.ROOT);
                }
                if (binding.milk.isChecked()){
                    if (Contraindications.length() == 0) Contraindications += binding.milk.getText().toString().toLowerCase(Locale.ROOT);
                    else Contraindications += "\n" + binding.milk.getText().toString().toLowerCase(Locale.ROOT);
                }

                if (binding.meal.isChecked()){
                    if (Contraindications.length() == 0) Contraindications += binding.meal.getText().toString().toLowerCase(Locale.ROOT);
                    else Contraindications += "\n" + binding.meal.getText().toString().toLowerCase(Locale.ROOT);
                }

                userViewModel.my_user.Contraindications_s = Contraindications;
                navController.navigate(R.id.action_contraindicationsQuestionFragment_to_planChoosingFragment);
            }
        });
    }

}