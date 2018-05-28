package com.example.heshu.mymap.network;

import com.example.heshu.mymap.gson.RetrofitReturn;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by heshu on 2018/2/26.
 */

public interface IUserRequest {

    //注册
    @POST("user//register")
    @FormUrlEncoded
    Call<RetrofitReturn> register(@Field("username") String username, @Field("account") String account, @Field("password") String password);

    //登录
    @POST("user/login")
    @FormUrlEncoded
    Call<RetrofitReturn> login (@Field("account") String account, @Field("password") String password);


}
