package com.zubisoft.campushelpdeskstudent.features.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.zubisoft.campushelpdeskstudent.R;
import com.zubisoft.campushelpdeskstudent.RequestDetailsActivity;
import com.zubisoft.campushelpdeskstudent.adapters.AllRequestAdapter;
import com.zubisoft.campushelpdeskstudent.databinding.ActivityAdminRequestListBinding;
import com.zubisoft.campushelpdeskstudent.databinding.RequestFilterBottomsheetLayoutBinding;
import com.zubisoft.campushelpdeskstudent.models.Request;
import com.zubisoft.campushelpdeskstudent.viewmodels.RequestViewModel;

import java.util.List;

public class AdminRequestListActivity extends AppCompatActivity implements AllRequestAdapter.RequestItemListener {

    private ActivityAdminRequestListBinding binding;
    private RequestViewModel requestViewModel;

    private int filterIndex=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAdminRequestListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        requestViewModel =
                new ViewModelProvider(this).get(RequestViewModel.class);

        AllRequestAdapter adapter = new AllRequestAdapter();
        adapter.setRequestItemListener(this);
        binding.requestsRecycler.setAdapter(adapter);

//        String query=getIntent().getStringExtra("query");
//        if(query != null){
//            requestViewModel.fetchAllRequests(query);
//            getSupportActionBar().setTitle("Currently Assigned");
//        }else{
//            requestViewModel.fetchAllRequests("");
//        }

        requestViewModel.onAllRequestFetched().observe(this, response -> {
            if(response.getError()==null){
                List<Request> requests=response.getData();
                if (requests.isEmpty()){
                    binding.emptyLayout.setVisibility(View.VISIBLE);
                    binding.requestsRecycler.setVisibility(View.GONE);
                }else {
                    binding.emptyLayout.setVisibility(View.GONE);
                    binding.requestsRecycler.setVisibility(View.VISIBLE);
                    adapter.setRequestList(requests);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.request_list_menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.filter_request){
            showBottomSheetDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestItemClicked(Request request) {
        Intent intent=new Intent(this, RequestDetailsActivity.class);
        intent.putExtra("type","admin");
        intent.putExtra("request",request);
        startActivity(intent);
    }

    @Override
    public void onMenuClicked(Request request, View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.request_menu);
        popupMenu.show();
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this){

        };
        RequestFilterBottomsheetLayoutBinding layout=RequestFilterBottomsheetLayoutBinding.bind(View.inflate(this,R.layout.request_filter_bottomsheet_layout,null));
        bottomSheetDialog.setContentView(layout.getRoot());
        for(int i=0; i<layout.getRoot().getChildCount(); i++){
            MaterialCardView cardView = (MaterialCardView) layout.getRoot().getChildAt(i);
            cardView.setCheckable(true);
            cardView.setChecked(i + 1 == filterIndex);

        }

        layout.btnAll.setOnClickListener(v -> {
            requestViewModel.fetchAllRequests("");
            filterIndex=1;
            bottomSheetDialog.dismiss();
        });

        layout.btnPending.setOnClickListener(v -> {
            requestViewModel.fetchAllRequests("pending");
            filterIndex=2;
            bottomSheetDialog.dismiss();
        });

        layout.btnProcessing.setOnClickListener(v -> {
            requestViewModel.fetchAllRequests("processing");
            filterIndex=3;
            bottomSheetDialog.dismiss();
        });

        layout.btnCompleted.setOnClickListener(v -> {
            requestViewModel.fetchAllRequests("complete");
            filterIndex=4;
            bottomSheetDialog.dismiss();
        });


        bottomSheetDialog.show();
    }
}