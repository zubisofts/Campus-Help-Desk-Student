package com.zubisoft.campushelpdeskstudent;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.zubisoft.campushelpdeskstudent.databinding.ActivityRequestDetailsBinding;
import com.zubisoft.campushelpdeskstudent.models.Request;
import com.zubisoft.campushelpdeskstudent.utils.AppUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class RequestDetailsActivity extends AppCompatActivity {

    private ActivityRequestDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRequestDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Request request= (Request) getIntent().getSerializableExtra("request");
        initDetails(request);
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

        }

    }
}