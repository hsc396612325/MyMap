package com.example.heshu.mymap.gson;


import com.google.gson.annotations.SerializedName;

/**
 * Created by heshu on 2018/2/26.
 */

public class RetrofitReturn {

    @SerializedName("status")
    public int status;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public String data;

}
