package com.example.fitfood.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fitfood.R;
import com.example.fitfood.data.data_sources.room.entites.PlanEntity;
import com.example.fitfood.data.data_sources.room.entites.ProductEntity;
import com.example.fitfood.data.data_sources.room.root.PlanDatabase;
import com.example.fitfood.databinding.FragmentShoppingListBinding;
import com.example.fitfood.ui.adapters.ProductListAdapter;
import com.example.fitfood.ui.view_models.ShoppingListViewModel;
import java.util.List;

public class ShoppingListFragment extends Fragment {
    ProductListAdapter adapter;
    FragmentShoppingListBinding binding;
    ShoppingListViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShoppingListBinding.inflate(inflater, container, false);
        adapter = new ProductListAdapter();
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        ShoppingListFragment.super.onViewCreated(view, savedInstanceState);
        binding.shoppingList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.shoppingList.setAdapter(this.adapter);
        ShoppingListViewModel shoppingListViewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);
        viewModel = shoppingListViewModel;

        shoppingListViewModel.getAllPlans().observe(getViewLifecycleOwner(), new Observer<List<PlanEntity>>() {
            @Override
            public void onChanged(List<PlanEntity> planEntities) {
                Toast.makeText(getContext(), String.valueOf(planEntities.size()), Toast.LENGTH_SHORT).show();
            }
        });

        shoppingListViewModel.getAllProducts().observe(getViewLifecycleOwner(), new Observer<List<ProductEntity>>() {
            @SuppressLint("SetTextI18n")
            public void onChanged(List<ProductEntity> productEntities) {
                adapter.setProducts(productEntities);
                binding.bought.setText("Куплено " + ShoppingListFragment.this.adapter.getSelectedItemCount() + " из " + ShoppingListFragment.this.adapter.getItemCount());
            }
        });
        this.binding.addProduct.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Navigation.findNavController(ShoppingListFragment.this.requireView()).navigate(R.id.action_shoppingListFragment_to_addOwnProduct);
            }
        });
    }
}