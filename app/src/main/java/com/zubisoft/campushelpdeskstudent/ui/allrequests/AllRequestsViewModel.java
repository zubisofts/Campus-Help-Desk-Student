package com.zubisoft.campushelpdeskstudent.ui.allrequests;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AllRequestsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AllRequestsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}