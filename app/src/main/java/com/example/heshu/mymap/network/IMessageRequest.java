package com.example.heshu.mymap.network;

import com.example.heshu.mymap.gson.RetrofitReturn;
import com.example.heshu.mymap.gson.RetrofitReturnGetMessage;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by heshu on 2018/2/25.
 */

public interface IMessageRequest {

    //上传评论
    @POST
    @FormUrlEncoded
    Call<RetrofitReturn> addComment(@Url String url, @Header("token") String token, @Field("pointId") int pointId, @Field("content")String content);

    //上传文件
    @POST
    @Multipart
    Call<RetrofitReturn>addFile(@Url String url, @Header("token") String token, @Part("pointId") int id, @Part("type")int type, @Part MultipartBody.Part file);


    //信息的获取
    @POST
    @FormUrlEncoded
    Call<RetrofitReturnGetMessage> getMessage(@Url String url,@Field("pointId") int id, @Field("type")int type);

    //多文件上传
    @POST
    @Multipart
    Call<RetrofitReturn>uploadMangPhotos(@Url String url,@Header("Content-Type") String Content, @Header("token") String token, @Part("pointId") int id, @Part("photos") List<RequestBody> photos);

}
