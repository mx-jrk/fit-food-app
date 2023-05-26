package com.example.fitfood.ui.Registration.LogIn;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.fitfood.R;
import com.example.fitfood.databinding.FragmentLogInBinding;
import com.example.fitfood.ui.view_models.ShoppingListViewModel;
import com.example.fitfood.ui.view_models.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class LogInFragment extends Fragment {
    FragmentLogInBinding binding;

    LifecycleOwner lifecycleOwner;

    NavHostFragment navHostFragment;
    NavController navController;

    UserViewModel userViewModel;
    ShoppingListViewModel shoppingListViewModel;

    FirebaseAuth firebaseAuth;
    DatabaseReference firebaseReference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogInBinding.inflate(inflater, container, false);
        lifecycleOwner = getViewLifecycleOwner();

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        shoppingListViewModel =new ViewModelProvider(requireActivity()).get(ShoppingListViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseReference = FirebaseDatabase.getInstance().getReference();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.progressBar.setVisibility(View.GONE);
        binding.progressBarTv.setVisibility(View.GONE);

        binding.userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.logInBtn.setEnabled(charSequence.length() > 0 && binding.password.getText().toString().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.logInBtn.setEnabled(charSequence.length() > 0 && binding.userName.getText().toString().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });


        binding.logInBtn.setOnClickListener(view1 -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.progressBarTv.setVisibility(View.VISIBLE);

            //User authentication in Firebase
            firebaseAuth.signInWithEmailAndPassword(binding.userName.getText().toString(), binding.password.getText().toString()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    userViewModel.my_user.FirebaseId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                    userViewModel.my_user.setFirebaseFields(firebaseAuth, firebaseReference);

                    //Setting values from Firebase to user object in ViewModel
                    userViewModel.downloadDataFromFirebase(() -> {
                        userViewModel.my_user.isLoadedToCloud = true;

                        userViewModel.setUserRecipesAndPlan(getViewLifecycleOwner(), () -> {
                            //Bulkhead shopping list
                            shoppingListViewModel.updateProductsForNewDay(getViewLifecycleOwner(), userViewModel.my_user.PlanId, () -> {
                                userViewModel.insert();
                                navController.navigate(R.id.action_logInFragment_to_homeFragment);
                            });
                        });

                    });
                } else {
                    Toast.makeText(getContext(), "Неверный логин или пароль!", Toast.LENGTH_LONG).show();
                    binding.progressBar.setVisibility(View.GONE);
                    binding.progressBarTv.setVisibility(View.GONE);
                }
            });
        });


    }
}