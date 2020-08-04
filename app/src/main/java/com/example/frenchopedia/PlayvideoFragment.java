package com.example.frenchopedia;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


public class PlayvideoFragment extends Fragment {

    int a;
    Uri uri;
    VideoView videoView;
    public PlayvideoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        videoView = (VideoView) view.findViewById(R.id.video_v);
        if(getArguments()!=null){
            a=getArguments().getInt("index");
        }
        Log.d("MainFragment","id="+a);
        indexVideo(a);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_playvideo, container, false);
    }

    private void indexVideo(int a) {
        switch (a){
            case 1: uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/frenchopedia-70435.appspot.com/o/course%2FFrench%20for%20Beginners%20%20Count%20from%201%20to%20100%20in%20French.mp4?alt=media&token=3d5ed9a5-9142-4f88-9bcc-683cb01b4d01");
                     playVideo();
        }

    }

    private void playVideo() {
        Toast.makeText(getActivity().getApplicationContext(),"Done",Toast.LENGTH_LONG).show();
        Log.d("video=","a="+a);
        MediaController mediaController =new MediaController(getActivity());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.start();
    }
}