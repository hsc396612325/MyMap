package com.example.heshu.mymap.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by heshu on 2018/3/4.
 */

public class RetrofitReturnGetPoints {
    @SerializedName("status")
    public int status;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public List<RetrofitReturnData> dataList ;

    public class RetrofitReturnData {
        @SerializedName("id")
        public int id;

        @SerializedName("name")
        public String name;

        @SerializedName("longitude")
        public double longitude;

        @SerializedName("latitude")
        public double latitude;

        @SerializedName("createBy")
        public int createBy;

        @SerializedName("createAt")
        public String createAt;

        @SerializedName("mesCount")
        public int mesCount;

        @SerializedName("phoCount")
        public int phoCount;

        @SerializedName("audCount")
        public int audCount;

        @SerializedName("vidCount")
        public int vidCount;
    }
}
