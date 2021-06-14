package com.zubisoft.campushelpdeskstudent.ui.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tiper.MaterialSpinner;
import com.zubisoft.campushelpdeskstudent.LoginActivity;
import com.zubisoft.campushelpdeskstudent.MainActivity;
import com.zubisoft.campushelpdeskstudent.R;
import com.zubisoft.campushelpdeskstudent.SignupActivity;
import com.zubisoft.campushelpdeskstudent.models.UserModel;
import com.zubisoft.campushelpdeskstudent.utils.InputListener;
import com.zubisoft.campushelpdeskstudent.viewmodels.AuthViewModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Date;

public class StudentSignUpFragment extends Fragment {

    private TextInputEditText edtName, edtRegNo, edtEmail, edtPhone, edtPassword;
    private TextInputLayout inputName, inputRegNo, inputEmail, inputPhoneNumber, inputPassword;
    private MaterialSpinner spinnerLevel;
    private MaterialSpinner spinnerDept;
    private ProgressDialog progressDialog;

    private AuthViewModel authViewModel;

    public StudentSignUpFragment() {
        // Required empty public constructor
    }

    public static StudentSignUpFragment newInstance() {
        StudentSignUpFragment fragment = new StudentSignUpFragment();
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
        View root=inflater.inflate(R.layout.fragment_student_signup, container, false);

        authViewModel =
                new ViewModelProvider(this).get(AuthViewModel.class);

        progressDialog=new ProgressDialog(getActivity());

//        Material Spinners
        spinnerLevel = root.findViewById(R.id.spinnerLevel);
        spinnerDept = root.findViewById(R.id.spinnerDept);

//        TextInputLayouts
        inputName=root.findViewById(R.id.inputName);
        inputRegNo=root.findViewById(R.id.inputRegNo);
        inputEmail=root.findViewById(R.id.inputEmail);
        inputPhoneNumber=root.findViewById(R.id.inputPhone);
        inputPassword=root.findViewById(R.id.inputPassword);

//        TextInputEditexts
        edtName=root.findViewById(R.id.edtName);
        edtRegNo=root.findViewById(R.id.edtRegNo);
        edtEmail=root.findViewById(R.id.edtEmail);
        edtPhone=root.findViewById(R.id.edtPhone);
        edtPassword=root.findViewById(R.id.edtPassword);

        attacheListenersToEditTexts();

        MaterialButton btn_signUP = root.findViewById(R.id.btn_sigUp);

        btn_signUP.setOnClickListener(view -> {
            if(isFieldsValidated()){
                showDialog();
                saveStudentToDatabase();
            }else{
                Toast.makeText(getActivity(), "Make all fields are filled", Toast.LENGTH_SHORT).show();
            }
        });

        root.findViewById(R.id.txtLogin).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });

        ArrayList<String> items = new ArrayList<>();
        items.add("NDI");
        items.add("NDII");
        items.add("HNDI");
        items.add("HNDII");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,items);
        spinnerLevel.setAdapter(adapter);

        ArrayList<String> dpt = new ArrayList<>();
        dpt.add("Computer Science");
        dpt.add("Computer Engeneering");
        dpt.add("Electrical Engeneering");
        dpt.add("HNDII");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,dpt);
        spinnerDept.setAdapter(adapter1);

        spinnerDept.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                Toast.makeText(getActivity(), materialSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });
        spinnerLevel.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                Toast.makeText(getActivity(), materialSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });

        authViewModel.onAuthResponse().observe(getViewLifecycleOwner(), authResponse -> {
            if(authResponse.getError()==null){
                Toast.makeText(getActivity(), "User account successfully created", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(), MainActivity.class);
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

    private void saveStudentToDatabase() {

        UserModel userModel=new UserModel(
                edtName.getText().toString(),
                edtRegNo.getText().toString(),
                "",
                "",
                spinnerLevel.getSelectedItem().toString(),
                spinnerDept.getSelectedItem().toString(),
                "student",
                edtEmail.getText().toString(),
                edtPhone.getText().toString(),
                new Date().getTime()
        );
        authViewModel.signUpUser(userModel, edtPassword.getText().toString());
    }

    private boolean isFieldsValidated() {

        if(edtName.getText().toString().isEmpty()){
            inputName.setError("Field must not be empty");
            return false;
        }else if(edtRegNo.getText().toString().isEmpty()){
            inputRegNo.setError("Field must not be empty");
            return false;
        }else if(spinnerLevel.getSelectedItemId()==MaterialSpinner.INVALID_POSITION){
            spinnerLevel.setError("Please select an option");
            return false;
        }else if(spinnerDept.getSelectedItemId()==MaterialSpinner.INVALID_POSITION){
            spinnerDept.setError("Please select an option");
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

        edtName.addTextChangedListener(new InputListener(inputName));
        edtEmail.addTextChangedListener(new InputListener(inputEmail));
        edtPhone.addTextChangedListener(new InputListener(inputPhoneNumber));
        edtRegNo.addTextChangedListener(new InputListener(inputRegNo));
        edtPassword.addTextChangedListener(new InputListener(inputPassword));
        spinnerLevel.getEditText().addTextChangedListener(new InputListener(spinnerLevel));
        spinnerDept.getEditText().addTextChangedListener(new InputListener(spinnerDept));

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