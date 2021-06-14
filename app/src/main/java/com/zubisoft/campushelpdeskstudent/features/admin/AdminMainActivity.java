package com.zubisoft.campushelpdeskstudent.features.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.zubisoft.campushelpdeskstudent.databinding.NewStaffNumberLayoutBinding;
import com.zubisoft.campushelpdeskstudent.models.ApiResponse;
import com.zubisoft.campushelpdeskstudent.models.Request;
import com.zubisoft.campushelpdeskstudent.models.UserModel;
import com.zubisoft.campushelpdeskstudent.viewmodels.RequestViewModel;
import com.zubisoft.campushelpdeskstudent.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class AdminMainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{

    private ActivityAdminMainBinding binding;
    private UserViewModel userViewModel;
    private RequestViewModel requestViewModel;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog=new ProgressDialog(this);

        requestViewModel =
                new ViewModelProvider(this).get(RequestViewModel.class);
        userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        setSupportActionBar(binding.toolbar);
        RecentListAdapter adapter = new RecentListAdapter(getIntent().getStringExtra("type"));
        binding.recentRecycler.setAdapter(adapter);

        binding.btnRequests.setOnClickListener(v -> {
            Intent intent=new Intent(AdminMainActivity.this, AdminRequestListActivity.class);
            startActivity(intent);
        });

        binding.btnStaffs.setOnClickListener(v->{
            Intent intent=new Intent(AdminMainActivity.this, StaffListActivity.class);
            startActivity(intent);
        });

        binding.btnStudents.setOnClickListener(v->{
            Intent intent=new Intent(AdminMainActivity.this, StudentListActivity.class);
            startActivity(intent);
        });

        binding.btnAssigned.setOnClickListener(v -> {
            Intent intent=new Intent(AdminMainActivity.this, AdminRequestListActivity.class);
            intent.putExtra("query", "processing");
            startActivity(intent);
        });

        userViewModel.fetchAllUsers();
        userViewModel.onUsersFetched().observe(this, usersResponse -> {
            List<UserModel> users=usersResponse.getData();
            if(usersResponse.getError()==null){
                List<UserModel> students=new ArrayList<>();
                List<UserModel> staffs=new ArrayList<>();
                for (UserModel userModel:users){
                    if(userModel.getType().equals("student")){
                        students.add(userModel);
                    }else if(userModel.getType().equals("staff")){
                        staffs.add(userModel);
                    }
                }

                binding.txtTotalStudents.setText(String.valueOf(students.size()));
                binding.txtTotalStaffs.setText(String.valueOf(staffs.size()));
            }
        });

        requestViewModel.fetchAllRequests("");
        requestViewModel.onAllRequestFetched().observe(this, response -> {
            if (response.getError() == null) {
                List<Request> requests=response.getData();
                List<Request> activeRequests=new ArrayList<>();
                for(Request request:requests){
                    if(request.getStatus().equals("processing")){
                        activeRequests.add(request);
                    }
                }
                binding.txtTotalRequests.setText(String.valueOf(requests.size()));
                binding.txtAssignedRequests.setText(String.valueOf(activeRequests.size()));

                if(requests.size()>3){
                    List<Request> recentRequests=requests.subList(0,3);
                    adapter.setRequestsArrayList(recentRequests);
                }else{
                    adapter.setRequestsArrayList(requests);
                }

                if(requests.isEmpty()){
                    binding.emptyLayout.setVisibility(View.VISIBLE);
                    binding.recentRecycler.setVisibility(View.GONE);
                }else{
                    binding.emptyLayout.setVisibility(View.GONE);
                    binding.recentRecycler.setVisibility(View.VISIBLE);
                }
            }
        });

        userViewModel.onStaffNumberAdded().observe(this, addStaffNumberResponse -> {
            if(addStaffNumberResponse.getError()==null){
                Snackbar.make(binding.getRoot(), addStaffNumberResponse.getData(), Snackbar.LENGTH_LONG).show();
            }else{
                Snackbar.make(binding.getRoot(), addStaffNumberResponse.getError(), Snackbar.LENGTH_LONG).show();
            }

            hideDialog();
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logout){
            FirebaseAuth.getInstance().signOut();
        }else if(item.getItemId()==R.id.staff_number){
            showAddStaffNumberDialog();
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
        if(firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void showAddStaffNumberDialog() {
        NewStaffNumberLayoutBinding layoutBinding=NewStaffNumberLayoutBinding.inflate(getLayoutInflater());
        View view=layoutBinding.getRoot();
        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("Add", (dialog, which) -> {
                    if(layoutBinding.edtStaffNo.getText().toString().isEmpty()){
                        Snackbar.make(binding.getRoot(), "You must enter staff number to continue", Snackbar.LENGTH_LONG).show();
                    }else{
                        addStaffNumber(layoutBinding.edtStaffNo.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
        .show();

    }

    private void addStaffNumber(String id) {
        showDialog();
        userViewModel.addStaffNumber(id);
    }

    private void showDialog(){
        progressDialog.setMessage("Adding staff number...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideDialog(){
        if(progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}