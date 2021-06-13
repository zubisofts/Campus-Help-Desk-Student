package com.zubisoft.campushelpdeskstudent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.zubisoft.campushelpdeskstudent.databinding.ActivityRequestDetailsBinding;
import com.zubisoft.campushelpdeskstudent.models.ApiResponse;
import com.zubisoft.campushelpdeskstudent.models.Request;
import com.zubisoft.campushelpdeskstudent.models.UserModel;
import com.zubisoft.campushelpdeskstudent.utils.AppUtils;
import com.zubisoft.campushelpdeskstudent.viewmodels.RequestViewModel;
import com.zubisoft.campushelpdeskstudent.viewmodels.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class RequestDetailsActivity extends AppCompatActivity {

    private ActivityRequestDetailsBinding binding;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRequestDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Request request= (Request) getIntent().getSerializableExtra("request");
        initDetails(request);

        userViewModel.onUserFetched().observe(this, userResponse -> {
            if(userResponse.getError()==null){
                UserModel user=userResponse.getData();
                binding.txtStaffName.setText(user.getFullName());
                binding.txtStaffPhone.setText(user.getPhoneNumber());
            }
        });
    }

    private void initDetails(Request request) {
        String time=new SimpleDateFormat("EEE, d MMM yyyy HH:mm a", Locale.getDefault()).format(request.getTimestamp());

        binding.txtDate.setText(time);
        binding.txtRequestTitle.setText(request.getTitle());
        binding.txtBody.setText(request.getBody());

        if (request.getStatus().equals("pending")) {
            binding.btnStatus.setBackgroundColor(AppUtils.getStatusColor(request.getStatus()));
        }

        if(request.getModeratorId().isEmpty()){
            binding.assignedStaffLayout.setVisibility(View.GONE);
            binding.unassignedStaffLayout.setVisibility(View.VISIBLE);
        }else{
            binding.assignedStaffLayout.setVisibility(View.GONE);
            binding.unassignedStaffLayout.setVisibility(View.VISIBLE);


        }

    }
}