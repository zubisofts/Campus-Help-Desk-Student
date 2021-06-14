package com.zubisoft.campushelpdeskstudent.ui.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tiper.MaterialSpinner;
import com.zubisoft.campushelpdeskstudent.LoginActivity;
import com.zubisoft.campushelpdeskstudent.MainActivity;
import com.zubisoft.campushelpdeskstudent.R;
import com.zubisoft.campushelpdeskstudent.features.staff.StaffMainActivity;
import com.zubisoft.campushelpdeskstudent.models.UserModel;
import com.zubisoft.campushelpdeskstudent.utils.InputListener;
import com.zubisoft.campushelpdeskstudent.viewmodels.AuthViewModel;

import java.util.Date;

public class StaffSignUpFragment extends Fragment {

    private TextInputEditText edtName, edtWorkTitle, edtStaffNo, edtEmail, edtPhone, edtPassword;
    private TextInputLayout inputName, inputWorkTitle,  inputStaffNo, inputEmail, inputPhoneNumber, inputPassword;
    private ProgressDialog progressDialog;

    private AuthViewModel authViewModel;


    public StaffSignUpFragment() {
        // Required empty public constructor
    }

    public static StaffSignUpFragment newInstance() {
        StaffSignUpFragment fragment = new StaffSignUpFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_staff_signup, container, false);

        authViewModel =
                new ViewModelProvider(this).get(AuthViewModel.class);

        progressDialog=new ProgressDialog(getActivity());

//        TextInputLayouts
        inputName=root.findViewById(R.id.inputName);
        inputStaffNo =root.findViewById(R.id.inputStaffNo);
        inputWorkTitle =root.findViewById(R.id.inputWorkTitle);
        inputEmail=root.findViewById(R.id.inputEmail);
        inputPhoneNumber=root.findViewById(R.id.inputPhone);
        inputPassword=root.findViewById(R.id.inputPassword);

//        TextInputEditexts
        edtName=root.findViewById(R.id.edtName);
        edtStaffNo =root.findViewById(R.id.edtStaffNo);
        edtWorkTitle =root.findViewById(R.id.edtWorkTitle);
        edtEmail=root.findViewById(R.id.edtEmail);
        edtPhone=root.findViewById(R.id.edtPhone);
        edtPassword=root.findViewById(R.id.edtPassword);

        attacheListenersToEditTexts();

        MaterialButton btn_signUP = root.findViewById(R.id.btn_sigUp);

        btn_signUP.setOnClickListener(view -> {
            if(isFieldsValidated()){
                showDialog();
                saveStaffToDatabase();
            }else{
                Toast.makeText(getActivity(), "Make all fields are filled", Toast.LENGTH_SHORT).show();
            }
        });

        root.findViewById(R.id.txtLogin).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });


        authViewModel.onAuthResponse().observe(getViewLifecycleOwner(), authResponse -> {
            if(authResponse.getError()==null){
                Toast.makeText(getActivity(), "User account successfully created", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(), StaffMainActivity.class);
                intent.putExtra("uid", authResponse.getData());
                startActivity(intent);
                getActivity().finish();
            }else{
                Snackbar.make(btn_signUP, authResponse.getError(), Snackbar.LENGTH_LONG).show();
            }

            hideDialog();
        });

        return root;
    }

    private void saveStaffToDatabase() {

        UserModel userModel=new UserModel(
                edtName.getText().toString(),
                "",
                edtStaffNo.getText().toString(),
                edtWorkTitle.getText().toString(),
               "",
                "",
                "staff",
                edtEmail.getText().toString(),
                edtPhone.getText().toString(),
                new Date().getTime()
        );
        authViewModel.signUpUser(userModel, edtPassword.getText().toString());
    }

    private boolean isFieldsValidated() {

        if(edtStaffNo.getText().toString().isEmpty()){
            inputStaffNo.setError("Field must not be empty");
            return false;
        }else if(edtName.getText().toString().isEmpty()){
            inputName.setError("Field must not be empty");
            return false;
        }else if(edtWorkTitle.getText().toString().isEmpty()){
            inputWorkTitle.setError("Field must not be empty");
            return false;
        }else if(edtPhone.getText().toString().isEmpty()){
            inputPhoneNumber.setError("Field must not be empty");
            return false;
        }else if(edtEmail.getText().toString().isEmpty()){
            inputEmail.setError("Field must not be empty");
            return false;
        }else if(edtPassword.getText().toString().isEmpty()){
            inputPassword.setError("Field must not be empty");
            return false;
        }else{
            return true;
        }

    }

    private void attacheListenersToEditTexts() {

        edtStaffNo.addTextChangedListener(new InputListener(inputPhoneNumber));
        edtName.addTextChangedListener(new InputListener(inputName));
        edtWorkTitle.addTextChangedListener(new InputListener(inputWorkTitle));
        edtEmail.addTextChangedListener(new InputListener(inputEmail));
        edtPhone.addTextChangedListener(new InputListener(inputPhoneNumber));
        edtStaffNo.addTextChangedListener(new InputListener(inputStaffNo));
        edtPassword.addTextChangedListener(new InputListener(inputPassword));

    }

    private void showDialog(){
        progressDialog.setMessage("Creating your account...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideDialog(){
        if(progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}