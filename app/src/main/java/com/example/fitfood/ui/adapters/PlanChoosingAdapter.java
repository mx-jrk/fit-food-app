package com.example.fitfood.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitfood.R;
import com.example.fitfood.data.data_sources.room.entites.PlanEntity;
import com.example.fitfood.data.data_sources.room.entites.ProductEntity;
import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;
import com.example.fitfood.ui.PlanChoosing.PlanCardFragment;
import com.example.fitfood.ui.Survey.WeightQuestionFragment;
import com.example.fitfood.ui.view_models.ShoppingListViewModel;
import com.example.fitfood.ui.view_models.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class PlanChoosingAdapter extends RecyclerView.Adapter<PlanChoosingAdapter.PlanHolder> {
    private List<PlanEntity> plans = new ArrayList<>();
    private Context context;
    private NavController navController;
    private UserViewModel userViewModel;
    private ShoppingListViewModel shoppingListViewModel;
    private boolean firstLaunch;

    public PlanChoosingAdapter(Context context, NavController navController, UserViewModel userViewModel, ShoppingListViewModel shoppingListViewModel) {
        this.context = context;
        this.navController = navController;
        this.userViewModel = userViewModel;
        this.shoppingListViewModel = shoppingListViewModel;
    }

    @NonNull
    @Override
    public PlanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_choosing_recyclerview_item, parent, false);
        return new PlanHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanHolder holder, int position) {
        PlanEntity currentPlan = plans.get(position);
        holder.planTitle.setText(String.valueOf(currentPlan.Title));
        holder.planDescription.setText(currentPlan.Description);
        holder.planCalories.setText(String.valueOf(holder.planCalories.getText().toString() + ' ' + currentPlan.AverageCalories));
        holder.planImage.setImageResource(context.getResources().getIdentifier(currentPlan.ImageName, "drawable", context.getPackageName()));
        holder.watchPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("plan", currentPlan.id);
                bundle.putBoolean("is_first", true);
                PlanCardFragment planCardFragment = new PlanCardFragment();
                planCardFragment.setArguments(bundle);
                navController.navigate(R.id.action_planChoosingFragment_to_planCardFragment, bundle);
            }
        });
        holder.planChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userViewModel.my_user.PlanId = currentPlan.id;
                userViewModel.my_user.Plan = currentPlan;
                userViewModel.getRecipesByPlan(userViewModel.my_user).observe((LifecycleOwner)context, new Observer<List<RecipeEntity>>() {

                    @Override
                    public void onChanged(List<RecipeEntity> recipeEntities) {
                        userViewModel.my_user.DailyRecipes = recipeEntities;
                        if (!firstLaunch) {
                            shoppingListViewModel.deleteGenerated();
                            System.out.println("DDDDDDDD");
                            userViewModel.update();
                        }
                        else{
                            userViewModel.insert();
                        }


                        String[] products;
                        List<ProductEntity> productEntityList = new ArrayList<>();
                        ProductEntity generatedProduct;

                        for (RecipeEntity recipe : recipeEntities) {
                            if (recipe.Products == null) continue;
                            products = recipe.Products.split("\n");
                            for (String product : products) {
                                generatedProduct = new ProductEntity(product.split(": ")[0], Integer.parseInt(product.split(": ")[1].trim()), false, true);
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

                        navController.navigate(R.id.action_planChoosingFragment_to_homeFragment);
                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    public void setPlans(List<PlanEntity> plans){
        this.plans = plans;
        notifyDataSetChanged();
    }

    public void setFirstLaunch(boolean isFirst){
        this.firstLaunch = isFirst;
    }





    static class PlanHolder extends RecyclerView.ViewHolder {
        private final TextView planTitle;
        private final TextView planDescription;
        private final TextView planCalories;
        private final ImageView planImage;
        private final Button planChoose;
        private final Button watchPlan;

        public PlanHolder(@NonNull View itemView) {
            super(itemView);
            planTitle = itemView.findViewById(R.id.plan_name);
            planDescription = itemView.findViewById(R.id.plan_description);
            planCalories = itemView.findViewById(R.id.average_calories);
            planImage = itemView.findViewById(R.id.plan_image);
            planChoose = itemView.findViewById(R.id.choose_btn);
            watchPlan = itemView.findViewById(R.id.watch_plan_btn);
        }
    }
}


