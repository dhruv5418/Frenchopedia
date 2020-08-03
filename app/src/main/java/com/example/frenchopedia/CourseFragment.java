package com.example.frenchopedia;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;


public class CourseFragment extends Fragment implements View.OnClickListener {

    LinearLayout l1;


    NavController navController;

Toolbar toolbar;

    public CourseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar=view.findViewById(R.id.toolbar_course);
        l1=view.findViewById(R.id.video1);
        l1.setOnClickListener(this);
        toolbar.inflateMenu(R.menu.tool_course);
        navController= Navigation.findNavController(getActivity(),R.id.nav_dashboard);
    }


    @Override
    public void onClick(View v) {
        Bundle b=new Bundle();
        switch (v.getId()){
            case R.id.video1:
                b.putInt("index",1);
                    navController.navigate(R.id.playvideoFragment,b);
        }

    }


}
