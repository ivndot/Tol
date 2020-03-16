package com.tolcompany.tol.Fragments;

import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

import com.tolcompany.tol.R;


public class HistoryFragment extends Fragment {
    private VideoView video;
    private Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_history, container, false);

        video=view.findViewById(R.id.videoView_History);
        uri=Uri.parse("https://firebasestorage.googleapis.com/v0/b/platzigram-5dbcb.appspot.com/o/bad%20decisions.mp4?alt=media&token=ac6e2f67-ce23-43d5-80ab-955e65470766");
        video.setVideoURI(uri);
        video.requestFocus();
        video.start();
        return view;
    }
}
