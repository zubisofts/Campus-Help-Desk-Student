package com.zubisoft.campushelpdeskstudent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tiper.MaterialSpinner;
import com.zubisoft.campushelpdeskstudent.adapters.SignUpTabAdapter;
import com.zubisoft.campushelpdeskstudent.databinding.ActivitySignupBinding;
import com.zubisoft.campushelpdeskstudent.models.ApiResponse;
import com.zubisoft.campushelpdeskstudent.models.UserModel;
import com.zubisoft.campushelpdeskstudent.ui.signup.StaffSignUpFragment;
import com.zubisoft.campushelpdeskstudent.ui.signup.StudentSignUpFragment;
import com.zubisoft.campushelpdeskstudent.utils.InputListener;
import com.zubisoft.campushelpdeskstudent.viewmodels.AuthViewModel;
import com.zubisoft.campushelpdeskstudent.viewmodels.RequestViewModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Date;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SignUpTabAdapter adapter=new SignUpTabAdapter(this);
        adapter.addFragment(StudentSignUpFragment.newInstance());
        adapter.addFragment(StaffSignUpFragment.newInstance());

        binding.pager.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLayout, binding.pager,
                (tab, position) -> tab.setText(position==0 ? "Student":"Staff")
        ).attach();
    }
}