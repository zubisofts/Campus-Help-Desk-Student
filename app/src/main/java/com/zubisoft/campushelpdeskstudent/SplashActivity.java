package com.zubisoft.campushelpdeskstudent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zubisoft.campushelpdeskstudent.databinding.ActivitySplashBinding;
import com.zubisoft.campushelpdeskstudent.features.admin.AdminMainActivity;
import com.zubisoft.campushelpdeskstudent.features.staff.StaffMainActivity;
import com.zubisoft.campushelpdeskstudent.models.UserModel;
import com.zubisoft.campushelpdeskstudent.viewmodels.AuthViewModel;

public class SplashActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel =
                new ViewModelProvider(this).get(AuthViewModel.class);

        authViewModel.onAuthLoginResponse().observe(this, userResponse -> {
            if(userResponse.getError()==null){
                UserModel user=userResponse.getData();
                if(user.getType().equals("student")){
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("uid", user.getId());
                    intent.putExtra("type", user.getType());
                    startActivity(intent);
                    finish();
                }else if(user.getType().equals("staff")){
                    Intent intent = new Intent(this, StaffMainActivity.class);
                    intent.putExtra("uid", user.getId());
                    intent.putExtra("type", user.getType());
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(this, AdminMainActivity.class);
                    intent.putExtra("uid", user.getId());
                    intent.putExtra("type", user.getType());
                    startActivity(intent);
                    finish();
                }
            }else{
                Snackbar.make(binding.getRoot(), "An error occurred, make sure you also have an active connection.", Snackbar.LENGTH_LONG).show();
            }
        });

        new CountDownTimer(2000,400) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                FirebaseUser currentUser = FirebaseAuth.getInstance()
                        .getCurrentUser();
                if(currentUser==null){
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }else{
                    authViewModel.fetchUserDetails(currentUser.getUid());
                }
            }
        }.start();
    }
}