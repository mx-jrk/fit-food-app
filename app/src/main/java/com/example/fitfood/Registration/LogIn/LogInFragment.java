package com.example.fitfood.Registration.LogIn;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitfood.MainActivity;
import com.example.fitfood.R;
import com.example.fitfood.databinding.FragmentLogInBinding;
import com.example.fitfood.databinding.FragmentLoginOrSignupBinding;


public class LogInFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.example.fitfood.databinding.FragmentLogInBinding binding = FragmentLogInBinding.inflate(getLayoutInflater());

        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();

        binding.logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.show_bottom();
                navController.navigate(R.id.action_logInFragment_to_homeFragment);
            }
        });

        return binding.getRoot();
    }
}