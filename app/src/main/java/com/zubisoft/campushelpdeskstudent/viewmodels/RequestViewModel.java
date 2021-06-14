package com.zubisoft.campushelpdeskstudent.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zubisoft.campushelpdeskstudent.models.ApiResponse;
import com.zubisoft.campushelpdeskstudent.models.Request;
import com.zubisoft.campushelpdeskstudent.models.UserModel;
import com.zubisoft.campushelpdeskstudent.repository.DataRepository;

import java.util.List;

public class RequestViewModel extends ViewModel {

    private DataRepository dataRepository = new DataRepository();

    private final MutableLiveData<ApiResponse<String, String>> requestListener = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse<List<Request>, String>> requestListListener = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse<Request, String>> requestDetailsListener = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse<List<Request>, String>> allRequestListListener = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse<UserModel, String>> userListener = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse<String, String>> requestResponseListener = new MutableLiveData<>();

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

    public void fetchRequestDetails(String id) {
        dataRepository.fetchRequestDetails(id, requestDetailsListener);
    }

    public MutableLiveData<ApiResponse<Request, String>> onRequestDetailsFetched() {
        return requestDetailsListener;
    }

    public MutableLiveData<ApiResponse<List<Request>, String>> onRequestListFetched() {
        return requestListListener;
    }

    public void fetchUser(String uid) {
        dataRepository.fetchUser(uid, userListener);
    }

    public MutableLiveData<ApiResponse<UserModel, String>> onUserFetched() {
        return userListener;
    }

    public void fetchAllRequests(String query) {
        dataRepository.fetchAllUsersRequest(query,allRequestListListener);
    }

    public MutableLiveData<ApiResponse<List<Request>, String>> onAllRequestFetched() {
        return allRequestListListener;
    }

    public void respondToRequest(Request request, String response) {
        dataRepository.respondToRequest(request, response, requestResponseListener);
    }

    public MutableLiveData<ApiResponse<String, String>> onResponseSent(){
        return requestResponseListener;
    }
}
