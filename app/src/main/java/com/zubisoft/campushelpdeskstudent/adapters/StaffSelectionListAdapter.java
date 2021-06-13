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

public class StaffSelectionListAdapter extends RecyclerView.Adapter<StaffSelectionListAdapter.SupplierItemViewHolder> {

    List<UserModel> staffs =new ArrayList<>();
    private StaffSelectionListener staffSelectionListener;

    @NonNull
    @Override
    public SupplierItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_selection_list_item, parent, false);
        return new SupplierItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplierItemViewHolder holder, int position) {
        UserModel staff= staffs.get(position);
        if (staff!=null){
            holder.txtName.setText(staff.getFullName());
            holder.txtEmail.setText(staff.getEmail());
           holder.btnSelectStaff.setOnClickListener(v -> staffSelectionListener.onItemSelected(staff));
        }
    }

    @Override
    public int getItemCount() {
        return staffs.size();
    }

    public void setStaffs(List<UserModel> users) {
        this.staffs = users;
        notifyDataSetChanged();
    }

    public void setStaffSelectionListener(StaffSelectionListener staffSelectionListener) {
        this.staffSelectionListener = staffSelectionListener;
    }

    static class SupplierItemViewHolder extends RecyclerView.ViewHolder{

        private TextView txtName,txtEmail;
        private MaterialButton btnSelectStaff;
        public SupplierItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.txtName);
            txtEmail=itemView.findViewById(R.id.txtEmail);
            btnSelectStaff=itemView.findViewById(R.id.btnSelectStaff);
        }
    }

    public interface StaffSelectionListener {
        void onItemSelected(UserModel staff);
    }
}
