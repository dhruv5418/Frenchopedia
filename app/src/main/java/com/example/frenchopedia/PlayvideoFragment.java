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
        if(getArguments()!=null) {
            a = getArguments().getInt("index");
        }

        Log.d("MainFragment", "id=" + a);
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
                     break;
            case 2: uri =Uri.parse("https://firebasestorage.googleapis.com/v0/b/frenchopedia-70435.appspot.com/o/course%2FLearn%20French.%20Pronunciation%20-%20French%20alphabet%20(l'alphabet%20fran%C3%A7ais).mp4?alt=media&token=e28cfcef-8d39-480a-8265-2ccdc09d67d9");
                    playVideo();
                    break;
            case 3: uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/frenchopedia-70435.appspot.com/o/course%2FHow%20to%20tell%20time%20in%20French.mp4?alt=media&token=e1b27863-afaf-4ae6-9142-e15d0e650ba0");
                   playVideo();
                    break;

            case 4: uri= Uri.parse("https://firebasestorage.googleapis.com/v0/b/frenchopedia-70435.appspot.com/o/course%2FTop%2010%20Basic%20French%20Words%20Every%20BEGINNER%20Should%20Know.mp4?alt=media&token=1054e45e-6da1-49ca-bb78-68da56bcbb91");
                   playVideo();
                   break;

            case 5: uri= Uri.parse("https://firebasestorage.googleapis.com/v0/b/frenchopedia-70435.appspot.com/o/course%2Fvideo5.mp4?alt=media&token=890df3e6-7e82-441c-a22d-90f3d648edac");
                   playVideo();
                   break;
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