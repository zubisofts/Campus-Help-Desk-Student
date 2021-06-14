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
import com.zubisoft.campushelpdeskstudent.models.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void fetchUser(String uid, MutableLiveData<ApiResponse<UserModel, String>> userListener) {
        db.collection("users")
                .document(uid)
                .addSnapshotListener((value, error) -> {
                    if(error==null){
                        userListener.postValue(new ApiResponse<>(value.toObject(UserModel.class), null));
                    }else{
                        userListener.postValue(new ApiResponse<>(null, error.getMessage()));
                    }
                });

    }

    public void saveUser(UserModel user, MutableLiveData<ApiResponse<String, String>> userAuthListener) {
        db.collection("users")
                .document(user.getId())
                .set(user)
                .addOnSuccessListener(aVoid -> userAuthListener.postValue(new ApiResponse<>(user.getId(), null)))
                .addOnFailureListener(e -> userAuthListener.postValue(new ApiResponse<>(null, e.getMessage())));
    }

    public void fetchAllUsers(MutableLiveData<ApiResponse<List<UserModel>, String>> allUsersListener) {
        db.collection("users")
                .addSnapshotListener((value, error) -> {
                    if(error==null){
                        List<UserModel> users=new ArrayList<>();
                        for(DocumentSnapshot snapshot:value){
                            users.add(snapshot.toObject(UserModel.class));
                        }
                        allUsersListener.postValue(new ApiResponse<>(users, null));
                    }else{
                        allUsersListener.postValue(new ApiResponse<>(null, error.getMessage()));
                    }
                });
    }

    public void fetchAllUsersRequest(String queryFilter, MutableLiveData<ApiResponse<List<Request>, String>> allRequestListListener) {

        Query query;
        if (queryFilter.isEmpty()) {
            query = db.collection("requests");
        } else {
            query = db.collection("requests")
                    .whereEqualTo("status", queryFilter);
        }
        query.addSnapshotListener((value, error) -> {
            if (error == null) {
                List<Request> requests = new ArrayList<>();
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    requests.add(snapshot.toObject(Request.class));
                }
                allRequestListListener.postValue(new ApiResponse<>(requests, null));
            } else {
                allRequestListListener.postValue(new ApiResponse<>(null, error.getMessage()));
            }
        });

    }

    public void fetchRequestDetails(String id, MutableLiveData<ApiResponse<Request, String>> requestDetailsListener) {
        db.collection("requests")
                .document(id)
                .addSnapshotListener((value, error) -> {
                    if(error==null){
                        requestDetailsListener.postValue(new ApiResponse<>(value.toObject(Request.class), null));
                    }else{
                        requestDetailsListener.postValue(new ApiResponse<>(null, error.getMessage()));
                    }
                });
    }

    public void assignStaff(String requestId, String staffId){
        Map<String, Object> data=new HashMap<>();
        data.put("moderatorId", staffId);
        data.put("status", "processing");
        db.collection("requests")
                .document(requestId)
                .update(data);
    }

    public void addStaffNumber(String id, MutableLiveData<ApiResponse<String, String>> addStaffNumberListener) {

        db.collection("staff_ids")
                .document(id)
                .get().addOnSuccessListener(documentSnapshots -> {

                    if (!documentSnapshots.exists()){
                        insertStaffId(id, addStaffNumberListener);
                    }else{
                        addStaffNumberListener.postValue(new ApiResponse<>(null, "Sorry this staff ID already exists."));
                    }

                }).addOnFailureListener(e -> addStaffNumberListener.postValue(new ApiResponse<>(null, e.getMessage())));

    }

    private void insertStaffId(String id, MutableLiveData<ApiResponse<String, String>> addStaffNumberListener) {

        HashMap<String,Object> data=new HashMap<>();
        data.put("id",id);
        data.put("registered", false);

        db.collection("staff_ids")
                .document(id)
                .set(data)
                .addOnSuccessListener(aVoid -> addStaffNumberListener.postValue(new ApiResponse<>("Successfully added", null)))
                .addOnFailureListener(e -> addStaffNumberListener.postValue(new ApiResponse<>(null, e.getMessage())));

    }

//    public void addStaffNumber(String id, MutableLiveData<ApiResponse<String, String>> addStaffNumberListener) {
//
//        db.collection("staff_ids")
//                .whereEqualTo("id", id)
//                .get().addOnSuccessListener(queryDocumentSnapshots -> {
//
//            if (queryDocumentSnapshots.isEmpty()){
//                addStaffNumberListener.postValue(new ApiResponse<>(null, "Sorry this staff ID is invalid or not approved by the admin, please contact the admin."));
//            }else{
//                DocumentSnapshot snapshot = queryDocumentSnapshots.getDocuments().get(0);
//                snapshot.getString()
//            }
//
//        }).addOnFailureListener(e -> addStaffNumberListener.postValue(new ApiResponse<>(null, e.getMessage())));
//
//    }
}
