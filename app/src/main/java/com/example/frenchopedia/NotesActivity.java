package com.example.frenchopedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NotesActivity extends AppCompatActivity {
    Toolbar toolbar;
    String data;
    EditText edt_data;
    FirebaseUser curUser;
    FirebaseAuth auth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        toolbar= findViewById(R.id.tool_notes);
        edt_data=findViewById(R.id.edt_notes);
        auth=FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.orenge_button));
        getSupportActionBar().setTitle("Notes");
        toolbar.setOnMenuItemClickListener(toolListener);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(intent);
            }
        });
        loadData();

    }
    void loadData(){
        readData(new FirestoreCallback() {
            @Override
            public void onClickback(DocumentSnapshot documentSnapshot) {
                edt_data.setText((CharSequence) documentSnapshot.get("Data"));

            }
        });
    }

    private void readData(final FirestoreCallback firestoreCallback){
        curUser=auth.getCurrentUser();
        DocumentReference docref=db.collection("Users").document(curUser.getUid()).collection("Notes").document("1");
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_settings, menu);
        return true;
    }

    Toolbar.OnMenuItemClickListener toolListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.done: data=edt_data.getText().toString();
                                curUser=auth.getCurrentUser();

                    DocumentReference userRef = db.collection("Users").document(curUser.getUid());
                   DocumentReference note= userRef.collection("Notes").document("1");
                    Map<String,Object> usermap=new HashMap<>();
                    usermap.put("Data",data);
                    db.collection("Users").document(curUser.getUid()).collection("Notes").document("1").set(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Notes Saved",Toast.LENGTH_LONG).show();
                                loadData();
                            }
                        }
                    });

            }

            return true;
        }
    };
}