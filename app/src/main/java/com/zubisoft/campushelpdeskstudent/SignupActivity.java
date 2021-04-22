package com.zubisoft.campushelpdeskstudent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tiper.MaterialSpinner;
import com.zubisoft.campushelpdeskstudent.models.Student;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {

    private TextInputEditText edtName, edtRegNo, edtEmail, edtPassword;
    private TextInputLayout inputName, inputRegNo, inputEmail, inputPassword;
    private MaterialSpinner spinnerLevel;
    private MaterialSpinner spinnerDept;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressDialog=new ProgressDialog(this);

//        Material Spinners
        spinnerLevel = findViewById(R.id.spinnerLevel);
        spinnerDept = findViewById(R.id.spinnerDept);

//        TextInputLayouts
        inputName=findViewById(R.id.inputName);
        inputRegNo=findViewById(R.id.inputRegNo);
        inputEmail=findViewById(R.id.inputEmail);
        inputPassword=findViewById(R.id.inputPassword);

//        TextInputEditexts
        edtName=findViewById(R.id.edtName);
        edtRegNo=findViewById(R.id.edtRegNo);
        edtEmail=findViewById(R.id.edtEmail);
        edtPassword=findViewById(R.id.edtPassword);

        attatchListenersToEditTexts();

        MaterialButton btn_signUP = findViewById(R.id.btn_sigUp);

        btn_signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFieldsValidated()){
                    showDialog();
                    saveStudentToDatabase();
                }else{
                    Toast.makeText(SignupActivity.this, "Make all fields are filled", Toast.LENGTH_SHORT).show();
                }
            }
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
    }

    private void saveStudentToDatabase() {

        Student student=new Student();
        student.setFullName(edtName.getText().toString());
        student.setRegNo(edtRegNo.getText().toString());
        student.setLevel(spinnerLevel.getSelectedItem().toString());
        student.setDepartment(spinnerDept.getSelectedItem().toString());
        student.setEmail(edtEmail.getText().toString());

        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        String id=authResult.getUser().getUid();
                        student.setId(id);
                        saveUser(student);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignupActivity.this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });

    }

    private void saveUser(Student student) {
        FirebaseFirestore.getInstance()
                .collection("students")
                .add(student)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(SignupActivity.this, "Student data saved successfully", Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignupActivity.this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });
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

    private void attatchListenersToEditTexts() {

       edtName.addTextChangedListener(new InputListener(inputName));
       edtEmail.addTextChangedListener(new InputListener(inputEmail));
       edtRegNo.addTextChangedListener(new InputListener(inputRegNo));
       edtPassword.addTextChangedListener(new InputListener(inputPassword));
       spinnerLevel.getEditText().addTextChangedListener(new InputListener(spinnerLevel));
       spinnerDept.getEditText().addTextChangedListener(new InputListener(spinnerDept));

    }

    private void showDialog(){
        progressDialog.setMessage("Savinf student data...");
        progressDialog.setCancelable(false);
        progressDialog.show();;
    }

    private void hideDialog(){
        if(progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public class InputListener implements TextWatcher{

        private TextInputLayout inputLayout;

        public InputListener(TextInputLayout textInputLayout){
            this.inputLayout=textInputLayout;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(TextUtils.isEmpty(s)){
                inputLayout.setError("This field must not be empty");
            }else{
                inputLayout.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}