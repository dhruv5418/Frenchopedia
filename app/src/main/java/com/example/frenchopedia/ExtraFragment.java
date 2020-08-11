package com.example.frenchopedia;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.frenchopedia.Adapter.CourseAdapter;
import com.example.frenchopedia.Model.Courses;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class ExtraFragment extends Fragment {


    private RecyclerView recyclerView;
    DatabaseReference d;
    CourseAdapter courseAdapter;
    ArrayList<Courses> extras=new ArrayList<>();
    Toolbar toolbar;
    NavController navController;
    public ExtraFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_extra, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar=view.findViewById(R.id.toolbar_extra);
        recyclerView=view.findViewById(R.id.recyclerView_Extra);
        navController= Navigation.findNavController(getActivity(),R.id.nav_dashboard);
        getJson();
    }

    private void getJson() {
        Log.d("Json","called="+d);
        d = FirebaseDatabase.getInstance().getReference().child("Extras");
        d.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                        Courses l = npsnapshot.getValue(Courses.class);
                        extras.add(l);
                    }
                    generateView(extras);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity().getApplicationContext(),""+databaseError,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void generateView(ArrayList<Courses> extras) {
        courseAdapter = new CourseAdapter(extras, getActivity().getApplicationContext());
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
            Log.d("OnClick1","url="+position);
            String url= extras.get(position).getUrl();
            Log.d("OnClick","url="+url);
            Intent intent=new Intent();
            intent.putExtra("url",url);
            startActivity(intent);
        }
    };

}
