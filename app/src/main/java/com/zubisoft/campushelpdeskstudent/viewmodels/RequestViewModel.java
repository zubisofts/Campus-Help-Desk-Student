package com.zubisoft.campushelpdeskstudent.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zubisoft.campushelpdeskstudent.models.ApiResponse;
import com.zubisoft.campushelpdeskstudent.models.Request;
import com.zubisoft.campushelpdeskstudent.repository.AuthRepository;
import com.zubisoft.campushelpdeskstudent.repository.DataRepository;

public class RequestViewModel extends ViewModel {

    private DataRepository dataRepository=new DataRepository();

    private final MutableLiveData<ApiResponse<String, String>> requestListener=new MutableLiveData<>();

    public void submitRequest(Request request){
        dataRepository.submitRequest(request, requestListener);
    }

    public MutableLiveData<ApiResponse<String, String>> onSubmitRequest(){
        return requestListener;
    }

}
