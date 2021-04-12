package com.zubisoft.campushelpdeskstudent.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.zubisoft.campushelpdeskstudent.R;

import java.util.ArrayList;


public class RecentListAdapter extends  RecyclerView.Adapter<RecentListAdapter.RecentItemHolder> {

    int red = R.color.red;
    ArrayList<Requests> requestsArrayList = new ArrayList<>();
    public RecentListAdapter() {
        this.requestsArrayList = Requests.getRequests();
    }

    @NonNull
    @Override
    public RecentItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_list_iem,parent,false);
        return new RecentItemHolder(view);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecentItemHolder holder, int position) {
        Requests requests = requestsArrayList.get(position);
        holder.txt_tittle.setText(requests.tittle);
        holder.txt_date.setText(requests.date);
        if (requests.status.equals("pending")){
            holder.btn_status.setBackgroundColor( R.color.red);
            holder.btn_status.setText(requests.status);

        }else {
            holder.btn_status.setText(requests.status);
        }
    }

    @Override
    public int getItemCount() {
        return requestsArrayList.size();
    }

    public class RecentItemHolder extends RecyclerView.ViewHolder{
        private TextView txt_tittle;
        private  TextView txt_date;
        private MaterialButton btn_status;
        public RecentItemHolder(@NonNull View itemView) {
            super(itemView);
            txt_tittle = itemView.findViewById(R.id.txtRequestTitle);
            txt_date = itemView.findViewById(R.id.txtDate);
            btn_status = itemView.findViewById(R.id.btnStatus);
        }
    }

    static class  Requests {
       String tittle;
        String date;
        String status;

        public Requests(String tittle, String date,String status) {
            this.tittle = tittle;
            this.date = date;
            this.status = status;
        }

        public static ArrayList<Requests> getRequests(){
            ArrayList<Requests> requests = new ArrayList<>();
            requests.add(new Requests("No CBT","20th dec 2020","active"));
            requests.add(new Requests("No CBT","20th dec 2020","pending"));
            requests.add(new Requests("No CBT","20th dec 2020","pending"));
            return requests;
        }

    }
}
