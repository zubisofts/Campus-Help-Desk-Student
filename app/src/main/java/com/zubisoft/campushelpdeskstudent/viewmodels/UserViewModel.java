package com.zubisoft.campushelpdeskstudent.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zubisoft.campushelpdeskstudent.models.ApiResponse;
import com.zubisoft.campushelpdeskstudent.models.Request;
import com.zubisoft.campushelpdeskstudent.models.UserModel;
import com.zubisoft.campushelpdeskstudent.repository.DataRepository;

import java.util.List;

public class UserViewModel extends ViewModel {

    private DataRepository dataRepository = new DataRepository();

    private final MutableLiveData<ApiResponse<UserModel, String>> userListener = new MutableLiveData<>();

    public void fetchUser(String uid) {
        dataRepository.fetchUser(uid, userListener);
    }

    public MutableLiveData<ApiResponse<UserModel, String>> onUserFetched() {
        return userListener;
    }
}
