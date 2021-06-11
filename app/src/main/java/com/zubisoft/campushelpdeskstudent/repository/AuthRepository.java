package com.zubisoft.campushelpdeskstudent.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.zubisoft.campushelpdeskstudent.models.ApiResponse;
import com.zubisoft.campushelpdeskstudent.models.UserModel;

public class AuthRepository {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void loginUser(String email, String password, MutableLiveData<ApiResponse<String, String>> userAuthListener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> userAuthListener.postValue(new ApiResponse<>(authResult.getUser().getUid(), null))).addOnFailureListener(e -> userAuthListener.postValue(new ApiResponse<>(null, e.getMessage())));
    }

}
