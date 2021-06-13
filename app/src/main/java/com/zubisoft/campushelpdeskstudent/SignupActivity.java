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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tiper.MaterialSpinner;
import com.zubisoft.campushelpdeskstudent.models.ApiResponse;
import com.zubisoft.campushelpdeskstudent.models.UserModel;
import com.zubisoft.campushelpdeskstudent.utils.InputListener;
import com.zubisoft.campushelpdeskstudent.viewmodels.AuthViewModel;
import com.zubisoft.campushelpdeskstudent.viewmodels.RequestViewModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Date;

public class SignupActivity extends AppCompatActivity {

    private TextInputEditText edtName, edtRegNo, edtEmail, edtPhone, edtPassword;
    private TextInputLayout inputName, inputRegNo, inputEmail, inputPhoneNumber, inputPassword;
    private MaterialSpinner spinnerLevel;
    private MaterialSpinner spinnerDept;
    private ProgressDialog progressDialog;

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        authViewModel =
                new ViewModelProvider(this).get(AuthViewModel.class);

        progressDialog=new ProgressDialog(this);

//        Material Spinners
        spinnerLevel = findViewById(R.id.spinnerLevel);
        spinnerDept = findViewById(R.id.spinnerDept);

//        TextInputLayouts
        inputName=findViewById(R.id.inputName);
        inputRegNo=findViewById(R.id.inputRegNo);
        inputEmail=findViewById(R.id.inputEmail);
        inputPhoneNumber=findViewById(R.id.inputPhone);
        inputPassword=findViewById(R.id.inputPassword);

//        TextInputEditexts
        edtName=findViewById(R.id.edtName);
        edtRegNo=findViewById(R.id.edtRegNo);
        edtEmail=findViewById(R.id.edtEmail);
        edtPhone=findViewById(R.id.edtPhone);
        edtPassword=findViewById(R.id.edtPassword);

        attacheListenersToEditTexts();

        MaterialButton btn_signUP = findViewById(R.id.btn_sigUp);

        btn_signUP.setOnClickListener(view -> {
            if(isFieldsValidated()){
                showDialog();
                saveStudentToDatabase();
            }else{
                Toast.makeText(SignupActivity.this, "Make all fields are filled", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.txtLogin).setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });

        ArrayList<String> items = new ArrayList<>();
        items.add("NDI");
        items.add("NDII");
        items.add("HNDI");
        items.add("HNDII");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(SignupActivity.this,R.layout.support_simple_spinner_dropdown_item,items);
        spinnerLevel.setAdapter(adapter);

        ArrayList<String> dpt = new ArrayList<>();
        dpt.add("Computer Science");
        dpt.add("Computer Engeneering");
        dpt.add("Electrical Engeneering");
        dpt.add("HNDII");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(SignupActivity.this,R.layout.support_simple_spinner_dropdown_item,dpt);
        spinnerDept.setAdapter(adapter1);

        spinnerDept.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                Toast.makeText(SignupActivity.this, materialSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });
        spinnerLevel.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                Toast.makeText(SignupActivity.this, materialSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });

        authViewModel.onAuthResponse().observe(this, authResponse -> {
            if(authResponse.getError()==null){
                Toast.makeText(SignupActivity.this, "User account successfully created", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(SignupActivity.this, MainActivity.class);
                intent.putExtra("uid", authResponse.getData());
                startActivity(intent);
                finish();
            }else{
                Snackbar.make(btn_signUP, authResponse.getError(), Snackbar.LENGTH_LONG).show();
            }

            hideDialog();
        });
    }

    private void saveStudentToDatabase() {

        UserModel userModel=new UserModel(
                edtName.getText().toString(),
                edtRegNo.getText().toString(),
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