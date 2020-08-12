package com.example.frenchopedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frenchopedia.Adapter.PracticesupportAdapter;
import com.example.frenchopedia.Model.Material;
import com.example.frenchopedia.Model.Question1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class QuizsupportActivity extends AppCompatActivity {

    String value;
    DatabaseReference d;
    Toolbar tool_q1;
    TextView txt_q,txt_timer;
    Button btn_next;
    RadioGroup selectAns;
    RadioButton a_1,a_2,a_3,a_4,a;
    int total=0;
    int correct=0;
    int wrong=0;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizsupport);
        txt_q=findViewById(R.id.txt_q1);
        selectAns=findViewById(R.id.selectAns1);
        a_1=findViewById(R.id.a_1);
        a_2=findViewById(R.id.a_2);
        a_3=findViewById(R.id.a_3);
        a_4=findViewById(R.id.a_4);
        btn_next=findViewById(R.id.btn_sub1);
        tool_q1=findViewById(R.id.toolbar_q1);
        txt_timer=findViewById(R.id.txt_timer1);
        tool_q1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        value = intent.getStringExtra("Title");
        select(value);
        reversTimer(30,txt_timer);
    }

    private void select(String value) {
        switch (value) {
            case "Saison":
                Log.d("MainFragment", "idmon=" + d);
                title="Quiz3 Saisons";
                loadQuiz();
                break;
            case "Numbers":
                Log.d("MainFragment", "idmon=" + d);
                title="Quiz1 Numbers";
                loadQuiz();
                break;
            case "Months":
                Log.d("MainFragment", "idmon=" + d);
                title="Quiz4 Months";
                loadQuiz();
                break;
            case "Weather":
                Log.d("MainFragment", "idmon=" + d);
                title="Quiz5 Weather";
                Toast.makeText(getApplicationContext(),"Weather Called",Toast.LENGTH_LONG).show();
                loadQuiz();
                break;
            case "Days":
                Log.d("MainFragment", "idmon=" + d);
                Toast.makeText(getApplicationContext(),"Days Called",Toast.LENGTH_LONG).show();
                title="Quiz6 Days";
                loadQuiz();
                break;

        }
    }

    private void loadQuiz() {
        total++;
        if(total >5){
            Intent intent=new Intent(QuizsupportActivity.this,ResultActivity.class);
            intent.putExtra("correct",String.valueOf(correct));
            intent.putExtra("wrong",String.valueOf(wrong));
            intent.putExtra("title",value);
            startActivity(intent);

        }else{
            Log.d("Quiz","called="+d);
            d = FirebaseDatabase.getInstance().getReference().child(title).child(String.valueOf(total));
            d.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final Question1 question1=dataSnapshot.getValue(Question1.class);
                    txt_q.setText(question1.getQuestion());
                    a_1.setText(question1.option1);
                    a_2.setText(question1.option2);
                    a_3.setText(question1.option3);
                    a_4.setText(question1.option4);
                    Toast.makeText(getApplicationContext(),"Ans="+question1.getAnswer(),Toast.LENGTH_LONG).show();
                    btn_next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int selectedId = selectAns.getCheckedRadioButtonId();
                            a=findViewById(selectedId);
                            if(a.getText().toString().equals(question1.answer)){
                                a.setTextColor(Color.GREEN);
                                Handler handler=new Handler();
                                handler.postDelayed(new Runnable(){
                                    @Override
                                    public void run() {
                                        a.setTextColor(Color.parseColor("#808080"));
                                        correct++;
                                        loadQuiz();
                                    }
                                },1500);
                            }else{
                                a.setTextColor(Color.RED);
                                if(a_1.getText().toString().equals(question1.answer)){
                                    a_1.setTextColor(Color.GREEN);
                                }else if(a_2.getText().toString().equals(question1.answer)){
                                    a_2.setTextColor(Color.GREEN);
                                }else if(a_3.getText().toString().equals(question1.answer)){
                                    a_3.setTextColor(Color.GREEN);
                                }else{
                                    a_4.setTextColor(Color.GREEN);
                                } Handler handler=new Handler();
                                handler.postDelayed(new Runnable(){
                                    @Override
                                    public void run() {
                                        a.setTextColor(Color.parseColor("#808080"));
                                        a_1.setTextColor(Color.parseColor("#808080"));
                                        a_2.setTextColor(Color.parseColor("#808080"));
                                        a_3.setTextColor(Color.parseColor("#808080"));
                                        a_4.setTextColor(Color.parseColor("#808080"));
                                        wrong++;
                                        loadQuiz();
                                    }
                                },1500);


                            }
                        }
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),""+databaseError,Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    public void reversTimer(int seconds, final TextView tv){
        new CountDownTimer(seconds*1000+1000,1000){
            public void onTick(long millisUntillFinished){
                int seconds = (int)(millisUntillFinished/1000);
                int minutes=seconds/60;
                seconds=seconds%60;
                tv.setText((String.format("%02d",minutes))+":"+String.format("%02d",seconds));

            }
            public void onFinish(){
                tv.setText("Completed");
                Intent intent=new Intent(QuizsupportActivity.this,ResultActivity.class);
                intent.putExtra("correct",String.valueOf(correct));
                intent.putExtra("wrong",String.valueOf(wrong));
                intent.putExtra("title",value);
                startActivity(intent);
            }

        }.start();
    }
}