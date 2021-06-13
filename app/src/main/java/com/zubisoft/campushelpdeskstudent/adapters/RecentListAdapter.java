package com.zubisoft.campushelpdeskstudent.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.zubisoft.campushelpdeskstudent.R;
import com.zubisoft.campushelpdeskstudent.RequestDetailsActivity;
import com.zubisoft.campushelpdeskstudent.models.Request;
import com.zubisoft.campushelpdeskstudent.utils.AppUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class RecentListAdapter extends  RecyclerView.Adapter<RecentListAdapter.RecentItemHolder> {

    private List<Request> requestsArrayList = new ArrayList<>();

    @NonNull
    @Override
    public RecentItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_request_list_item,parent,false);
        return new RecentItemHolder(view);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecentItemHolder holder, int position) {
        Request request = requestsArrayList.get(position);
        holder.txtTitle.setText(request.getTitle());
        holder.txtBody.setText(request.getBody());
        String time=new SimpleDateFormat("EEE, d MMM yyyy HH:mm a", Locale.getDefault()).format(request.getTimestamp());
        holder.txtDate.setText(time);
        holder.btnStatus.setBackgroundColor(AppUtils.getStatusColor(request.getStatus()));
        holder.btnStatus.setText(request.getStatus());

        holder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(holder.itemView.getContext(), RequestDetailsActivity.class);
            intent.putExtra("request", request);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return requestsArrayList.size();
    }

    public void setRequestsArrayList(List<Request> requestsArrayList) {
        this.requestsArrayList = requestsArrayList;
        notifyDataSetChanged();
    }

    public class RecentItemHolder extends RecyclerView.ViewHolder{
        private TextView txtTitle;
        private  TextView txtBody;
        private  TextView txtDate;
        private MaterialButton btnStatus;
        public RecentItemHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtRequestTitle);
            txtBody = itemView.findViewById(R.id.txtBody);
            txtDate = itemView.findViewById(R.id.txtDate);
            btnStatus = itemView.findViewById(R.id.btnStatus);
        }
    }
}
