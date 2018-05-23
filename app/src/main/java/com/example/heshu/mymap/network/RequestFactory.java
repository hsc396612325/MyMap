package com.example.heshu.mymap.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by heshu on 2018/3/6.
 */

public class RequestFactory {
    private static  String MainUrl = "http://47.95.207.40/markMap/";

    public static Retrofit getRetrofit(){
        Retrofit.Builder builder = new Retrofit.Builder();
        Gson gson = new GsonBuilder().setLenient().create();


        return builder
                .baseUrl(MainUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
