package com.example.fitfood.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fitfood.R;
import com.example.fitfood.databinding.FragmentShoppingListBinding;
import com.example.fitfood.ui.adapters.ProductListAdapter;
import com.example.fitfood.ui.view_models.ShoppingListViewModel;

public class ShoppingListFragment extends Fragment {
    ProductListAdapter adapter;

    FragmentShoppingListBinding binding;

    ShoppingListViewModel shoppingListViewModel;

    String selectedSpinner;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShoppingListBinding.inflate(inflater, container, false);

        adapter = new ProductListAdapter(requireContext());

        shoppingListViewModel = new ViewModelProvider(requireActivity()).get(ShoppingListViewModel.class);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        ShoppingListFragment.super.onViewCreated(view, savedInstanceState);

        binding.shoppingList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.shoppingList.setAdapter(this.adapter);

        selectedSpinner = "today";

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        selectedSpinner = "today";
                        break;
                    case 1:
                        selectedSpinner = "tomorrow";
                        break;
                    case 2:
                        selectedSpinner = "week";
                        break;
                    default:
                        break;
                }

                shoppingListViewModel.getAllProductsByType(selectedSpinner).observe(getViewLifecycleOwner(), productEntities -> {
                    if (selectedSpinner.contains(productEntities.get(0).type)){
                        adapter.setProducts(productEntities);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        this.binding.addProduct.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putString("type", selectedSpinner);
            AddOwnProductFragment addOwnProductFragment = new AddOwnProductFragment();
            addOwnProductFragment.setArguments(bundle);
            Navigation.findNavController(ShoppingListFragment.this.requireView()).navigate(R.id.action_shoppingListFragment_to_addOwnProduct, bundle);
        });
    }
}