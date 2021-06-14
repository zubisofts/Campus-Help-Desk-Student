package com.zubisoft.campushelpdeskstudent.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zubisoft.campushelpdeskstudent.R;
import com.zubisoft.campushelpdeskstudent.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class StaffListAdapter extends RecyclerView.Adapter<StaffListAdapter.StaffItemViewHolder> {

    List<UserModel> staffs =new ArrayList<>();
    private StaffSelectionListener staffSelectionListener;

    @NonNull
    @Override
    public StaffItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_list_item, parent, false);
        return new StaffItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffItemViewHolder holder, int position) {
        UserModel student= staffs.get(position);
        if (student!=null){
            holder.txtName.setText(student.getFullName());
            holder.txtEmail.setText(student.getEmail());
            holder.txtStaffNo.setText(student.getStaffNo());
           holder.itemView.setOnClickListener(v -> staffSelectionListener.onStaffSelected(student));
        }
    }

    @Override
    public int getItemCount() {
        return staffs.size();
    }

    public void setStaffs(List<UserModel> staffs) {
        this.staffs = staffs;
        notifyDataSetChanged();
    }

    public void setStaffSelectionListener(StaffSelectionListener staffSelectionListener) {
        this.staffSelectionListener = staffSelectionListener;
    }

    static class StaffItemViewHolder extends RecyclerView.ViewHolder{

        private TextView txtName,txtEmail, txtStaffNo;
        public StaffItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.txtName);
            txtEmail=itemView.findViewById(R.id.txtEmail);
            txtStaffNo=itemView.findViewById(R.id.txtStaffNo);
        }
    }

    public interface StaffSelectionListener {
        void onStaffSelected(UserModel staff);
    }
}
