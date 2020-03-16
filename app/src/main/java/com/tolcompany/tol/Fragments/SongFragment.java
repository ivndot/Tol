package com.tolcompany.tol.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.tolcompany.tol.R;


public class SongFragment extends Fragment {

    public SongFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //ponemos a que layout va a mostrar el fragment
        View view=inflater.inflate(R.layout.fragment_song, container, false);
        return view;
    }

}
