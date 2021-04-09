package com.zubisoft.campushelpdeskstudent.ui.activerequests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.zubisoft.campushelpdeskstudent.R;

public class ActiveRequestsFragment extends Fragment {

    private ActiveRequestsViewModel activeRequestsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        activeRequestsViewModel =
                new ViewModelProvider(this).get(ActiveRequestsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_active_requests, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        activeRequestsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}