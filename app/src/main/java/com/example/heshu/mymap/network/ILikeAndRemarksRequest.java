package com.example.heshu.mymap.network;

import com.example.heshu.mymap.gson.RetrofitReturn;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by heshu on 2018/3/11.
 */

public interface ILikeAndRemarksRequest {
    //点赞
    @POST("click")
    @FormUrlEncoded
    Call<RetrofitReturn> addLike(@Header("token")String token,@Field("type")int type,@Field("infoOrRemarkId")int infoOrRemarkId) ;

    //取消点赞
    @POST("unclick")
    @FormUrlEncoded
    Call<RetrofitReturn> unLike(@Header("token")String token,@Field("type")int type,@Field("infoOrRemarkId")int infoOrRemarkId);
}
