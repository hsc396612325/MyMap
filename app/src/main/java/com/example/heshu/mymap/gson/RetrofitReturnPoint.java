package com.example.heshu.mymap.gson;

import com.google.gson.annotations.SerializedName;



/**
 * Created by heshu on 2018/3/1.
 */

public class RetrofitReturnPoint {
    @SerializedName("status")
    public int status;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public Object data;
}

