package com.example.frenchopedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.frenchopedia.Adapter.PracticesupportAdapter;
import com.example.frenchopedia.Model.Question1;
import com.example.frenchopedia.Model.Question2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Quizsupport2Activity extends AppCompatActivity {

    String value;
    DatabaseReference d;
    TextView txt_q;
    Button btn_next;
    RadioGroup selectAns;
    RadioButton a_1,a_2,a_3,a_4,a;
    int total=0;
    int correct=0;
    int wrong=0;
    ImageView imageView;
    String title;
    Toolbar tool_q2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizsupport2);
        txt_q=findViewById(R.id.txt_q1);
        selectAns=findViewById(R.id.selectAns1);
        imageView=findViewById(R.id.img_quiz);
        a_1=findViewById(R.id.a_1);
        a_2=findViewById(R.id.a_2);
        a_3=findViewById(R.id.a_3);
        a_4=findViewById(R.id.a_4);
        tool_q2=findViewById(R.id.toolbar_q2);
        tool_q2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_next=findViewById(R.id.btn_sub1);
        Intent intent = getIntent();
        value = intent.getStringExtra("Title");
        select(value);
    }

    private void select(String value) {
        switch (value) {
            case "Colors":
                Log.d("MainFragment", "idmon=" + d);
                title="Quiz2 Couleurs";
                loadQuiz();
                break;
        }
    }

    private void loadQuiz() {

        total++;
        if(total >5){
            Intent intent=new Intent(Quizsupport2Activity.this,ResultActivity.class);
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
                    final Question2 question2=dataSnapshot.getValue(Question2.class);
                    txt_q.setText(question2.getQuestion());
                    Glide.with(Quizsupport2Activity.this).asBitmap().load(question2.url).into(imageView);
                    a_1.setText(question2.option1);
                    a_2.setText(question2.option2);
                    a_3.setText(question2.option3);
                    a_4.setText(question2.option4);
                    Toast.makeText(getApplicationContext(),"Ans="+question2.getAnswer(),Toast.LENGTH_LONG).show();
                    btn_next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int selectedId = selectAns.getCheckedRadioButtonId();
                            a=findViewById(selectedId);
                            if(a.getText().toString().equals(question2.answer)){
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
                                if(a_1.getText().toString().equals(question2.answer)){
                                    a_1.setTextColor(Color.GREEN);
                                }else if(a_2.getText().toString().equals(question2.answer)){
                                    a_2.setTextColor(Color.GREEN);
                                }else if(a_3.getText().toString().equals(question2.answer)){
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
}