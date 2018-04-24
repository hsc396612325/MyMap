package com.example.heshu.mymap.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.example.heshu.mymap.gson.RetrofitReturn;
import com.example.heshu.mymap.network.IMessageRequest;
import com.example.heshu.mymap.network.RequestFactory;
import com.example.heshu.mymap.presenter.ReleasePresenter;
import com.example.heshu.mymap.util.UriToPathUtil;
import com.example.heshu.mymap.view.App;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by heshu on 2018/2/25.
 */

public class ReleaseModel implements IReleaseModel {

    private static final String TAG = "ReleaseModel";
    private String mToken;
    private ReleasePresenter mReleasePresenter;
    public ReleaseModel(ReleasePresenter presenter) {
        mReleasePresenter = presenter;
    }

    @Override
    public void pushComment(int pointId, String eText) {
        final IMessageRequest request = RequestFactory.getRetrofit().create(IMessageRequest.class);
        SharedPreferences preferences = App.getContext().getSharedPreferences("token", Context.MODE_PRIVATE);
        mToken = preferences.getString("token","");
        Log.d(TAG, "ReleaseModel: "+mToken);
        Call<RetrofitReturn> call = request.addComment("addMessage/" + pointId, mToken, pointId, eText);
        //发送网络请求(异步)
        call.enqueue(new Callback<RetrofitReturn>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<RetrofitReturn> call, Response<RetrofitReturn> response) {
                RetrofitReturn retrofitReturn = response.body();
                Log.d(TAG, "onResponse: "+response);
                Log.d(TAG, "onResponse: "+retrofitReturn.status);
                Log.d(TAG, "onResponse: "+retrofitReturn.message);
                mReleasePresenter.fileOK();
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<RetrofitReturn> call, Throwable throwable) {
                System.out.println("连接失败");
                mReleasePresenter.fileNO();
            }
        });
    }

    @Override
    public void pushFile(int pointId, int type, Uri uri) {

        SharedPreferences preferences = App.getContext().getSharedPreferences("token", Context.MODE_PRIVATE);
        mToken = preferences.getString("token","");
        File file = new File(UriToPathUtil.getImageAbsolutePath(App.getContext(),uri));
        final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        final IMessageRequest request = RequestFactory.getRetrofit().create(IMessageRequest.class);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Call<RetrofitReturn> call = request.addFile("upload/" + pointId, mToken,pointId,type,body);

        //发送网络请求(异步)
        call.enqueue(new Callback<RetrofitReturn>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<RetrofitReturn> call, Response<RetrofitReturn> response) {
                RetrofitReturn retrofitReturn = response.body();
                Log.d("pushComment", "" + response);
                Log.d("pushComment", "" + retrofitReturn);
                Log.d("pushComment",""+retrofitReturn.message);
                mReleasePresenter.fileOK();
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<RetrofitReturn> call, Throwable throwable) {
                System.out.println("连接失败");
                mReleasePresenter.fileNO();
            }
        });
    }
    @Override
    public void pushFile(int pointId, int type,String uri) {
        SharedPreferences preferences = App.getContext().getSharedPreferences("token", Context.MODE_PRIVATE);
        mToken = preferences.getString("token","");

        File file = new File(uri);
        final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        final IMessageRequest request = RequestFactory.getRetrofit().create(IMessageRequest.class);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Call<RetrofitReturn> call = request.addFile("upload/" + pointId, mToken,pointId,type,body);

        //发送网络请求(异步)
        call.enqueue(new Callback<RetrofitReturn>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<RetrofitReturn> call, Response<RetrofitReturn> response) {
                RetrofitReturn retrofitReturn = response.body();
                Log.d("pushComment", "" + response);
                Log.d("pushComment", "" + retrofitReturn);
                Log.d("pushComment",""+retrofitReturn.message);
                mReleasePresenter.fileOK();
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<RetrofitReturn> call, Throwable throwable) {
                System.out.println("连接失败");
                mReleasePresenter.fileNO();
            }
        });
    }

    @Override
    public void pushImageFile(int pointId, List<String> urlList) {
        SharedPreferences preferences = App.getContext().getSharedPreferences("token", Context.MODE_PRIVATE);
        mToken = preferences.getString("token","");

        List<File> fileList = new ArrayList<>();
        File[] files = new File[10];

        int i=0;
        for(String uri : urlList){
            File file = new File(UriToPathUtil.getImageAbsolutePath(App.getContext(),Uri.parse((String) uri)));
            fileList.add(file);
            files[i]=file;
            i++;
        }

        MultipartBody.Part[] parts = new MultipartBody.Part[10];
        RequestBody[]  requestBodys = new  RequestBody[10];
        List<RequestBody> listBody = new ArrayList<>();
        i=0;
        for(File file :fileList) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            listBody.add(requestFile);
            i++;
        }

        final IMessageRequest request = RequestFactory.getRetrofit().create(IMessageRequest.class);
        Call<RetrofitReturn> call = request.uploadMangPhotos("uploadMangPhotos/" + pointId,"multipart/form-data", mToken,pointId,listBody);

        //发送网络请求(异步)
        call.enqueue(new Callback<RetrofitReturn>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<RetrofitReturn> call, Response<RetrofitReturn> response) {
                RetrofitReturn retrofitReturn = response.body();
                Log.d(TAG, "onResponse: Image "+response);
                Log.d(TAG, "onResponse: "+retrofitReturn);
                Log.d(TAG, "onResponse: "+retrofitReturn.status);
                Log.d(TAG, "onResponse: "+retrofitReturn.message);
                mReleasePresenter.fileOK();
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<RetrofitReturn> call, Throwable throwable) {
                System.out.println("连接失败");
                mReleasePresenter.fileNO();
            }
        });
    }

}
