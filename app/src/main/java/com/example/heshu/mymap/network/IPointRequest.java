package com.example.heshu.mymap.network;

import com.example.heshu.mymap.gson.RetrofitReturnGetPoints;
import com.example.heshu.mymap.gson.RetrofitReturnPoint;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by heshu on 2018/3/1.
 */

public  interface IPointRequest {

    //点的添加
    @POST("point/addPoint")
    @FormUrlEncoded
    Call<RetrofitReturnPoint> addPoint(@Header("token") String token, @Field("name")String name, @Field("longitude")double longitude , @Field("latitude")double latitude);

    //点的获取
    @POST("none/getPoints")
    @FormUrlEncoded
    Call<RetrofitReturnGetPoints> getPoint(@Field("longitude")double longitude , @Field("latitude")double latitude, @Field("range")int range);
}
