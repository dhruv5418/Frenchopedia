package com.example.frenchopedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.frenchopedia.Adapter.PracticesupportAdapter;
import com.example.frenchopedia.Model.Material;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

public class PracticesupportActivity extends AppCompatActivity {
    String value;
    PracticesupportAdapter practiceAdapter;
    DatabaseReference d;
    ArrayList<Material> days = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practicesupport);
        Intent intent = getIntent();
        value = intent.getStringExtra("Title");
        select(value);
    }

    private void select(String value) {
            switch (value) {
                case "Days":
                    d = FirebaseDatabase.getInstance().getReference().child("Days of Week");
                    Log.d("MainFragment", "idmon=" + d);
                    loadJson();
                    break;
                case "Numbers":
                    d= FirebaseDatabase.getInstance().getReference().child("Numbers");
                    Log.d("MainFragment","idmon="+d);
                    loadJson();
                    break;
                case "Colors": d= FirebaseDatabase.getInstance().getReference().child("Colors");
                    Log.d("MainFragment","idmon="+d);
                    loadJson();
                    break;
                case "Saison": d= FirebaseDatabase.getInstance().getReference().child("Saison");
                    Log.d("MainFragment","idmon="+d);
                    loadJson();
                    break;
                case "Weather": d= FirebaseDatabase.getInstance().getReference().child("Weather");
                    Log.d("MainFragment","idmon="+d);
                    loadJson();
                    break;
                case "Months": d= FirebaseDatabase.getInstance().getReference().child("Months");
                    Log.d("MainFragment","idmon="+d);
                    loadJson();
                    break;
            }
    }


    private void loadJson() {
        Log.d("Json","called="+d);
        //Toast.makeText(getApplicationContext(),"Load Json"+d,Toast.LENGTH_LONG).show();
        d.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                        Material l = npsnapshot.getValue(Material.class);
                        days.add(l);
                    }
                    generateView(days);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),""+databaseError,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void generateView(ArrayList<Material> materials) {
        practiceAdapter = new PracticesupportAdapter(materials, getApplicationContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView=findViewById(R.id.recyclerView_PracticeS);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(practiceAdapter);
        Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_LONG).show();
        practiceAdapter.setOnClickListner(onClickListener);
    }
    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();

            String url= days.get(position).getPronounciation();
            Log.d("OnClick","url="+url);
            try {
                MediaPlayer player = new MediaPlayer();
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setDataSource(url);
                player.prepare();
                player.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(),days.get(position).getFrench(),Toast.LENGTH_SHORT).show();
        }
    };

}