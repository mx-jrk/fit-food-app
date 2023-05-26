package com.example.fitfood.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.fitfood.R;
import com.example.fitfood.databinding.FragmentLogoBinding;
import com.example.fitfood.ui.view_models.ShoppingListViewModel;
import com.example.fitfood.ui.view_models.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class LogoFragment extends Fragment {
    FragmentLogoBinding binding;

    NavHostFragment navHostFragment;
    NavController navController;

    UserViewModel userViewModel;
    ShoppingListViewModel shoppingListViewModel;

    FirebaseAuth firebaseAuth;
    DatabaseReference firebaseReference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogoBinding.inflate(inflater, container, false);

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        shoppingListViewModel =new ViewModelProvider(requireActivity()).get(ShoppingListViewModel.class);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseReference = FirebaseDatabase.getInstance().getReference();

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);


        userViewModel.setFirebaseFields(firebaseAuth, firebaseReference);
        userViewModel.setUserFields(getViewLifecycleOwner(), firebaseAuth,firebaseReference ,()  -> {
            //First Launch checking
            if (userViewModel.my_user.Name == null) {
                navController.navigate(R.id.loginOrSignupFragment);
            }
            else if (userViewModel.my_user.LastChangeDateInt != 0 && Calendar.getInstance().get(Calendar.DAY_OF_YEAR) != userViewModel.my_user.LastChangeDateInt){
                //First Launch checking
                shoppingListViewModel.updateProductsForNewDay(getViewLifecycleOwner(), userViewModel.my_user.PlanId, this::goToHomePage);
            }
            else {
                goToHomePage();
            }

        });

    }

    private void goToHomePage(){
        navController.navigate(R.id.homeFragment);
    }





}
