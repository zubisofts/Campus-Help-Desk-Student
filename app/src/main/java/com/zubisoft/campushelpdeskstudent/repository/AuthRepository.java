package com.zubisoft.campushelpdeskstudent.repository;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zubisoft.campushelpdeskstudent.SignupActivity;
import com.zubisoft.campushelpdeskstudent.models.ApiResponse;
import com.zubisoft.campushelpdeskstudent.models.UserModel;

public class AuthRepository {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void loginUser(String email, String password, MutableLiveData<ApiResponse<UserModel, String>> userAuthLoginListener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> new DataRepository().fetchUser(authResult.getUser().getUid(), userAuthLoginListener)).addOnFailureListener(e -> userAuthLoginListener.postValue(new ApiResponse<>(null, e.getMessage())));
    }

    public void signUpStudent(UserModel user, String password, MutableLiveData<ApiResponse<String, String>> userAuthListener) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnSuccessListener(authResult -> {
                    String id=authResult.getUser().getUid();
                    user.setId(id);
                    new DataRepository().saveUser(user, userAuthListener);
                }).addOnFailureListener(e -> {
        })
        .addOnFailureListener(e -> userAuthListener.postValue(new ApiResponse<>(null, e.getMessage())));
    }

    public void signUpStaff(UserModel user, String password, MutableLiveData<ApiResponse<String, String>> userAuthListener) {
        FirebaseFirestore.getInstance()
                .collection("staff_ids")
                .document(user.getStaffNo())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        boolean isRegistered = documentSnapshot.getBoolean("registered");
                        if(isRegistered){
                            userAuthListener.postValue(new ApiResponse<>(null, "Sorry the staff ID provided is already a member, please login with it's credentials."));
                        }else{
                           registerStaff(user, password, userAuthListener);
                        }
                    }else{
                        userAuthListener.postValue(new ApiResponse<>(null, "Sorry the staff ID provided is not valid or approved by the admin. Please contact admin."));
                    }
                })
                .addOnFailureListener(e -> {

                });
    }

    private void registerStaff(UserModel user, String password, MutableLiveData<ApiResponse<String, String>> userAuthListener) {

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
