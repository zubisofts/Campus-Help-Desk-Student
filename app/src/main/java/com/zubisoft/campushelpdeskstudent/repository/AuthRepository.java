package com.zubisoft.campushelpdeskstudent.repository;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.zubisoft.campushelpdeskstudent.SignupActivity;
import com.zubisoft.campushelpdeskstudent.models.ApiResponse;
import com.zubisoft.campushelpdeskstudent.models.UserModel;

public class AuthRepository {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void loginUser(String email, String password, MutableLiveData<ApiResponse<UserModel, String>> userAuthLoginListener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> new DataRepository().fetchUser(authResult.getUser().getUid(), userAuthLoginListener)).addOnFailureListener(e -> userAuthLoginListener.postValue(new ApiResponse<>(null, e.getMessage())));
    }

    public void signUpUser(UserModel user, String password, MutableLiveData<ApiResponse<String, String>> userAuthListener) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnSuccessListener(authResult -> {
                    String id=authResult.getUser().getUid();
                    user.setId(id);
                    new DataRepository().saveUser(user, userAuthListener);
                }).addOnFailureListener(e -> {
        })
        .addOnFailureListener(e -> userAuthListener.postValue(new ApiResponse<>(null, e.getMessage())));
    }
}
