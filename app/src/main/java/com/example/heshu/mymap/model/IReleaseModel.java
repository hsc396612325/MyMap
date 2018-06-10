package com.example.heshu.mymap.model;

import android.net.Uri;

import java.util.List;

/**
 * 上传数据
 * Created by heshu on 2018/2/25.
 */

public interface IReleaseModel  {

    //将评论上传
    void pushComment(int pointId,String eText);
    //将文件上传
    void pushFile(int pointId,int type,String uri,String title);
    void pushFile(int pointId,int type,Uri uri,String title);
    //上传多个图片文件
    void pushImageFile(int pointId, List<String> urlList,String title);
}
