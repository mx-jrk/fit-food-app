package com.example.fitfood.ui;

import android.graphics.Color;
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
import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;
import com.example.fitfood.data.data_sources.room.entites.UserEntity;
import com.example.fitfood.databinding.FragmentProfileBinding;
import com.example.fitfood.ui.Survey.GoalWeightFragment;
import com.example.fitfood.ui.Survey.NameQuestionFragment;
import com.example.fitfood.ui.Survey.WeightQuestionFragment;
import com.example.fitfood.ui.view_models.UserViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    UserViewModel userViewModel;
    NavHostFragment navHostFragment;
    NavController navController;
    List<Entry> weight, calories;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();

        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.name.setText(userViewModel.my_user.Name + "!");
        binding.userName.setText(userViewModel.my_user.Name);

        binding.userWeight.setText(userViewModel.my_user.Weight + "кг");

        binding.userGoalWeight.setText(userViewModel.my_user.WeightGoal + "кг");

        binding.userPlan.setText(userViewModel.my_user.Plan.Title);

        Bundle bundle = new Bundle();
        bundle.putString("source", "profile");

        binding.changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NameQuestionFragment nameQuestionFragment = new NameQuestionFragment();
                nameQuestionFragment.setArguments(bundle);
                navController.navigate(R.id.action_profileFragment_to_nameQuestionFragment, bundle);
            }
        });


        binding.changeWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeightQuestionFragment weightQuestionFragment = new WeightQuestionFragment();
                  weightQuestionFragment.setArguments(bundle);
                navController.navigate(R.id.action_profileFragment_to_weightQuestionFragment, bundle);
            }
        });

        binding.changeGoalWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoalWeightFragment goalWeightFragment = new GoalWeightFragment();
               goalWeightFragment.setArguments(bundle);
                navController.navigate(R.id.action_profileFragment_to_goalWeightFragment, bundle);
            }
        });

        weight = userViewModel.my_user.getWeightHistoryAsList();
        calories = userViewModel.my_user.getEatenCaloriesHistoryAsList();

        if (userViewModel.my_user.EatenCaloriesHistory == null) calories.add(new Entry( 1, userViewModel.my_user.EatenCalories));
        else if (calories.size() == userViewModel.my_user.EatenCaloriesHistory.split(" ").length) calories.add(new Entry(calories.size() + 1, userViewModel.my_user.EatenCalories));
        else if (calories.get(calories.size() - 1).getY() != userViewModel.my_user.EatenCalories) calories.get(calories.size() - 1).setY(userViewModel.my_user.EatenCalories);
        setGraph(binding.caloriesGraph, calories, "Количество калорий", Color.parseColor("#62D2A2"));

        if (userViewModel.my_user.WeightHistory == null)weight.add(new Entry(1, Float.parseFloat(String.valueOf(userViewModel.my_user.Weight))));
        else if (weight.size() == userViewModel.my_user.WeightHistory.split(" ").length) weight.add(new Entry(weight.size() + 1, Float.parseFloat(String.valueOf(userViewModel.my_user.Weight))));
        else if (weight.get(weight.size() - 1).getY() != userViewModel.my_user.Weight) weight.get(weight.size()-1).setY(Float.parseFloat(String.valueOf(userViewModel.my_user.Weight)));
        setGraph(binding.weightGraph, weight, "Вес", Color.parseColor("#00E0FF"));

    }

    private void setGraph(LineChart graph, List<Entry> data, String label, int color){
        graph.getLegend().setEnabled(false);

        XAxis xAxis = graph.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "День " + (int) value;
            }
        });

        YAxis yAxis = graph.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setGranularity(1f);

        graph.getAxisRight().setEnabled(false);

        LineDataSet eatenCaloriesDataSet = new LineDataSet(data, label);
        eatenCaloriesDataSet.setColor(color);
        eatenCaloriesDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        eatenCaloriesDataSet.setCubicIntensity(0.15f);
        eatenCaloriesDataSet.setDrawFilled(true);
        eatenCaloriesDataSet.setFillColor(color);
        eatenCaloriesDataSet.setFillAlpha(70);

        LineData lineData = new LineData(eatenCaloriesDataSet);
        graph.setData(lineData);

        graph.getDescription().setEnabled(false);
        graph.animateX(200);
        graph.invalidate();
    }

}