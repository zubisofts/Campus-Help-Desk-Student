package com.zubisoft.campushelpdeskstudent.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zubisoft.campushelpdeskstudent.models.ApiResponse;
import com.zubisoft.campushelpdeskstudent.models.Request;
import com.zubisoft.campushelpdeskstudent.repository.DataRepository;

import java.util.List;

public class RequestViewModel extends ViewModel {

    private DataRepository dataRepository = new DataRepository();

    private final MutableLiveData<ApiResponse<String, String>> requestListener = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse<List<Request>, String>> requestListListener = new MutableLiveData<>();

    public void submitRequest(Request request) {
        dataRepository.submitRequest(request, requestListener);
    }

    public MutableLiveData<ApiResponse<String, String>> onSubmitRequest() {
        return requestListener;
    }

    public void fetchRequests(String uid) {
        dataRepository.fetchAllRequests(uid, requestListListener);
    }

    public void fetchRequests(String uid, String filter) {
        dataRepository.fetchAllRequests(uid, filter, requestListListener);
    }

    public MutableLiveData<ApiResponse<List<Request>, String>> onRequestListFetched() {
        return requestListListener;
    }
}
