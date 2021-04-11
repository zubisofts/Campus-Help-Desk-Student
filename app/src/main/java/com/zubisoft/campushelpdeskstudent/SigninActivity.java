package com.zubisoft.campushelpdeskstudent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.tiper.MaterialSpinner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class SigninActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        final MaterialSpinner materialSpinner = findViewById(R.id.material_spinner);
        final MaterialSpinner SpnDpt = findViewById(R.id.spnDpt);
        MaterialButton btn_signUP = findViewById(R.id.btn_sigUp);

        btn_signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SigninActivity.this,MainActivity.class));
                finish();
            }
        });
        ArrayList<String> items = new ArrayList<>();
        items.add("NDI");
        items.add("NDII");
        items.add("HNDI");
        items.add("HNDII");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(SigninActivity.this,R.layout.support_simple_spinner_dropdown_item,items);
        materialSpinner.setAdapter(adapter);
        ArrayList<String> dpt = new ArrayList<>();
        dpt.add("Computer Science");
        dpt.add("Computer Engeneering");
        dpt.add("Electrical Engeneering");
        dpt.add("HNDII");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(SigninActivity.this,R.layout.support_simple_spinner_dropdown_item,dpt);
        SpnDpt.setAdapter(adapter1);

        SpnDpt.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                Toast.makeText(SigninActivity.this, materialSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });
        materialSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                Toast.makeText(SigninActivity.this, materialSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });
    }
}