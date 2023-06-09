package com.example.fitfood.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.fitfood.R;
import com.example.fitfood.data.data_sources.room.entites.ProductEntity;
import com.example.fitfood.databinding.FragmentAddOwnProductBinding;
import com.example.fitfood.ui.view_models.ShoppingListViewModel;

public class AddOwnProductFragment extends Fragment {
    FragmentAddOwnProductBinding binding;

    private boolean count_input;
    private boolean name_input;

    ShoppingListViewModel shoppingListViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddOwnProductBinding.inflate(inflater, container, false);

        count_input = false;

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        AddOwnProductFragment.super.onViewCreated(view, savedInstanceState);
        shoppingListViewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);
        binding.productName.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name_input = charSequence.toString().length() != 0;
                check_button();
            }

            public void afterTextChanged(Editable editable) {}
        });

        binding.productCount.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                count_input = charSequence.toString().length() != 0;
                check_button();
            }

            public void afterTextChanged(Editable editable) {}
        });

        binding.nextBtn.setOnClickListener(view1 -> {
            try {
                assert getArguments() != null;
                shoppingListViewModel.insert(new ProductEntity(binding.productName.getText().toString(),
                        Integer.parseInt(binding.productCount.getText().toString()), false, false, getArguments().getString("type")));
                Navigation.findNavController(AddOwnProductFragment.this.requireView()).navigate(R.id.action_addOwnProduct_to_shoppingListFragment);
            } catch (NumberFormatException e) {
                Toast.makeText(AddOwnProductFragment.this.getActivity(), "В поле «количество» вы ввели не целое число!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void check_button() {
        if (name_input && count_input) {
            binding.nextBtn.setEnabled(true);
        }
    }
}