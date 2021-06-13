package com.zubisoft.campushelpdeskstudent.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.zubisoft.campushelpdeskstudent.R;
import com.zubisoft.campushelpdeskstudent.adapters.RecentListAdapter;
import com.zubisoft.campushelpdeskstudent.databinding.FragmentHomeBinding;
import com.zubisoft.campushelpdeskstudent.models.ApiResponse;
import com.zubisoft.campushelpdeskstudent.models.Request;
import com.zubisoft.campushelpdeskstudent.viewmodels.RequestViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class HomeFragment extends Fragment {

    private RequestViewModel requestViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentHomeBinding.bind(inflater.inflate(R.layout.fragment_home, container, false));
        requestViewModel =
                new ViewModelProvider(this).get(RequestViewModel.class);

        RecentListAdapter adapter = new RecentListAdapter();
        binding.recentRecycler.setAdapter(adapter);

        requestViewModel.fetchRequests(getActivity().getIntent().getStringExtra("uid"));
        requestViewModel.onRequestListFetched().observe(getViewLifecycleOwner(), response -> {
            if (response.getError() == null) {
                List<Request> requests=response.getData();
                List<Request> activeRequests=new ArrayList<>();
                for(Request request:requests){
                    if(request.getStatus().equals("processing")){
                        activeRequests.add(request);
                    }
                }
                binding.txtTotalRequest.setText(String.valueOf(requests.size()));
                binding.txtActiveRequests.setText(String.valueOf(activeRequests.size()));

                if(requests.size()>3){
                    List<Request> recentRequests=requests.subList(0,3);
                    adapter.setRequestsArrayList(recentRequests);
                }else{
                    adapter.setRequestsArrayList(requests);
                }

                if(requests.isEmpty()){
                    binding.emptyLayout.setVisibility(View.VISIBLE);
                    binding.recentRecycler.setVisibility(View.GONE);
                }else{
                    binding.emptyLayout.setVisibility(View.GONE);
                    binding.recentRecycler.setVisibility(View.VISIBLE);
                }
            }
        });

        return binding.getRoot();
    }
}