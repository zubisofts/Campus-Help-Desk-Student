package com.zubisoft.campushelpdeskstudent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.zubisoft.campushelpdeskstudent.features.admin.AdminMainActivity;
import com.zubisoft.campushelpdeskstudent.databinding.ActivityLoginBinding;
import com.zubisoft.campushelpdeskstudent.models.UserModel;
import com.zubisoft.campushelpdeskstudent.viewmodels.AuthViewModel;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AuthViewModel authViewModel;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(this);

        authViewModel = new ViewModelProvider.NewInstanceFactory().create(AuthViewModel.class);
        initFields();
        binding.btnLogin.setOnClickListener(v -> {
            if (validateLogin()) {
                signInUser();
            }
        });

        binding.txtCreateAccount.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        });

        authViewModel.onAuthLoginResponse().observe(this, response -> {
            if (response.getError() == null) {
                UserModel user=response.getData();
                if(user.getType().equals("student")){
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("uid", response.getData().getId());
                    startActivity(intent);
                    finish();
                }else if(user.getType().equals("staff")){
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("uid", response.getData().getId());
//                    startActivity(intent);
                    Toast.makeText(this, "This is a staff", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(this, AdminMainActivity.class);
                    intent.putExtra("uid", response.getData().getId());
                    startActivity(intent);
                }
            } else {
                Snackbar.make(binding.getRoot(), response.getError(), Snackbar.LENGTH_LONG).show();
            }

            hideDialog();
        });

//        ArrayList<String> dpt = new ArrayList<>();
//        dpt.add("Student");
//        dpt.add("Staff");
//        dpt.add("Admin");
//        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, dpt);
//        binding.spinnerRole.setAdapter(adapter1);

    }

    private void signInUser() {
        showLoadingDialog();
        authViewModel.loginUser(binding.edtEmail.getText().toString(),
                binding.edtPassword.getText().toString());

    }

    private boolean validateLogin() {

        if (TextUtils.isEmpty(binding.edtEmail.getText().toString())) {
            return false;
        } else return binding.edtPassword.getText().toString().length() >= 5;

    }

    private void initFields() {
        binding.edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    binding.inputEmail.setError("Please enter a valid email");
                } else {
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

    private void showLoadingDialog() {
        dialog.setMessage("Signing in...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void hideDialog() {
        if (dialog.isShowing()) {
            dialog.hide();
        }
    }
}