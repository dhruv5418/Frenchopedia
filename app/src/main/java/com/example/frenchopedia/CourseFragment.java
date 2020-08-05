package com.example.frenchopedia;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frenchopedia.Adapter.CourseAdapter;
import com.example.frenchopedia.Adapter.PracticeAdapter;
import com.example.frenchopedia.Adapter.PracticesupportAdapter;
import com.example.frenchopedia.Model.Courses;
import com.example.frenchopedia.Model.Material;
import com.example.frenchopedia.Model.Practice_;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;


public class CourseFragment extends Fragment{



    NavController navController;
    Toolbar toolbar;
    private RecyclerView recyclerView;
    CourseAdapter courseAdapter;
    ArrayList<Courses> courses=new ArrayList<>();
    DatabaseReference d;

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
        recyclerView=view.findViewById(R.id.recyclerView_Course);
        toolbar.inflateMenu(R.menu.tool_course);
        navController= Navigation.findNavController(getActivity(),R.id.nav_dashboard);
        getJson();
    }

    private void getJson() {
        Log.d("Json","called="+d);
        //Toast.makeText(getApplicationContext(),"Load Json"+d,Toast.LENGTH_LONG).show();
        d = FirebaseDatabase.getInstance().getReference().child("Courses");
        d.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                        Courses l = npsnapshot.getValue(Courses.class);
                        courses.add(l);
                    }
                    generateView(courses);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity().getApplicationContext(),""+databaseError,Toast.LENGTH_LONG).show();
            }
        });
    }
    private void generateView(ArrayList<Courses> courses) {
        courseAdapter = new CourseAdapter(courses, getActivity().getApplicationContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(courseAdapter);
        Toast.makeText(getActivity().getApplicationContext(),"done",Toast.LENGTH_LONG).show();
        courseAdapter.setOnClickListner(onClickListener);
    }
    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();

            String url= courses.get(position).getURL();
            Log.d("OnClick","url="+url);

        }
    };





   /* @Override
    public void onClick(View v) {
        Bundle b=new Bundle();
        switch (v.getId()){
            case R.id.video1:
                 b.putInt("index",1);
                 navController.navigate(R.id.playvideoFragment,b);
                 break;
            case R.id.video2:
                b.putInt("index",2);
                navController.navigate(R.id.playvideoFragment,b);
                break;

            case R.id.video3:
                b.putInt("index",3);
                navController.navigate(R.id.playvideoFragment,b);
                break;

            case R.id.video4:
                b.putInt("index",4);
                navController.navigate(R.id.playvideoFragment,b);
                break;

            case R.id.video5:
                b.putInt("index",5);
                navController.navigate(R.id.playvideoFragment,b);
                break;

                }

        }*/

    }


