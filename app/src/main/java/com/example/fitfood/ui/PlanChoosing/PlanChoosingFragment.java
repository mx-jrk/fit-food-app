package com.example.fitfood.ui.PlanChoosing;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitfood.R;
import com.example.fitfood.data.data_sources.room.entites.PlanEntity;
import com.example.fitfood.databinding.FragmentPlanChoosingBinding;
import com.example.fitfood.ui.adapters.PlanChoosingAdapter;
import com.example.fitfood.ui.view_models.ShoppingListViewModel;
import com.example.fitfood.ui.view_models.UserViewModel;

import java.util.List;

public class PlanChoosingFragment extends Fragment {

    FragmentPlanChoosingBinding binding;
    UserViewModel userViewModel;
    NavHostFragment navHostFragment;
    NavController navController;
    PlanChoosingAdapter adapter;
    ShoppingListViewModel shoppingListViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlanChoosingBinding.inflate(inflater, container, false);

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();

        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        shoppingListViewModel =new ViewModelProvider(getActivity()).get(ShoppingListViewModel.class);


        adapter = new PlanChoosingAdapter(getContext(), navController, userViewModel, shoppingListViewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.thanksTo.setText(binding.thanksTo.getText() + " " + userViewModel.my_user.Name + '!');
        binding.plansList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.plansList.setAdapter(adapter);


        userViewModel.getAllPlans().observe(getViewLifecycleOwner(), new Observer<List<PlanEntity>>() {
            @Override
            public void onChanged(List<PlanEntity> planEntities) {
                adapter.setPlans(userViewModel.my_user.choose_plans(planEntities));
            }
        });


    }
}