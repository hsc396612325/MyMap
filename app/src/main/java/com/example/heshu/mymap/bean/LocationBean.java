package com.example.heshu.mymap.bean;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by heshu on 2018/1/29.
 */

public class LocationBean {
    private int id;
    private double longitude;
    private double latitude;
    private LatLng point ;

    public int getId() {
        return id;
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
}
