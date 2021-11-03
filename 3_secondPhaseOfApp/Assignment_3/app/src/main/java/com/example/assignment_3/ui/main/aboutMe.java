package com.example.assignment_3.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.assignment_3.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link aboutMe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class aboutMe extends Fragment {



    public aboutMe() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_me, container, false);
        //return super.onCreateView(inflater, container, savedInstanceState);

    }
}