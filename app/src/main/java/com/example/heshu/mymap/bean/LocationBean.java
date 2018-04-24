package com.example.heshu.mymap.bean;

import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;

/**
 * Created by heshu on 2018/1/29.
 */

public class LocationBean  implements Serializable {
    private int id;
    private double longitude;
    private double latitude;
    private LatLng point ;  //百度的点包含经纬度
    private String pointName;

    private int commentNum;
    private int imageNum;
    private int videoNum;
    private int voiceNum;

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getImageNum() {
        return imageNum;
    }

    public void setImageNum(int imageNum) {
        this.imageNum = imageNum;
    }

    public int getVideoNum() {
        return videoNum;
    }

    public void setVideoNum(int videoNum) {
        this.videoNum = videoNum;
    }

    public int getVoiceNum() {
        return voiceNum;
    }

    public void setVoiceNum(int voiceNum) {
        this.voiceNum = voiceNum;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public  LatLng getPoint(){return new LatLng(latitude,longitude);}

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public String getPointName(){
        return pointName;
    }
}
