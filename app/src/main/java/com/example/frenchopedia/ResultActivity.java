package com.example.frenchopedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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


public class ResultActivity extends AppCompatActivity {

    String correct,wrong,title,ref;
    TextView txt_cor,txt_wro,txt_tot;
    int t;
    Toolbar toolbar;
    private FirebaseAuth auth;
    private FirebaseUser curUser;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        final Intent intent = getIntent();
        correct = intent.getStringExtra("correct");
        wrong = intent.getStringExtra("wrong");
        title = intent.getStringExtra("title");
        txt_cor=findViewById(R.id.txt_correct);
        txt_wro=findViewById(R.id.txt_wrong);
        txt_tot=findViewById(R.id.txt_total);
        toolbar=findViewById(R.id.toolbar_r);
        auth= FirebaseAuth.getInstance();
        curUser=auth.getCurrentUser();
        db= FirebaseFirestore.getInstance();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1= new Intent(ResultActivity.this,Dashboard.class);
                startActivity(intent1);
            }
        });
        t=10*(Integer.parseInt(correct));
        showData();
    }

    private void showData() {
        txt_wro.setText(String.valueOf(wrong));
        txt_cor.setText(String.valueOf(correct));
        txt_tot.setText(String.valueOf(t));
        saveResult();
        updateResult();
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
                    db.collection("Users").document(curUser.getUid()).collection("Progress").document("ProgressQuiz").update(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"Progress updated for quiz"+ref,Toast.LENGTH_LONG).show();
                        }
                    });

                }else{
                    Toast.makeText(getApplicationContext(),"Progress Not updated for quiz"+ref,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveResult() {
        switch (title){
            case "Saison":
                    ref="4";
                break;
            case "Numbers":
                ref="1";
                break;
            case "Months":
                ref="5";
                break;
        }
    }


    void updateResult(){
        readData(new FirestoreCallback() {
            @Override
            public void onClickback(DocumentSnapshot documentSnapshot) {
                double p=Double.valueOf(documentSnapshot.get("total").toString());
                int a=(int)(long) documentSnapshot.get(ref);
                Toast.makeText(getApplicationContext(),"Total"+p,Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"Ref="+a,Toast.LENGTH_LONG).show();
                if (a==0){
                    Log.d("supportPractice","Vaue="+documentSnapshot.get(ref).toString());
                    Map<String,Object> usermap=new HashMap<>();
                    usermap.put(ref,t);
                    p=p+t;
                    usermap.put("total",p);
                    db.collection("Users").document(curUser.getUid()).collection("Result").document("result").update(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"result updated for"+ref,Toast.LENGTH_LONG).show();
                        }
                    });

                }else{
                    Toast.makeText(getApplicationContext(),"result Not updated for"+ref,Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void readData1(final FirestoreCallback firestoreCallback){
        curUser=auth.getCurrentUser();
        DocumentReference docref=db.collection("Users").document(curUser.getUid()).collection("Progress").document("ProgressQuiz");
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


    private void readData(final FirestoreCallback firestoreCallback){
        curUser=auth.getCurrentUser();
        DocumentReference docref=db.collection("Users").document(curUser.getUid()).collection("Result").document("result");
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
}