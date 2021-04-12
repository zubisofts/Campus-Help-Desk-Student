package com.zubisoft.campushelpdeskstudent.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.zubisoft.campushelpdeskstudent.R;
import com.zubisoft.campushelpdeskstudent.RequestDetails;

import java.util.ArrayList;

public class AllRequestAdapter extends RecyclerView.Adapter<AllRequestAdapter.RequestItemHolder> {

        ArrayList<RecentListAdapter.Requests> requestsArrayList = new ArrayList<>();
    public AllRequestAdapter() {
        this.requestsArrayList = RecentListAdapter.Requests.getRequests();
    }


    @NonNull
    @Override
    public RequestItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_list_iem,parent,false);
      return new RequestItemHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RequestItemHolder holder, int position) {
        RecentListAdapter.Requests requests = requestsArrayList.get(position);
        holder.txt_tittle.setText(requests.tittle);
        holder.txt_date.setText(requests.date);
        if (requests.status.equals("pending")){
            holder.btn_status.setBackgroundColor( R.color.red);
            holder.btn_status.setText(requests.status);

        }else {
            holder.btn_status.setText(requests.status);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), RequestDetails.class));
            }
        });
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(),holder.menu);
                popupMenu.inflate(R.menu.request_menu);
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestsArrayList.size();
    }

    public  class RequestItemHolder extends RecyclerView.ViewHolder{

        private TextView txt_tittle;
        private  TextView txt_date;
        private MaterialButton btn_status;
        private ImageView menu;
        public RequestItemHolder(@NonNull View itemView) {
            super(itemView);
            txt_tittle = itemView.findViewById(R.id.txtRequestTitle);
            txt_date = itemView.findViewById(R.id.txtDate);
            btn_status = itemView.findViewById(R.id.btnStatus);
            menu = itemView.findViewById(R.id.btn_menu);
        }
    }

}
