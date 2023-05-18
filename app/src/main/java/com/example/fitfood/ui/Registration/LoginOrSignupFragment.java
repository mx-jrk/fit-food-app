package com.example.fitfood.ui.Registration;

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
import com.example.fitfood.databinding.FragmentLoginOrSignupBinding;
import com.example.fitfood.ui.view_models.UserViewModel;

public class LoginOrSignupFragment extends Fragment {

   FragmentLoginOrSignupBinding binding;

   UserViewModel userViewModel;

   NavHostFragment navHostFragment;
   NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginOrSignupBinding.inflate(inflater, container, false);

         navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.logInBtn.setOnClickListener(view1 -> navController.navigate(R.id.action_loginOrSignupFragment_to_logInFragment));

        binding.signUpBtn.setOnClickListener(view12 -> navController.navigate(R.id.action_loginOrSignupFragment_to_signUpFragment));
    }
}