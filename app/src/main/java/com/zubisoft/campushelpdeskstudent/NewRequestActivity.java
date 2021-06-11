package com.zubisoft.campushelpdeskstudent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.zubisoft.campushelpdeskstudent.databinding.ActivityNewRequestBinding;
import com.zubisoft.campushelpdeskstudent.models.ApiResponse;
import com.zubisoft.campushelpdeskstudent.models.Request;
import com.zubisoft.campushelpdeskstudent.utils.InputListener;
import com.zubisoft.campushelpdeskstudent.viewmodels.RequestViewModel;

import java.util.Date;

public class NewRequestActivity extends AppCompatActivity {

    private ActivityNewRequestBinding binding;
    private ProgressDialog dialog;
    private RequestViewModel requestViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        requestViewModel = new ViewModelProvider.NewInstanceFactory().create(RequestViewModel.class);
        dialog = new ProgressDialog(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();

        binding.btnSubmitRequest.setOnClickListener(v -> {
            if (validateFields()) {
                sendRequest();
            }
        });

        requestViewModel.onSubmitRequest().observe(this, response -> {
            if (response.getError() == null) {
                Toast.makeText(this, "Request has been successfully sent", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, response.getError(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendRequest() {
        String userId = getIntent().getStringExtra("uid");
        Request request = new Request(
                binding.edtTitle.getText().toString(),
                binding.edtRequestBody.getText().toString(),
                userId, "pending",
                "",
                new Date().getTime());
        showLoadingDialog("Submitting your request...");
        requestViewModel.submitRequest(request);
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.save_menu,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.save){
////          na just to save am
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private boolean validateFields() {
        if (binding.edtTitle.getText().toString().isEmpty()) {
            return false;
        } else return !binding.edtRequestBody.getText().toString().isEmpty();
    }

    private void initViews() {
        binding.edtTitle.addTextChangedListener(new InputListener(binding.inputTitle));
        binding.edtRequestBody.addTextChangedListener(new InputListener(binding.inputRequestBody));
    }

    private void showLoadingDialog(String message) {
        dialog.setMessage(message);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void hideDialog() {
        if (dialog.isShowing()) {
            dialog.hide();
        }
    }
}