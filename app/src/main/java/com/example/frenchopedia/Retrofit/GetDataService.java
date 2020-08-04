package com.example.frenchopedia.Retrofit;

import com.example.frenchopedia.Model.Practice;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {

    @GET("4kfs6Dzbt")
    Call<Practice> getAllPractice();

}
