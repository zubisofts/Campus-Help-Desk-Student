package com.zubisoft.campushelpdeskstudent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.util.PatternsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.zubisoft.campushelpdeskstudent.databinding.ActivityLoginBinding;
import com.zubisoft.campushelpdeskstudent.models.ApiResponse;
import com.zubisoft.campushelpdeskstudent.viewmodels.AuthViewModel;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AuthViewModel authViewModel;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog=new ProgressDialog(this);

        authViewModel= new ViewModelProvider.NewInstanceFactory().create(AuthViewModel.class);
        initFields();
        binding.btnLogin.setOnClickListener(v -> {
            if(validateLogin()){
                signInUser();
            }
        });

        binding.txtCreateAccount.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        });

        authViewModel.onAuthResponse().observe(this, response -> {
            if(response.getError()==null){
                Intent intent=new Intent(this, MainActivity.class);
                intent.putExtra("uid", response.getData());
                startActivity(intent);
                finish();
            }else{
                Snackbar.make(binding.getRoot(), response.getError(), Snackbar.LENGTH_LONG).show();
            }

            hideDialog();
        });

    }

    private void signInUser() {
        showLoadingDialog("Signing in...");
        authViewModel.loginUser(binding.edtEmail.getText().toString(), binding.edtPassword.getText().toString());

    }

    private boolean validateLogin() {

       if(TextUtils.isEmpty(binding.edtEmail.getText().toString())){
           return false;
       }else return binding.edtPassword.getText().toString().length() >= 5;

    }

    private void initFields(){
        binding.edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!Patterns.EMAIL_ADDRESS.matcher(s).matches()){
                    binding.inputEmail.setError("Please enter a valid email");
                }else {
                    binding.inputEmail.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    binding.inputPassword.setError(null);
                } else {
                    binding.inputPassword.setError("Password must be at least 5 characters");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showLoadingDialog(String message){
        dialog.setMessage(message);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void hideDialog(){
        if(dialog.isShowing()){
            dialog.hide();
        }
    }
}