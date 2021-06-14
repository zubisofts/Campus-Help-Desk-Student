package com.zubisoft.campushelpdeskstudent.features.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.os.Bundle;

import com.zubisoft.campushelpdeskstudent.adapters.StudentListAdapter;
import com.zubisoft.campushelpdeskstudent.databinding.ActivityStudentListBinding;
import com.zubisoft.campushelpdeskstudent.models.ApiResponse;
import com.zubisoft.campushelpdeskstudent.models.UserModel;
import com.zubisoft.campushelpdeskstudent.viewmodels.RequestViewModel;
import com.zubisoft.campushelpdeskstudent.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity implements StudentListAdapter.StudentSelectionListener {

    private ActivityStudentListBinding binding;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityStudentListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v->onBackPressed());

        userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        StudentListAdapter adapter=new StudentListAdapter();
        adapter.setStudentSelectionListener(this);
        binding.studentListRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.studentListRecycler.setAdapter(adapter);

        userViewModel.fetchAllUsers();
        userViewModel.onUsersFetched().observe(this, usersListResponse -> {
            if(usersListResponse.getError()==null){
                List<UserModel> users=usersListResponse.getData();
                List<UserModel> students=new ArrayList<>();
                for(UserModel user:users){
                    if(user.getType().equals("student")){
                        students.add(user);
                    }
                }

                adapter.setStudents(students);
            }
        });
    }

    @Override
    public void onStudentSelected(UserModel staff) {

    }
}