package com.zubisoft.campushelpdeskstudent.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zubisoft.campushelpdeskstudent.models.ApiResponse;
import com.zubisoft.campushelpdeskstudent.models.Request;

public class DataRepository {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void submitRequest(Request request, MutableLiveData<ApiResponse<String, String>> requestListener) {
        DocumentReference ref = db.collection("requests")
                .document();
        request.setId(ref.getId());
        ref.set(request).addOnSuccessListener(aVoid -> {
            requestListener.postValue(new ApiResponse<>(ref.getId(), null));
        }).addOnFailureListener(e -> {
            requestListener.postValue(new ApiResponse<>(null, e.getMessage()));
        });
    }

}
