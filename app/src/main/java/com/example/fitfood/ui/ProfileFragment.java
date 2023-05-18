package com.example.fitfood.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.Calendar;
import java.util.List;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;

    UserViewModel userViewModel;

    NavHostFragment navHostFragment;
    NavController navController;

    List<Entry> weight, calories;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (hour > 5 && hour < 12) binding.hello.setText("Доброе утро,");
        else if (hour >= 12 && hour < 17) binding.hello.setText("Добрый день,");
        else if (hour >= 17 && hour < 23) binding.hello.setText("Добрый вечер,");
        else binding.hello.setText("Доброй ночи,");


        binding.name.setText(userViewModel.my_user.Name + "!");

        binding.userName.setText(userViewModel.my_user.Name);

        binding.userWeight.setText(userViewModel.my_user.Weight + "кг");

        binding.userGoalWeight.setText(userViewModel.my_user.WeightGoal + "кг");

        binding.userPlan.setText(userViewModel.my_user.Plan.Title);

        Bundle bundle = new Bundle();
        bundle.putString("source", "profile");

        //Setting up transitions to editing fragments
        binding.changeName.setOnClickListener(view1 -> {
            NameQuestionFragment nameQuestionFragment = new NameQuestionFragment();
            nameQuestionFragment.setArguments(bundle);
            navController.navigate(R.id.action_profileFragment_to_nameQuestionFragment, bundle);
        });


        binding.changeWeight.setOnClickListener(view12 -> {
            WeightQuestionFragment weightQuestionFragment = new WeightQuestionFragment();
              weightQuestionFragment.setArguments(bundle);
            navController.navigate(R.id.action_profileFragment_to_weightQuestionFragment, bundle);
        });

        binding.changeGoalWeight.setOnClickListener(view13 -> {
            GoalWeightFragment goalWeightFragment = new GoalWeightFragment();
           goalWeightFragment.setArguments(bundle);
            navController.navigate(R.id.action_profileFragment_to_goalWeightFragment, bundle);
        });

        //Setting charts
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

    //  Method of creating and configuring graphs
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