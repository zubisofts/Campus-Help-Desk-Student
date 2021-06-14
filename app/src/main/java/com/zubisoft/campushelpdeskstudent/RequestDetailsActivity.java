package com.zubisoft.campushelpdeskstudent;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.zubisoft.campushelpdeskstudent.adapters.StaffSelectionListAdapter;
import com.zubisoft.campushelpdeskstudent.databinding.NewStaffNumberLayoutBinding;
import com.zubisoft.campushelpdeskstudent.databinding.RequestStatusBottomsheetLayoutBinding;
import com.zubisoft.campushelpdeskstudent.databinding.ActivityRequestDetailsBinding;
import com.zubisoft.campushelpdeskstudent.databinding.StaffResponseLayoutBinding;
import com.zubisoft.campushelpdeskstudent.models.ApiResponse;
import com.zubisoft.campushelpdeskstudent.models.Request;
import com.zubisoft.campushelpdeskstudent.models.UserModel;
import com.zubisoft.campushelpdeskstudent.repository.DataRepository;
import com.zubisoft.campushelpdeskstudent.utils.AppUtils;
import com.zubisoft.campushelpdeskstudent.viewmodels.RequestViewModel;
import com.zubisoft.campushelpdeskstudent.viewmodels.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RequestDetailsActivity extends AppCompatActivity {

    private ActivityRequestDetailsBinding binding;
    private UserViewModel userViewModel;
    private RequestViewModel requestViewModel;
    private List<UserModel> users = new ArrayList<>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);
        requestViewModel =
                new ViewModelProvider(this).get(RequestViewModel.class);

        progressDialog=new ProgressDialog(this);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Request request = (Request) getIntent().getSerializableExtra("request");
//        initDetails(request);

        requestViewModel.fetchRequestDetails(request.getId());

        userViewModel.onUserFetched().observe(this, userResponse -> {
            if (userResponse.getError() == null) {
                UserModel user = userResponse.getData();
                binding.txtStaffName.setText(user.getFullName());
                binding.txtStaffPhone.setText(user.getPhoneNumber());
                binding.assignedStaffLayout.setVisibility(View.VISIBLE);
                binding.unassignedStaffLayout.setVisibility(View.GONE);
            }
        });

        if (getIntent().getStringExtra("type").equals("admin")) {
            binding.assignLayout.setVisibility(View.VISIBLE);
            binding.btnAssign.setText("Assign Staff");
        }else if(getIntent().getStringExtra("type").equals("staff")) {
            binding.assignLayout.setVisibility(View.VISIBLE);
            binding.btnAssign.setText("Respond");
        }else {
            binding.assignLayout.setVisibility(View.GONE);
        }

        userViewModel.fetchAllUsers();
        userViewModel.onUsersFetched().observe(this, usersResponse -> {
            if (usersResponse.getError() == null) {
                users = usersResponse.getData();
            }
        });

        requestViewModel.onRequestDetailsFetched().observe(this, requestResponse -> {
            if(requestResponse.getError()==null){
                Request req=requestResponse.getData();
                binding.btnStatus.setBackgroundColor(AppUtils.getStatusColor(req.getStatus()));
                binding.btnStatus.setText(req.getStatus());
                binding.txtResponse.setText(request.getResponse());
                if(!req.getModeratorId().isEmpty()){
                    userViewModel.fetchUser(req.getModeratorId());
                   
                }
                initDetails(req);
            }
        });

        requestViewModel.onResponseSent().observe(this, response -> {
            if(response.getError()==null){
                Snackbar.make(binding.getRoot(), response.getData(), Snackbar.LENGTH_LONG).show();
            }else{
                Snackbar.make(binding.getRoot(), response.getError(), Snackbar.LENGTH_LONG).show();
            }
            hideDialog();
        });

        binding.btnAssign.setOnClickListener(v -> {

            if(getIntent().getStringExtra("type").equals("admin")){
                showBottomSheetDialog(users, request);
            }else{
                showResponseDialog(request);
            }
        });
    }



    private void initDetails(Request request) {
        String time = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a", Locale.getDefault()).format(request.getTimestamp());

        binding.txtDate.setText(time);
        binding.txtRequestTitle.setText(request.getTitle());
        binding.txtBody.setText(request.getBody());

        binding.btnStatus.setBackgroundColor(AppUtils.getStatusColor(request.getStatus()));
        binding.btnStatus.setText(request.getStatus());

        if (request.getModeratorId().isEmpty()) {
            binding.assignedStaffLayout.setVisibility(View.GONE);
            binding.unassignedStaffLayout.setVisibility(View.VISIBLE);
        } else {
            binding.assignedStaffLayout.setVisibility(View.GONE);
            binding.unassignedStaffLayout.setVisibility(View.VISIBLE);
        }

        if (request.getResponse().isEmpty()) {
            binding.responseStaffLayout.setVisibility(View.VISIBLE);
            binding.txtResponse.setText(request.getResponse());
        } else {
            binding.responseStaffLayout.setVisibility(View.GONE);
        }

    }

    private void showBottomSheetDialog(List<UserModel> users, Request request) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this) {

        };
        RequestStatusBottomsheetLayoutBinding layout = RequestStatusBottomsheetLayoutBinding.bind(View.inflate(this, R.layout.request_status_bottomsheet_layout, null));
        bottomSheetDialog.setContentView(layout.getRoot());

        List<UserModel> staffs = new ArrayList<>();
        for (UserModel userModel : users) {
            if (userModel.getType().equals("staff")) {
                staffs.add(userModel);
            }
        }

        StaffSelectionListAdapter adapter = new StaffSelectionListAdapter();
        adapter.setStaffSelectionListener(staff -> {
            new DataRepository().assignStaff(request.getId(), staff.getId());
            bottomSheetDialog.dismiss();
        });

        adapter.setStaffs(staffs);
        layout.staffRecyclerView.setAdapter(adapter);

        bottomSheetDialog.show();
    }
    private void showResponseDialog(Request request) {
        StaffResponseLayoutBinding layoutBinding=StaffResponseLayoutBinding.inflate(getLayoutInflater());
        View view=layoutBinding.getRoot();
        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("Send", (dialog, which) -> {
                    if(layoutBinding.edtResponse.getText().toString().isEmpty()){
                        Snackbar.make(binding.getRoot(), "You must enter a response to continue", Snackbar.LENGTH_LONG).show();
                    }else{
                        respondToRequest(request, layoutBinding.edtResponse.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void respondToRequest(Request request, String response) {
        showDialog();
        requestViewModel.respondToRequest(request, response);
    }

    private void showDialog(){
        progressDialog.setMessage("Sending response...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideDialog(){
        if(progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}