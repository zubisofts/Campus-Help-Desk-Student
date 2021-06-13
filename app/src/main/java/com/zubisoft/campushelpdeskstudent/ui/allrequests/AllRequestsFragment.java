package com.zubisoft.campushelpdeskstudent.ui.allrequests;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.zubisoft.campushelpdeskstudent.R;
import com.zubisoft.campushelpdeskstudent.RequestDetailsActivity;
import com.zubisoft.campushelpdeskstudent.adapters.AllRequestAdapter;
import com.zubisoft.campushelpdeskstudent.databinding.FragmentAllRequestsBinding;
import com.zubisoft.campushelpdeskstudent.databinding.RequestFilterBottomsheetLayoutBinding;
import com.zubisoft.campushelpdeskstudent.models.ApiResponse;
import com.zubisoft.campushelpdeskstudent.models.Request;
import com.zubisoft.campushelpdeskstudent.viewmodels.RequestViewModel;

import java.util.List;

public class AllRequestsFragment extends Fragment implements AllRequestAdapter.RequestItemListener {

    private RequestViewModel requestViewModel;
    private FragmentAllRequestsBinding binding;
    private int filterIndex=1;
    private String uid;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        requestViewModel =
                new ViewModelProvider(this).get(RequestViewModel.class);
        binding=FragmentAllRequestsBinding.bind(inflater.inflate(R.layout.fragment_all_requests, container, false));

        AllRequestAdapter adapter = new AllRequestAdapter();
        adapter.setRequestItemListener(this);
        binding.requestsRecycler.setAdapter(adapter);

        uid=getActivity().getIntent().getStringExtra("uid");
        requestViewModel.fetchRequests(uid);
        requestViewModel.onRequestListFetched().observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<Request>, String>>() {
            @Override
            public void onChanged(ApiResponse<List<Request>, String> response) {
                if(response.getError()==null){
                    List<Request> requests=response.getData();
                    if (requests.isEmpty()){
                        binding.emptyLayout.setVisibility(View.VISIBLE);
                    }else {
                        binding.emptyLayout.setVisibility(View.GONE);
                        adapter.setRequestList(requests);
                    }
                }
            }
        });

        return binding.getRoot();
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext()){

        };
        RequestFilterBottomsheetLayoutBinding layout=RequestFilterBottomsheetLayoutBinding.bind(View.inflate(getContext(),R.layout.request_filter_bottomsheet_layout,null));
        bottomSheetDialog.setContentView(layout.getRoot());
        for(int i=0; i<layout.getRoot().getChildCount(); i++){
            MaterialCardView cardView = (MaterialCardView) layout.getRoot().getChildAt(i);
            cardView.setCheckable(true);
            cardView.setChecked(i + 1 == filterIndex);

        }

       layout.btnAll.setOnClickListener(v -> {
           requestViewModel.fetchRequests(uid, "");
           filterIndex=1;
           bottomSheetDialog.dismiss();
       });

        layout.btnPending.setOnClickListener(v -> {
            requestViewModel.fetchRequests(uid, "pending");
            filterIndex=2;
            bottomSheetDialog.dismiss();
        });

        layout.btnProcessing.setOnClickListener(v -> {
            requestViewModel.fetchRequests(uid, "processing");
            filterIndex=3;
            bottomSheetDialog.dismiss();
        });

        layout.btnCompleted.setOnClickListener(v -> {
            requestViewModel.fetchRequests(uid, "complete");
            filterIndex=4;
            bottomSheetDialog.dismiss();
        });


        bottomSheetDialog.show();
    }

    @Override
    public void onRequestItemClicked(Request request) {
        Intent intent=new Intent(getActivity(), RequestDetailsActivity.class);
        intent.putExtra("request", request);
        startActivity(intent);
    }

    @Override
    public void onMenuClicked(Request request, View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(R.menu.request_menu);
        popupMenu.show();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.clear();
       getActivity().getMenuInflater().inflate(R.menu.request_list_menu_main,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.filter_request){
            showBottomSheetDialog();
        }
        return super.onOptionsItemSelected(item);
    }
}