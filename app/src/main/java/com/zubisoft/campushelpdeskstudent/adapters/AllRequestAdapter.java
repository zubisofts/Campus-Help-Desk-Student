package com.zubisoft.campushelpdeskstudent.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class AllRequestAdapter extends RecyclerView.Adapter<AllRequestAdapter.RequestItemHolder> {

    private List<Request> requestList = new ArrayList<>();
    private RequestItemListener requestItemListener;

    @NonNull
    @Override
    public RequestItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_list_item, parent, false);
        return new RequestItemHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RequestItemHolder holder, int position) {
        Request request = requestList.get(position);
        holder.txtTitle.setText(request.getTitle());
        holder.txtBody.setText(request.getBody());
        String time = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a", Locale.getDefault()).format(request.getTimestamp());
        holder.txtDate.setText(time);
        holder.btnStatus.setBackgroundColor(AppUtils.getStatusColor(request.getStatus()));
        holder.btnStatus.setText(request.getStatus());
        holder.itemView.setOnClickListener(view -> holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), RequestDetailsActivity.class)));
        holder.menu.setOnClickListener(view -> {
            requestItemListener.onMenuClicked(request, holder.menu);
        });

        holder.itemView.setOnClickListener(v -> requestItemListener.onRequestItemClicked(request));
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
        notifyDataSetChanged();
    }

    public void setRequestItemListener(RequestItemListener requestItemListener) {
        this.requestItemListener = requestItemListener;
    }

    public class RequestItemHolder extends RecyclerView.ViewHolder {

        private final TextView txtTitle;
        private final TextView txtBody;
        private TextView txtDate;
        private final MaterialButton btnStatus;
        private final ImageView menu;

        public RequestItemHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtRequestTitle);
            txtBody = itemView.findViewById(R.id.txtBody);
            txtDate = itemView.findViewById(R.id.txtDate);
            btnStatus = itemView.findViewById(R.id.btnStatus);
            menu = itemView.findViewById(R.id.btn_menu);
        }
    }

    public interface RequestItemListener {
        void onRequestItemClicked(Request request);

        void onMenuClicked(Request request, View view);
    }

}
