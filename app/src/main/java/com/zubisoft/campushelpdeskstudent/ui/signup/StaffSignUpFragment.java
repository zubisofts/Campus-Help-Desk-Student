package com.zubisoft.campushelpdeskstudent.ui.signup;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zubisoft.campushelpdeskstudent.R;

public class StaffSignUpFragment extends Fragment {

    public StaffSignUpFragment() {
        // Required empty public constructor
    }

    public static StaffSignUpFragment newInstance() {
        StaffSignUpFragment fragment = new StaffSignUpFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_staff_signup, container, false);
    }
}