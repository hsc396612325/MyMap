package com.example.heshu.mymap.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Created by heshu on 2018/3/9.
 */

public class RetrofitReturnGetMessage {
    @SerializedName("status")
    public int status;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public List<Data> dataList;

    public class Data {
        @SerializedName("id")
        public int dataId;

        @SerializedName("pointId")
        public int pointId;

        @SerializedName("type")
        public int type;

        @SerializedName("username")
        public String userName;

        @SerializedName("content")
        public String content;

        @SerializedName("remarkCount")
        public int comtNum;

        @SerializedName("clickCount")
        public int likeNum;

        @SerializedName("createAt")
        public String createAt;

        @SerializedName("isClick")
        public boolean likeFlag;
    }
}
