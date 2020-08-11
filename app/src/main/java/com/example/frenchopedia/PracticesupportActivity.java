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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PracticesupportActivity extends AppCompatActivity {
    String value;
    String ref;
    PracticesupportAdapter practiceAdapter;
    DatabaseReference d;
    ArrayList<Material> days = new ArrayList<>();
    RecyclerView recyclerView;
    private FirebaseAuth auth;
    private FirebaseUser curUser;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practicesupport);
        Intent intent = getIntent();
        value = intent.getStringExtra("Title");
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        select(value);
        updateProgress();
    }

    private void updateProgress() {
        readData1(new FirestoreCallback() {
            @Override
            public void onClickback(DocumentSnapshot documentSnapshot) {
                double p=Double.valueOf(documentSnapshot.get("total").toString());
                int a=(int)(long) documentSnapshot.get(ref);
                Toast.makeText(getApplicationContext(),"Total"+p,Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"Ref="+a,Toast.LENGTH_LONG).show();
                if (a==0){
                    Log.d("supportPractice","Vaue="+documentSnapshot.get(ref).toString());
                    Map<String,Object> usermap=new HashMap<>();
                    usermap.put(ref,1);
                    p=p+16.67;
                    usermap.put("total",p);
                    db.collection("Users").document(curUser.getUid()).collection("Progress").document("ProgressPractice").update(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"Progress updated for"+ref,Toast.LENGTH_LONG).show();
                        }
                    });

                }else{
                    Toast.makeText(getApplicationContext(),"Progress Not updated for"+ref,Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void readData1(final FirestoreCallback firestoreCallback){
        curUser=auth.getCurrentUser();
        DocumentReference docref=db.collection("Users").document(curUser.getUid()).collection("Progress").document("ProgressPractice");
        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    documentSnapshot.getData();
                    firestoreCallback.onClickback(documentSnapshot);
                }else{
                    Log.d("Else=","Doc not exist");
                }
            }
        });

    }
    private interface FirestoreCallback{
        void onClickback(DocumentSnapshot documentSnapshot);
    }

    private void select(String value) {
        switch (value) {
            case "Days":
                ref="2";
                d = FirebaseDatabase.getInstance().getReference().child("Days of Week");
                Log.d("MainFragment", "idmon=" + d);
                loadJson();
                break;
            case "Numbers":
                ref="1";
                d= FirebaseDatabase.getInstance().getReference().child("Numbers");
                Log.d("MainFragment","idmon="+d);
                loadJson();
                break;
            case "Colors":
                ref="6";
                d= FirebaseDatabase.getInstance().getReference().child("Colors");
                Log.d("MainFragment","idmon="+d);
                loadJson();
                break;
            case "Saison":
                ref="4";
                d= FirebaseDatabase.getInstance().getReference().child("Saison");
                Log.d("MainFragment","idmon="+d);
                loadJson();
                break;
            case "Weather":
                ref="3";
                d= FirebaseDatabase.getInstance().getReference().child("Weather");
                Log.d("MainFragment","idmon="+d);
                loadJson();
                break;
            case "Months":
                ref="5";
                d= FirebaseDatabase.getInstance().getReference().child("Months");
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