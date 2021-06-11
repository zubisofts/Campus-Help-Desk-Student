package com.zubisoft.campushelpdeskstudent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        CountDownTimer timer = new CountDownTimer(2000,400) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                FirebaseUser currentUser = FirebaseAuth.getInstance()
                        .getCurrentUser();
                if(currentUser==null){
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }else{
                    Intent intent=new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("uid", currentUser.getUid());
                    startActivity(intent);
                }
                finish();


            }
        }.start();
    }
}