package com.example.heshu.mymap.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.heshu.mymap.gson.RetrofitReturn;
import com.example.heshu.mymap.network.IUserRequest;
import com.example.heshu.mymap.network.RequestFactory;
import com.example.heshu.mymap.view.App;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heshu on 2018/3/12.
 */

public class RegisterTokenUtil {
    private static final String TAG = "RegisterTokenUtil";
    public static void Register(){
        simulateLogin();
    }
    private static void simulateLogin() {

        IUserRequest request = RequestFactory.getRetrofit().create(IUserRequest.class);
        Call<RetrofitReturn> call = request.login("111222", "123456");
        //发送网络请求(异步)
        call.enqueue(new Callback<RetrofitReturn>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<RetrofitReturn> call, Response<RetrofitReturn> response) {
                RetrofitReturn retrofitReturn = response.body();
                SharedPreferences.Editor  editor = App.getContext().getSharedPreferences("token", Context.MODE_PRIVATE).edit();
                editor.putString("token",retrofitReturn.data);
                editor.apply();
                Log.d(TAG, "onResponse: "+retrofitReturn.data);
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<RetrofitReturn> call, Throwable throwable) {

                Log.d("pushComment", "连接失败 " + throwable);
            }
        });
    }
}
