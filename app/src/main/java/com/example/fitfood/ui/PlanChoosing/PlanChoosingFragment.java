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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fitfood.R;
import com.example.fitfood.databinding.FragmentPlanChoosingBinding;
import com.example.fitfood.ui.adapters.PlanChoosingAdapter;
import com.example.fitfood.ui.view_models.ShoppingListViewModel;
import com.example.fitfood.ui.view_models.UserViewModel;

public class PlanChoosingFragment extends Fragment {

    FragmentPlanChoosingBinding binding;

    NavHostFragment navHostFragment;
    NavController navController;

    UserViewModel userViewModel;
    ShoppingListViewModel shoppingListViewModel;

    PlanChoosingAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlanChoosingBinding.inflate(inflater, container, false);


        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        shoppingListViewModel =new ViewModelProvider(requireActivity()).get(ShoppingListViewModel.class);


        adapter = new PlanChoosingAdapter(getContext(), navController, userViewModel, shoppingListViewModel);

        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.thanksTo.setText(binding.thanksTo.getText() + " " + userViewModel.my_user.Name + '!');
        binding.plansList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.plansList.setAdapter(adapter);

        userViewModel.getAllPlans().observe(getViewLifecycleOwner(), planEntities -> {
            adapter.setFirstLaunch(getArguments() == null || getArguments().getBoolean("is_first"));
            adapter.setPlans(userViewModel.my_user.choosePlans(planEntities));
        });
    }
}