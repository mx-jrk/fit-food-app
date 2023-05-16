package com.example.fitfood.ui.PlanChoosing;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fitfood.R;
import com.example.fitfood.data.data_sources.room.entites.PlanEntity;
import com.example.fitfood.data.data_sources.room.entites.ProductEntity;
import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;
import com.example.fitfood.databinding.FragmentPlanCardBinding;
import com.example.fitfood.ui.adapters.PlanCardAdapter;
import com.example.fitfood.ui.view_models.HomeViewModel;
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
    PlanCardAdapter planCardAdapter;
    ShoppingListViewModel shoppingListViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlanCardBinding.inflate(inflater, container, false);

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();

        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        shoppingListViewModel = new ViewModelProvider(getActivity()).get(ShoppingListViewModel.class);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (getArguments() != null){
            List<List<RecipeEntity>> plansRicepes = new ArrayList<>();

            userViewModel.getPlansById(getArguments().getInt("plan")).observe(getViewLifecycleOwner(), new Observer<PlanEntity>() {
                @Override
                public void onChanged(PlanEntity plan) {
                    binding.title.setText(plan.Title);
                    binding.description.setText(plan.Description);
                    binding.calories.setText(String.valueOf("Среднесуточный калораж: " + plan.AverageCalories));
                    binding.image.setImageResource(getContext().getResources().getIdentifier(plan.ImageName, "drawable", getContext().getPackageName()));
                    if (!getArguments().getBoolean("is_first")) binding.chooseBtn.setVisibility(View.GONE);
                    binding.chooseBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            userViewModel.my_user.Plan = plan;
                            userViewModel.my_user.PlanId = plan.id;
                            userViewModel.getRecipesByPlan(userViewModel.my_user).observe(getViewLifecycleOwner(), new Observer<List<RecipeEntity>>() {
                                @Override
                                public void onChanged(List<RecipeEntity> recipeEntities) {
                                    userViewModel.my_user.DailyRecipes = recipeEntities;
                                    userViewModel.insert();

                                    shoppingListViewModel.deleteGenerated();

                                    parseRecipes(recipeEntities, "today");

                                    userViewModel.getRecipesByPlan(userViewModel.my_user.PlanId, getNextDayOfWeek(new Date().toString().split(" ")[0])).observe(getViewLifecycleOwner(), new Observer<List<RecipeEntity>>() {
                                        @Override
                                        public void onChanged(List<RecipeEntity> recipeEntities) {
                                            parseRecipes(recipeEntities, "tomorrow");

                                            shoppingListViewModel.getAllRecipesByPlan(userViewModel.my_user.PlanId).observe(getViewLifecycleOwner(), new Observer<List<RecipeEntity>>() {
                                                @Override
                                                public void onChanged(List<RecipeEntity> recipeEntities) {
                                                    parseRecipes(recipeEntities, "week");
                                                    navController.navigate(R.id.action_planCardFragment_to_homeFragment);
                                                }
                                            });
                                        }
                                    });

                                }
                            });
                        }
                    });


                    userViewModel.getRecipesByPlan(getArguments().getInt("plan"), "Mon").observe(getViewLifecycleOwner(), new Observer<List<RecipeEntity>>() {
                        @Override
                        public void onChanged(List<RecipeEntity> recipeEntities) {
                            plansRicepes.add(recipeEntities);
                            userViewModel.getRecipesByPlan(getArguments().getInt("plan"), "Tue").observe(getViewLifecycleOwner(), new Observer<List<RecipeEntity>>() {
                                @Override
                                public void onChanged(List<RecipeEntity> recipeEntities) {
                                    plansRicepes.add(recipeEntities);
                                    userViewModel.getRecipesByPlan(getArguments().getInt("plan"), "Wed").observe(getViewLifecycleOwner(), new Observer<List<RecipeEntity>>() {
                                        @Override
                                        public void onChanged(List<RecipeEntity> recipeEntities) {
                                            plansRicepes.add(recipeEntities);
                                            userViewModel.getRecipesByPlan(getArguments().getInt("plan"), "Thu").observe(getViewLifecycleOwner(), new Observer<List<RecipeEntity>>() {
                                                @Override
                                                public void onChanged(List<RecipeEntity> recipeEntities) {
                                                    plansRicepes.add(recipeEntities);
                                                    userViewModel.getRecipesByPlan(getArguments().getInt("plan"), "Fri").observe(getViewLifecycleOwner(), new Observer<List<RecipeEntity>>() {
                                                        @Override
                                                        public void onChanged(List<RecipeEntity> recipeEntities) {
                                                            plansRicepes.add(recipeEntities);
                                                            userViewModel.getRecipesByPlan(getArguments().getInt("plan"), "Sat").observe(getViewLifecycleOwner(), new Observer<List<RecipeEntity>>() {
                                                                @Override
                                                                public void onChanged(List<RecipeEntity> recipeEntities) {
                                                                    plansRicepes.add(recipeEntities);
                                                                    userViewModel.getRecipesByPlan(getArguments().getInt("plan"), "Sun").observe(getViewLifecycleOwner(), new Observer<List<RecipeEntity>>() {
                                                                        @Override
                                                                        public void onChanged(List<RecipeEntity> recipeEntities) {
                                                                            plansRicepes.add(recipeEntities);
                                                                            planCardAdapter = new PlanCardAdapter(getContext(),plansRicepes);
                                                                            binding.allPlanRecipes.setAdapter(planCardAdapter);
                                                                            planCardAdapter.notifyDataSetChanged();
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
                                }
                            });
                        }
                    });
                }
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

