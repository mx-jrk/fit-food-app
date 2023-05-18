package com.example.fitfood.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.fitfood.R;
import com.example.fitfood.data.data_sources.room.entites.ProductEntity;
import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;
import com.example.fitfood.databinding.FragmentLogoBinding;
import com.example.fitfood.ui.view_models.ShoppingListViewModel;
import com.example.fitfood.ui.view_models.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
        //First Launch checking
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null){
                NavController navController = navHostFragment.getNavController();
                navController.navigate(R.id.loginOrSignupFragment);
            }
            else {
                user.setFirebaseFields(firebaseAuth, firebaseReference);
                userViewModel.getPlansById(user.PlanId).observe(getViewLifecycleOwner(), plan -> {
                    user.Plan = plan;
                    if (user.LastChangeDateInt != 0 && Calendar.getInstance().get(Calendar.DAY_OF_YEAR) != user.LastChangeDateInt){
                        System.out.println(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + " " + user.LastChangeDateInt);
                        userViewModel.getRecipesByPlan(user.PlanId, new Date().toString().split(" ")[0]).observe(getViewLifecycleOwner(), recipeEntities -> {
                            userViewModel.my_user = user;

                            userViewModel.my_user.resetForNewDay(recipeEntities);

                            shoppingListViewModel.deleteGenerated();

                            parseRecipes(recipeEntities, "today");

                            userViewModel.getRecipesByPlan(user.PlanId, getNextDayOfWeek(new Date().toString().split(" ")[0])).observe(getViewLifecycleOwner(), recipeEntities12 -> {
                                parseRecipes(recipeEntities12, "tomorrow");
                                System.out.println(getNextDayOfWeek(new Date().toString().split(" ")[0]) + recipeEntities12.size());
                                shoppingListViewModel.getAllRecipesByPlan(user.PlanId).observe(getViewLifecycleOwner(), recipeEntities1 -> {
                                    parseRecipes(recipeEntities1, "week");
                                    navController.navigate(R.id.action_logoFragment_to_homeFragment);
                                });
                            });
                        });
                    }
                    else {
                        userViewModel.getRecipesByPlan(user).observe(getViewLifecycleOwner(), recipeEntities -> {
                            user.DailyRecipes = recipeEntities;
                            userViewModel.my_user = user;
                            if (hasConnection(requireContext()) && !userViewModel.my_user.isLoadedToCloud) {
                                System.out.println("+CONNECTION -LOADED");
                                goToHomePage();
                            }
                            else if (hasConnection(requireContext())){
                                userViewModel.downloadDataFromFirebase(() -> {
                                    try {
                                        System.out.println("+CONNECTION +LOADED");
                                        System.out.println(userViewModel.my_user.BreakfastEaten);
                                        goToHomePage();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        System.out.println("ERROR OF NAV");
                                    }
                                });
                            }
                            else {
                                Toast.makeText(getContext(), "Не удалось загрузить данные из базы. Будут использованы локальные данные", Toast.LENGTH_SHORT).show();
                                System.out.println("-CONNECTION +LOADED");
                                goToHomePage();
                            }
                        });
                    }
                });

            }
        });
    }

    private void goToHomePage(){
        navController.navigate(R.id.homeFragment);
    }

    private boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        return wifiInfo != null && wifiInfo.isConnected();
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
        System.out.println(productEntityList.size());
        for (ProductEntity product : productEntityList) {
            shoppingListViewModel.insert(product);
            System.out.println(product.name + " " + product.count);
        }
    }

}
