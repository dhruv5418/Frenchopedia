package com.example.frenchopedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frenchopedia.Adapter.PracticeAdapter;
import com.example.frenchopedia.Model.Practice;
import com.example.frenchopedia.Model.Practice_;
import com.example.frenchopedia.Retrofit.GetDataService;
import com.example.frenchopedia.Retrofit.RetrofitClientInstance;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    PracticeAdapter practiceAdapter;
    ArrayList<Practice_> parrayList;
    Toolbar toolbar;
    TextView txt_result;
    private FirebaseAuth auth;
    private FirebaseUser curUser;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        toolbar= findViewById(R.id.toolbar_quize);
        txt_result=findViewById(R.id.txt_totalResult);
        auth= FirebaseAuth.getInstance();
        curUser=auth.getCurrentUser();
        db= FirebaseFirestore.getInstance();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Back",Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        });
        getJson();
        getResult();
    }

    private void getResult() {
        readData(new FirestoreCallback() {
            @Override
            public void onClickback(DocumentSnapshot documentSnapshot) {
                double p=Double.valueOf(documentSnapshot.get("total").toString());
                int t=(int)(p);
                txt_result.setText(t+"/300");
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

    private void getJson() {
        GetDataService service= RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Practice> call =service.getAllPractice();
        call.enqueue(new Callback<Practice>() {
            @Override
            public void onResponse(Call<Practice> call, Response<Practice> response) {
                System.out.println("Response From URL :"+response.body());
                try{
                    Practice Practice=response.body();
                    parrayList=new ArrayList<>(Practice.getPractice());
                    generateView(parrayList);
                }catch (NullPointerException n){
                    System.out.println("Nullpointer Exception :"+n.getMessage());

                }

            }

            @Override
            public void onFailure(Call<Practice> call, Throwable t) {
                System.out.println("In Failure :"+t.getMessage());
            }
        });
    }

    private void generateView(ArrayList<Practice_> parrayList) {
        practiceAdapter = new PracticeAdapter(parrayList, getApplicationContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView=findViewById(R.id.recyclerView_Quiz);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(practiceAdapter);
        practiceAdapter.setOnClickListner(onClickListener);
    }
    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            String title = parrayList.get(position).getName();
            Toast.makeText(getApplicationContext(), "title=" + title, Toast.LENGTH_LONG).show();
            Intent intent;
            switch (title) {
                case "Seasons":
                    intent = new Intent(QuizActivity.this, QuizsupportActivity.class);
                    intent.putExtra("Title", "Saison");
                    startActivity(intent);
                    break;
                case "Numbers":
                    intent = new Intent(QuizActivity.this, QuizsupportActivity.class);
                    intent.putExtra("Title", "Numbers");
                    startActivity(intent);
                    break;
                case "Months":
                    intent = new Intent(QuizActivity.this, QuizsupportActivity.class);
                    intent.putExtra("Title", "Months");
                    startActivity(intent);
                    break;
            }
        }
    };



}