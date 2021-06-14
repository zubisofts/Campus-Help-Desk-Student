package com.zubisoft.campushelpdeskstudent.features.admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.zubisoft.campushelpdeskstudent.adapters.StaffListAdapter;
import com.zubisoft.campushelpdeskstudent.adapters.StudentListAdapter;
import com.zubisoft.campushelpdeskstudent.databinding.ActivityStaffListBinding;
import com.zubisoft.campushelpdeskstudent.models.UserModel;
import com.zubisoft.campushelpdeskstudent.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class StaffListActivity extends AppCompatActivity implements StudentListAdapter.StudentSelectionListener {

    private ActivityStaffListBinding binding;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityStaffListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v->onBackPressed());

        userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        StaffListAdapter adapter=new StaffListAdapter();
        binding.staffListRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.staffListRecycler.setAdapter(adapter);

        userViewModel.fetchAllUsers();
        userViewModel.onUsersFetched().observe(this, usersListResponse -> {
            if(usersListResponse.getError()==null){
                List<UserModel> users=usersListResponse.getData();
                List<UserModel> staffs=new ArrayList<>();
                for(UserModel user:users){
                    if(user.getType().equals("staff")){
                        staffs.add(user);
                    }
                }

                adapter.setStaffs(staffs);
            }
        });
    }

    @Override
    public void onStudentSelected(UserModel staff) {

    }
}