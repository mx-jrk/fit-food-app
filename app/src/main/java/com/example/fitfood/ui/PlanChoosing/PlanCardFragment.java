package com.example.fitfood.ui.PlanChoosing;

import android.annotation.SuppressLint;
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
import com.example.fitfood.data.data_sources.room.entites.ProductEntity;
import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;
import com.example.fitfood.databinding.FragmentPlanCardBinding;
import com.example.fitfood.ui.adapters.PlanCardAdapter;
import com.example.fitfood.ui.view_models.ShoppingListViewModel;
import com.example.fitfood.ui.view_models.UserViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PlanCardFragment extends Fragment {
    FragmentPlanCardBinding binding;

    NavHostFragment navHostFragment;
    NavController navController;

    UserViewModel userViewModel;
    ShoppingListViewModel shoppingListViewModel;

    PlanCardAdapter planCardAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlanCardBinding.inflate(inflater, container, false);

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        shoppingListViewModel = new ViewModelProvider(requireActivity()).get(ShoppingListViewModel.class);

        return binding.getRoot();
    }

    @SuppressLint({"SetTextI18n", "DiscouragedApi"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null){
            List<List<RecipeEntity>> plansRecipes = new ArrayList<>();

            userViewModel.getPlansById(getArguments().getInt("plan")).observe(getViewLifecycleOwner(), plan -> {
                binding.title.setText(plan.Title);
                binding.description.setText(plan.Description);
                binding.calories.setText("Среднесуточный калораж: " + plan.AverageCalories);
                binding.image.setImageResource(requireContext().getResources().getIdentifier(plan.ImageName, "drawable", requireContext().getPackageName()));

                assert getArguments() != null;
                if (!getArguments().getBoolean("is_first")) binding.chooseBtn.setVisibility(View.GONE);
                binding.chooseBtn.setOnClickListener(view1 -> {
                    userViewModel.my_user.Plan = plan;
                    userViewModel.my_user.PlanId = plan.id;
                    userViewModel.getRecipesByPlan(userViewModel.my_user).observe(getViewLifecycleOwner(), recipeEntities -> {
                        userViewModel.my_user.DailyRecipes = recipeEntities;
                        userViewModel.insert();

                        shoppingListViewModel.deleteGenerated();

                        parseRecipes(recipeEntities, "today");

                        userViewModel.getRecipesByPlan(userViewModel.my_user.PlanId, getNextDayOfWeek(new Date().toString().split(" ")[0])).observe(getViewLifecycleOwner(), recipeEntities1 -> {
                            parseRecipes(recipeEntities1, "tomorrow");

                            shoppingListViewModel.getAllRecipesByPlan(userViewModel.my_user.PlanId).observe(getViewLifecycleOwner(), recipeEntities11 -> {
                                parseRecipes(recipeEntities11, "week");
                                navController.navigate(R.id.action_planCardFragment_to_homeFragment);
                            });
                        });

                    });
                });


                userViewModel.getRecipesByPlan(getArguments().getInt("plan"), "Mon").observe(getViewLifecycleOwner(), recipeEntities -> {
                    plansRecipes.add(recipeEntities);
                    userViewModel.getRecipesByPlan(getArguments().getInt("plan"), "Tue").observe(getViewLifecycleOwner(), recipeEntities12 -> {
                        plansRecipes.add(recipeEntities12);
                        userViewModel.getRecipesByPlan(getArguments().getInt("plan"), "Wed").observe(getViewLifecycleOwner(), recipeEntities1212 -> {
                            plansRecipes.add(recipeEntities1212);
                            userViewModel.getRecipesByPlan(getArguments().getInt("plan"), "Thu").observe(getViewLifecycleOwner(), recipeEntities121 -> {
                                plansRecipes.add(recipeEntities121);
                                userViewModel.getRecipesByPlan(getArguments().getInt("plan"), "Fri").observe(getViewLifecycleOwner(), recipeEntities1211 -> {
                                    plansRecipes.add(recipeEntities1211);
                                    userViewModel.getRecipesByPlan(getArguments().getInt("plan"), "Sat").observe(getViewLifecycleOwner(), recipeEntities12111 -> {
                                        plansRecipes.add(recipeEntities12111);
                                        userViewModel.getRecipesByPlan(getArguments().getInt("plan"), "Sun").observe(getViewLifecycleOwner(), recipeEntities121111 -> {
                                            plansRecipes.add(recipeEntities121111);
                                            planCardAdapter = new PlanCardAdapter(getContext(),plansRecipes);
                                            binding.allPlanRecipes.setAdapter(planCardAdapter);
                                            planCardAdapter.notifyDataSetChanged();
                                        });
                                    });
                                });
                            });

                        });
                    });
                });
            });




        }
    }

    private String getNextDayOfWeek(String dayOfWeek) {
        if (Objects.equals(dayOfWeek, "Sun")) return "Mon";
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
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
                System.out.println(product);
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

