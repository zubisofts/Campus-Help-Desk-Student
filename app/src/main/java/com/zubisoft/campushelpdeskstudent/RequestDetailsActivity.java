package com.zubisoft.campushelpdeskstudent;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.zubisoft.campushelpdeskstudent.adapters.StaffSelectionListAdapter;
import com.zubisoft.campushelpdeskstudent.databinding.RequestStatusBottomsheetLayoutBinding;
import com.zubisoft.campushelpdeskstudent.databinding.ActivityRequestDetailsBinding;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);
        requestViewModel =
                new ViewModelProvider(this).get(RequestViewModel.class);

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

        if (getIntent().getStringExtra("type").equals("admin") || getIntent().getStringExtra("type").equals("staff")) {
            binding.assignLayout.setVisibility(View.VISIBLE);
        } else {
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
                if(!req.getModeratorId().isEmpty()){
                    userViewModel.fetchUser(req.getModeratorId());
                   
                }
                initDetails(req);
            }
        });


        binding.btnAssign.setOnClickListener(v -> showBottomSheetDialog(users, request));
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
}