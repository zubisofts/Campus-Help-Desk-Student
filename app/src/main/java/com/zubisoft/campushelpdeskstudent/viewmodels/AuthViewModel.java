package com.zubisoft.campushelpdeskstudent.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zubisoft.campushelpdeskstudent.models.ApiResponse;
import com.zubisoft.campushelpdeskstudent.models.UserModel;
import com.zubisoft.campushelpdeskstudent.repository.AuthRepository;
import com.zubisoft.campushelpdeskstudent.repository.DataRepository;

public class AuthViewModel extends ViewModel {

    private AuthRepository authRepository=new AuthRepository();

    private MutableLiveData<ApiResponse<String, String>> userAuthListener=new MutableLiveData<>();
    private MutableLiveData<ApiResponse<UserModel, String>> userAuthLoginListener=new MutableLiveData<>();

    public void loginUser(String email, String password){
        authRepository.loginUser(email, password, userAuthLoginListener);
    }

    public void signUpUser(UserModel user, String password){
        authRepository.signUpUser(user, password, userAuthListener);
    }

    public MutableLiveData<ApiResponse<String, String>> onAuthResponse(){
        return userAuthListener;
    }

    public void fetchUserDetails(String uid){
        new DataRepository().fetchUser(uid, userAuthLoginListener);
    }

    public MutableLiveData<ApiResponse<UserModel, String>> onAuthLoginResponse(){
        return userAuthLoginListener;
    }

}
