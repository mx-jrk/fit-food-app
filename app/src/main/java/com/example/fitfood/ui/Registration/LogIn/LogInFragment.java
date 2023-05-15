package com.example.fitfood.ui.Registration.LogIn;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
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
import com.example.fitfood.data.DataLoadCallback;
import com.example.fitfood.data.data_sources.room.entites.PlanEntity;
import com.example.fitfood.data.data_sources.room.entites.ProductEntity;
import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;
import com.example.fitfood.databinding.FragmentLogInBinding;
import com.example.fitfood.databinding.FragmentLoginOrSignupBinding;
import com.example.fitfood.ui.view_models.ShoppingListViewModel;
import com.example.fitfood.ui.view_models.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class LogInFragment extends Fragment {
    FragmentLogInBinding binding;
    UserViewModel userViewModel;
    NavHostFragment navHostFragment;
    NavController navController;
    FirebaseAuth firebaseAuth;
    DatabaseReference firestoreReference;
    ShoppingListViewModel shoppingListViewModel;
    LifecycleOwner lifecycleOwner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogInBinding.inflate(inflater, container, false);
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();

        firebaseAuth = FirebaseAuth.getInstance();
        firestoreReference = FirebaseDatabase.getInstance().getReference();

        shoppingListViewModel =new ViewModelProvider(getActivity()).get(ShoppingListViewModel.class);

        lifecycleOwner = getViewLifecycleOwner();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.progressBar.setVisibility(View.GONE);
        binding.progressBarTv.setVisibility(View.GONE);

        binding.userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 &&
                        binding.password.getText().toString().length() > 0) binding.logInBtn.setEnabled(true);
                else binding.logInBtn.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 &&
                        binding.userName.getText().toString().length() > 0) binding.logInBtn.setEnabled(true);
                else binding.logInBtn.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        binding.logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.progressBarTv.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(binding.userName.getText().toString(), binding.password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userViewModel.uploadDataToFirebaseCloud(new DataLoadCallback() {
                                @Override
                                public void onDataLoaded() {
                                    userViewModel.my_user.FirebaseId = firebaseAuth.getCurrentUser().getUid();
                                    userViewModel.my_user.isLoadedToCloud = true;
                                    userViewModel.getRecipesByPlan(userViewModel.my_user.PlanId, new Date().toString().split(" ")[0]).observe(lifecycleOwner, new Observer<List<RecipeEntity>>() {
                                        @Override
                                        public void onChanged(List<RecipeEntity> recipeEntities) {
                                            userViewModel.my_user.DailyRecipes = recipeEntities;
                                            userViewModel.getPlansById(userViewModel.my_user.PlanId).observe(lifecycleOwner, new Observer<PlanEntity>() {
                                                @Override
                                                public void onChanged(PlanEntity plan) {
                                                    userViewModel.my_user.Plan = plan;
                                                    userViewModel.insert();
                                                    shoppingListViewModel.deleteGenerated();

                                                    parseRecipes(recipeEntities, "today");

                                                    userViewModel.getRecipesByPlan(userViewModel.my_user.PlanId, getNextDayOfWeek(new Date().toString().split(" ")[0])).observe((LifecycleOwner) getContext(), new Observer<List<RecipeEntity>>() {
                                                        @Override
                                                        public void onChanged(List<RecipeEntity> recipeEntities) {
                                                            parseRecipes(recipeEntities, "tomorrow");

                                                            shoppingListViewModel.getAllRecipesByPlan(userViewModel.my_user.PlanId).observe(lifecycleOwner, new Observer<List<RecipeEntity>>() {
                                                                @Override
                                                                public void onChanged(List<RecipeEntity> recipeEntities) {
                                                                    parseRecipes(recipeEntities, "week");
                                                                    navController.navigate(R.id.action_logInFragment_to_homeFragment);
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });

                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "Неверный логин или пароль!", Toast.LENGTH_LONG).show();
                            binding.progressBar.setVisibility(View.GONE);
                            binding.progressBarTv.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });


    }

    private void parseRecipes(List<RecipeEntity> recipeEntities, String type){
        String[] products;
        List<ProductEntity> productEntityList = new ArrayList<>();
        ProductEntity generatedProduct;
        for (RecipeEntity recipe : recipeEntities) {
            if (recipe.Products == null) continue;
            products = recipe.Products.split("\n");
            for (String product : products) {
                generatedProduct = new ProductEntity(product.split(": ")[0], Integer.parseInt(product.split(": ")[1].trim()), false, true, type);
                if (productEntityList.contains(generatedProduct)) {
                    productEntityList.get(productEntityList.indexOf(generatedProduct)).count++;
                } else {
                    productEntityList.add(generatedProduct);
                }
            }
        }
        for (ProductEntity product : productEntityList) {
            shoppingListViewModel.insert(product);
            System.out.println(product.name + " " + product.count);
        }
    }

    private static String getNextDayOfWeek(String dayOfWeek) {
        if (Objects.equals(dayOfWeek, "Sun")) return "Mon";
        String[] days = {"Mon", "Tue", "Wen", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < days.length; i++){
            if (days[i].equals(dayOfWeek)) return days[i + 1];
        }
        return dayOfWeek;
    }
}