package com.example.fitfood.ui.Registration.SignUp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fitfood.R;
import com.example.fitfood.databinding.FragmentLogInBinding;
import com.example.fitfood.databinding.FragmentSignUpBinding;
import com.example.fitfood.ui.view_models.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpFragment extends Fragment {

    FragmentSignUpBinding binding;
    UserViewModel userViewModel;
    NavHostFragment navHostFragment;
    NavController navController;
    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();

        firebaseAuth = FirebaseAuth.getInstance();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.progressBar.setVisibility(View.GONE);
        binding.progressBarTv.setVisibility(View.GONE);

        binding.userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 &&
                        binding.firstPassword.getText().toString().length() > 0
                        && binding.secondPassword.getText().toString().length() > 0) binding.logInBtn.setEnabled(true);
                else binding.logInBtn.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.firstPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 &&
                        binding.userName.getText().toString().length() > 0
                        && binding.secondPassword.getText().toString().length() > 0) binding.logInBtn.setEnabled(true);
                else binding.logInBtn.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.secondPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 &&
                        binding.firstPassword.getText().toString().length() > 0
                        && binding.userName.getText().toString().length() > 0) binding.logInBtn.setEnabled(true);
                else binding.logInBtn.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                firebaseAuth.createUserWithEmailAndPassword(binding.userName.getText().toString(), binding.firstPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            navController.navigate(R.id.action_signUpFragment_to_helloFragment);
                        }
                        else {
                            Toast.makeText(getContext(), "Ошибка регистрации пользователя. Проверьте подключение к интернету.", Toast.LENGTH_SHORT).show();
                            binding.progressBar.setVisibility(View.GONE);
                            binding.progressBarTv.setVisibility(View.GONE);
                        }
                    }
                });



            }
        });
    }

    private boolean isEmail(String string) {
        String emailPattern = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }
}