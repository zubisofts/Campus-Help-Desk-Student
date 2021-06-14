package com.zubisoft.campushelpdeskstudent.features.staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.zubisoft.campushelpdeskstudent.LoginActivity;
import com.zubisoft.campushelpdeskstudent.R;
import com.zubisoft.campushelpdeskstudent.adapters.RecentListAdapter;
import com.zubisoft.campushelpdeskstudent.databinding.ActivityAdminMainBinding;
import com.zubisoft.campushelpdeskstudent.databinding.ActivityStaffMainBinding;
import com.zubisoft.campushelpdeskstudent.features.admin.AdminMainActivity;
import com.zubisoft.campushelpdeskstudent.features.admin.AdminRequestListActivity;
import com.zubisoft.campushelpdeskstudent.features.admin.StaffListActivity;
import com.zubisoft.campushelpdeskstudent.features.admin.StudentListActivity;
import com.zubisoft.campushelpdeskstudent.models.Request;
import com.zubisoft.campushelpdeskstudent.models.UserModel;
import com.zubisoft.campushelpdeskstudent.viewmodels.RequestViewModel;
import com.zubisoft.campushelpdeskstudent.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class StaffMainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private ActivityStaffMainBinding binding;
    private UserViewModel userViewModel;
    private RequestViewModel requestViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStaffMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        requestViewModel =
                new ViewModelProvider(this).get(RequestViewModel.class);
        userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        setSupportActionBar(binding.toolbar);
        RecentListAdapter adapter = new RecentListAdapter(getIntent().getStringExtra("type"));
        binding.recentRecycler.setAdapter(adapter);

        binding.btnAllRequests.setOnClickListener(v -> {
            Intent intent = new Intent(StaffMainActivity.this, AdminRequestListActivity.class);
            intent.putExtra("uid", getIntent().getStringExtra("uid"));
            intent.putExtra("query", "");
            startActivity(intent);
        });

        binding.btnStudents.setOnClickListener(v -> {
            Intent intent = new Intent(StaffMainActivity.this, StudentListActivity.class);
            startActivity(intent);
        });

        binding.btnCompleted.setOnClickListener(v -> {
            Intent intent = new Intent(StaffMainActivity.this, AdminRequestListActivity.class);
            intent.putExtra("query", "completed");
            intent.putExtra("uid", getIntent().getStringExtra("uid"));
            startActivity(intent);
        });

        binding.btnPending.setOnClickListener(v -> {
            Intent intent = new Intent(StaffMainActivity.this, AdminRequestListActivity.class);
            intent.putExtra("query", "processing");
            intent.putExtra("uid", getIntent().getStringExtra("uid"));
            startActivity(intent);
        });

        userViewModel.fetchAllUsers();
        userViewModel.onUsersFetched().observe(this, usersResponse -> {
            List<UserModel> users = usersResponse.getData();
            if (usersResponse.getError() == null) {
                List<UserModel> students = new ArrayList<>();
//                List<UserModel> staffs=new ArrayList<>();
                for (UserModel userModel : users) {
                    if (userModel.getType().equals("student")) {
                        students.add(userModel);
                    }
//                    else if(userModel.getType().equals("staff")){
//                        staffs.add(userModel);
//                    }
                }

                binding.txtTotalStudents.setText(String.valueOf(students.size()));
            }
        });

        requestViewModel.fetchAllRequests("");
        requestViewModel.onAllRequestFetched().observe(this, response -> {
            if (response.getError() == null) {
                List<Request> requests = response.getData();
                List<Request> reqs = new ArrayList<>();
                for (Request request : requests) {
                    if (request.getModeratorId().equals(getIntent().getStringExtra("uid"))) {
                        reqs.add(request);
                    }
                }
                List<Request> activeRequests = new ArrayList<>();
                List<Request> completedRequests = new ArrayList<>();
                for (Request request : reqs) {
                    if (request.getStatus().equals("processing")) {
                        activeRequests.add(request);
                    }else if(request.getStatus().equals("completed")){
                        completedRequests.add(request);
                    }
                }
                binding.txtTotalRequest.setText(String.valueOf(reqs.size()));
                binding.txtPendingRequests.setText(String.valueOf(activeRequests.size()));
                binding.txtCompletedRequests.setText(String.valueOf(completedRequests.size()));

                if (reqs.size() > 3) {
                    List<Request> recentRequests = reqs.subList(0, 3);
                    adapter.setRequestsArrayList(recentRequests);
                } else {
                    adapter.setRequestsArrayList(reqs);
                }

                if (reqs.isEmpty()) {
                    binding.emptyLayout.setVisibility(View.VISIBLE);
                    binding.recentRecycler.setVisibility(View.GONE);
                } else {
                    binding.emptyLayout.setVisibility(View.GONE);
                    binding.recentRecycler.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.staff_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}