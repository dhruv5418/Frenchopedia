package com.example.frenchopedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class ResultActivity extends AppCompatActivity {

    String correct,wrong;
    TextView txt_cor,txt_wro,txt_tot;
    int t;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        final Intent intent = getIntent();
        correct = intent.getStringExtra("correct");
        wrong = intent.getStringExtra("wrong");
        txt_cor=findViewById(R.id.txt_correct);
        txt_wro=findViewById(R.id.txt_wrong);
        txt_tot=findViewById(R.id.txt_total);
        toolbar=findViewById(R.id.toolbar_r);
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
    }
}