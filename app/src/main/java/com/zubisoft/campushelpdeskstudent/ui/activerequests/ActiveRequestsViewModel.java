package com.zubisoft.campushelpdeskstudent.ui.activerequests;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ActiveRequestsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ActiveRequestsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}