package com.zubisoft.campushelpdeskstudent.ui.activerequests;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.zubisoft.campushelpdeskstudent.R;
import com.zubisoft.campushelpdeskstudent.RequestDetailsActivity;
import com.zubisoft.campushelpdeskstudent.adapters.AllRequestAdapter;
import com.zubisoft.campushelpdeskstudent.databinding.FragmentActiveRequestsBinding;
import com.zubisoft.campushelpdeskstudent.models.ApiResponse;
import com.zubisoft.campushelpdeskstudent.models.Request;
import com.zubisoft.campushelpdeskstudent.viewmodels.RequestViewModel;

import java.util.ArrayList;
import java.util.List;

public class ActiveRequestsFragment extends Fragment implements AllRequestAdapter.RequestItemListener {

    private RequestViewModel requestViewModel;
    private FragmentActiveRequestsBinding binding;
    private String uid;
    private String type;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        requestViewModel =
                new ViewModelProvider(this).get(RequestViewModel.class);
        binding=FragmentActiveRequestsBinding.bind(inflater.inflate(R.layout.fragment_active_requests, container, false));

        AllRequestAdapter adapter = new AllRequestAdapter();
        adapter.setRequestItemListener(this);
        binding.requestsRecycler.setAdapter(adapter);
        uid=getActivity().getIntent().getStringExtra("uid");
        type=getActivity().getIntent().getStringExtra("type");
        requestViewModel.fetchRequests(uid);

        requestViewModel.onRequestListFetched().observe(getViewLifecycleOwner(), response -> {
            if(response.getError()==null){
                List<Request> requests=response.getData();
                List<Request> activeRequests=new ArrayList<>();
                for(Request request:requests){
                    if(request.getStatus().equals("processing")){
                        activeRequests.add(request);
                    }
                }
                if (activeRequests.isEmpty()){
                    binding.emptyLayout.setVisibility(View.VISIBLE);
                    binding.requestsRecycler.setVisibility(View.GONE);
                }else {
                    binding.emptyLayout.setVisibility(View.GONE);
                    binding.requestsRecycler.setVisibility(View.VISIBLE);
                    adapter.setRequestList(activeRequests);
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onRequestItemClicked(Request request) {
        Intent intent=new Intent(getActivity(), RequestDetailsActivity.class);
        intent.putExtra("request", request);
        intent.putExtra("type",type);
        startActivity(intent);
    }

    @Override
    public void onMenuClicked(Request request, View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(R.menu.request_menu);
        popupMenu.show();
    }
}