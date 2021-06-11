package com.zubisoft.campushelpdeskstudent.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zubisoft.campushelpdeskstudent.models.ApiResponse;
import com.zubisoft.campushelpdeskstudent.repository.AuthRepository;

public class AuthViewModel extends ViewModel {

    private AuthRepository authRepository=new AuthRepository();

    private MutableLiveData<ApiResponse<String, String>> userAuthListener=new MutableLiveData<>();

    public void loginUser(String email, String password){
        authRepository.loginUser(email, password, userAuthListener);
    }

    public MutableLiveData<ApiResponse<String, String>> onAuthResponse(){
        return userAuthListener;
    }

}
