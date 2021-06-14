package com.zubisoft.campushelpdeskstudent.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.zubisoft.campushelpdeskstudent.R;
import com.zubisoft.campushelpdeskstudent.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.SupplierItemViewHolder> {

    List<UserModel> students =new ArrayList<>();
    private StudentSelectionListener studentSelectionListener;

    @NonNull
    @Override
    public SupplierItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_item, parent, false);
        return new SupplierItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplierItemViewHolder holder, int position) {
        UserModel student= students.get(position);
        if (student!=null){
            holder.txtName.setText(student.getFullName());
            holder.txtEmail.setText(student.getEmail());
            holder.txtRegNo.setText(student.getRegNo());
           holder.itemView.setOnClickListener(v -> studentSelectionListener.onStudentSelected(student));
        }
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public void setStudents(List<UserModel> users) {
        this.students = users;
        notifyDataSetChanged();
    }

    public void setStudentSelectionListener(StudentSelectionListener studentSelectionListener) {
        this.studentSelectionListener = studentSelectionListener;
    }

    static class SupplierItemViewHolder extends RecyclerView.ViewHolder{

        private TextView txtName,txtEmail, txtRegNo;
        public SupplierItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.txtName);
            txtEmail=itemView.findViewById(R.id.txtEmail);
            txtRegNo=itemView.findViewById(R.id.txtRegNo);
        }
    }

    public interface StudentSelectionListener {
        void onStudentSelected(UserModel staff);
    }
}
