package com.example.fitfood.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fitfood.R;
import com.example.fitfood.data.DataLoadCallback;
import com.example.fitfood.data.data_sources.room.entites.PlanEntity;
import com.example.fitfood.data.data_sources.room.entites.ProductEntity;
import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;
import com.example.fitfood.data.data_sources.room.entites.UserEntity;
import com.example.fitfood.data.repositories.PlanRepository;
import com.example.fitfood.databinding.FragmentHomeBinding;
import com.example.fitfood.databinding.FragmentLogoBinding;
import com.example.fitfood.ui.view_models.ShoppingListViewModel;
import com.example.fitfood.ui.view_models.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class LogoFragment extends Fragment {

    FragmentLogoBinding binding;
    NavHostFragment navHostFragment;
    NavController navController;
    UserViewModel userViewModel;
    ShoppingListViewModel shoppingListViewModel;
    FirebaseAuth firebaseAuth;
    DatabaseReference firestoreReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogoBinding.inflate(inflater, container, false);

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();

        shoppingListViewModel =new ViewModelProvider(getActivity()).get(ShoppingListViewModel.class);

        firebaseAuth = FirebaseAuth.getInstance();

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //First Launch checking
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        userViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity user) {
                if (user == null){
                    NavController navController = navHostFragment.getNavController();
                    navController.navigate(R.id.loginOrSignupFragment);
                }
                else {
                    userViewModel.getPlansById(user.PlanId).observe(getViewLifecycleOwner(), new Observer<PlanEntity>() {
                        @Override
                        public void onChanged(PlanEntity plan) {
                            user.Plan = plan;
                            if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) != user.LastChangeDateInt){
                                userViewModel.getRecipesByPlan(user.PlanId, new Date().toString().split(" ")[0]).observe(getViewLifecycleOwner(), new Observer<List<RecipeEntity>>() {
                                    @Override
                                    public void onChanged(List<RecipeEntity> recipeEntities) {
                                        userViewModel.my_user = user;

                                        userViewModel.my_user.resetForNewDay(recipeEntities);

                                        shoppingListViewModel.deleteGenerated();

                                        parseRecipes(recipeEntities, "today");

                                        userViewModel.getRecipesByPlan(user.PlanId, getNextDayOfWeek(new Date().toString().split(" ")[0])).observe(getViewLifecycleOwner(), new Observer<List<RecipeEntity>>() {
                                            @Override
                                            public void onChanged(List<RecipeEntity> recipeEntities) {
                                                parseRecipes(recipeEntities, "tomorrow");

                                                shoppingListViewModel.getAllRecipesByPlan(user.PlanId).observe(getViewLifecycleOwner(), new Observer<List<RecipeEntity>>() {
                                                    @Override
                                                    public void onChanged(List<RecipeEntity> recipeEntities) {
                                                        parseRecipes(recipeEntities, "week");
                                                        navController.navigate(R.id.action_logoFragment_to_homeFragment);
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                            else {
                                userViewModel.getRecipesByPlan(user).observe(getViewLifecycleOwner(), new Observer<List<RecipeEntity>>() {
                                    @Override
                                    public void onChanged(List<RecipeEntity> recipeEntities) {
                                        user.DailyRecipes = recipeEntities;
                                        System.out.println(getNextDayOfWeek("Sun"));
                                        userViewModel.my_user = user;

                                        try {
                                            firestoreReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid());
                                             userViewModel.uploadDataToFirebaseCloud(new DataLoadCallback() {
                                                 @Override
                                                 public void onDataLoaded() {
                                                     navController.navigate(R.id.action_logoFragment_to_homeFragment);
                                                 }
                                             });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Toast.makeText(getContext(), "Не удалось загрузить данные из базы. Будут использованы локальные данные", Toast.LENGTH_SHORT).show();
                                            navController.navigate(R.id.action_logoFragment_to_homeFragment);
                                        }

                                    }
                                });
                            }
                        }
                    });

                }
            }
        });
    }

    private String getNextDayOfWeek(String dayOfWeek) {
        if (Objects.equals(dayOfWeek, "Sun")) return "Mon";
        String[] days = {"Mon", "Tue", "Wen", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < days.length; i++){
            if (days[i].equals(dayOfWeek)) return days[i + 1];
        }
        return dayOfWeek;
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

}
