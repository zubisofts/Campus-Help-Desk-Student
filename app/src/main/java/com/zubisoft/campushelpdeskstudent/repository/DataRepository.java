package com.zubisoft.campushelpdeskstudent.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.zubisoft.campushelpdeskstudent.models.ApiResponse;
import com.zubisoft.campushelpdeskstudent.models.Request;

import java.util.ArrayList;
import java.util.List;

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

    public void fetchAllRequests(String uid, MutableLiveData<ApiResponse<List<Request>, String>> requestListListener) {

        db.collection("requests")
                .whereEqualTo("userId", uid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            List<Request> requests = new ArrayList<>();
                            for (DocumentSnapshot snapshot : value.getDocuments()) {
                                requests.add(snapshot.toObject(Request.class));
                            }
                            requestListListener.postValue(new ApiResponse<>(requests, null));
                        } else {
                            requestListListener.postValue(new ApiResponse<>(null, error.getMessage()));
                        }
                    }
                });

    }

    public void fetchAllRequests(String uid, String filter, MutableLiveData<ApiResponse<List<Request>, String>> requestListListener) {

        Query query;
        if (filter.isEmpty()) {
            query = db.collection("requests")
                    .whereEqualTo("userId", uid);
        } else {
            query = db.collection("requests")
                    .whereEqualTo("userId", uid)
                    .whereEqualTo("status", filter);
        }
        query.addSnapshotListener((value, error) -> {
            if (error == null) {
                List<Request> requests = new ArrayList<>();
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    requests.add(snapshot.toObject(Request.class));
                }
                requestListListener.postValue(new ApiResponse<>(requests, null));
            } else {
                requestListListener.postValue(new ApiResponse<>(null, error.getMessage()));
            }
        });

    }
}
