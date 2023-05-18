package com.example.fitfood.ui.Registration.SignUp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.fitfood.databinding.FragmentSignUpBinding;
import com.example.fitfood.ui.view_models.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpFragment extends Fragment {

    FragmentSignUpBinding binding;

    UserViewModel userViewModel;

    NavHostFragment navHostFragment;
    NavController navController;

    FirebaseAuth firebaseAuth;
    DatabaseReference firebaseReference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseReference = FirebaseDatabase.getInstance().getReference();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.progressBar.setVisibility(View.GONE);
        binding.progressBarTv.setVisibility(View.GONE);

        binding.userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.logInBtn.setEnabled(charSequence.length() > 0 &&
                        binding.firstPassword.getText().toString().length() > 0
                        && binding.secondPassword.getText().toString().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.firstPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.logInBtn.setEnabled(charSequence.length() > 0 &&
                        binding.userName.getText().toString().length() > 0
                        && binding.secondPassword.getText().toString().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.secondPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.logInBtn.setEnabled(charSequence.length() > 0 &&
                        binding.firstPassword.getText().toString().length() > 0
                        && binding.userName.getText().toString().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.logInBtn.setOnClickListener(view1 -> {
            if (!isEmail(binding.userName.getText().toString())){
                Toast.makeText(getContext(), "Вы ввели логин не в формате почты!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (binding.firstPassword.getText().toString().length() < 6){
                Toast.makeText(getContext(), "Ваш пароль меньше 6-ти символов!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!binding.secondPassword.getText().toString().equals(binding.firstPassword.getText().toString())){
                Toast.makeText(getContext(), "Ваши пароли не совпадают!", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.progressBarTv.setVisibility(View.VISIBLE);

            userViewModel.my_user.Login = binding.userName.getText().toString();
            userViewModel.my_user.Password = binding.firstPassword.getText().toString();

            //Creating a user in Firebase
            firebaseAuth.createUserWithEmailAndPassword(binding.userName.getText().toString(), binding.firstPassword.getText().toString()).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    userViewModel.my_user.FirebaseId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                    userViewModel.my_user.setFirebaseFields(firebaseAuth, firebaseReference);
                    userViewModel.my_user.isLoadedToCloud = true;
                    navController.navigate(R.id.action_signUpFragment_to_helloFragment);
                }
                else {
                    Toast.makeText(getContext(), "Ошибка регистрации пользователя. Проверьте подключение к интернету.", Toast.LENGTH_SHORT).show();
                    binding.progressBar.setVisibility(View.GONE);
                    binding.progressBarTv.setVisibility(View.GONE);
                }
            });
        });
    }

    //Checking that the string is mail
    private boolean isEmail(String string) {
        String emailPattern = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }
}