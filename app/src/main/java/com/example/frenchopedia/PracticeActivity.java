package com.example.frenchopedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.frenchopedia.Adapter.PracticeAdapter;
import com.example.frenchopedia.Model.Practice;
import com.example.frenchopedia.Model.Practice_;
import com.example.frenchopedia.Retrofit.GetDataService;
import com.example.frenchopedia.Retrofit.RetrofitClientInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PracticeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    PracticeAdapter practiceAdapter;
    ArrayList<Practice_> parrayList;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        toolbar= findViewById(R.id.toolbar_practice);
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
        recyclerView=findViewById(R.id.recyclerView_Practice);
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
                case "Days":
                    intent = new Intent(PracticeActivity.this, PracticesupportActivity.class);
                    intent.putExtra("Title", "Days");
                    startActivity(intent);
                    break;
                case "Numbers":intent=new Intent(PracticeActivity.this,PracticesupportActivity.class);
                    intent.putExtra("Title","Numbers");
                    startActivity(intent);
                    break;
                case "Seasons":intent=new Intent(PracticeActivity.this,PracticesupportActivity.class);
                    intent.putExtra("Title","Saison");
                    startActivity(intent);
                    break;
                case "Colors":intent=new Intent(PracticeActivity.this,PracticesupportActivity.class);
                    intent.putExtra("Title","Colors");
                    startActivity(intent);
                    break;
            }
        }
    };
}