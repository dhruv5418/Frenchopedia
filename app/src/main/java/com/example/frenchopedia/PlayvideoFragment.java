package com.example.frenchopedia;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class PlayvideoFragment extends Fragment {

    Uri uri;
    VideoView videoView;
    String u;
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
        if(getArguments()!=null) {
            u=getArguments().getString("url");
        }
        uri = Uri.parse(u);
        playVideo();
        super.onViewCreated(view, savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_playvideo, container, false);
    }



    private void playVideo() {
        Toast.makeText(getActivity().getApplicationContext(),"Playing Video",Toast.LENGTH_LONG).show();

        MediaController mediaController =new MediaController(getActivity());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.start();
    }
}