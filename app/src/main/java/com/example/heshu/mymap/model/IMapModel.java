package com.example.heshu.mymap.model;

import com.example.heshu.mymap.bean.LocationBean;

/**
 * 获取经纬度
 * Created by heshu on 2018/1/29.
 */

public interface IMapModel {
    //提供此时的定位点
    void pointInit();

    //获得定位点附近的点
    void pointListInit(LocationBean locationBean);

    //添加点
    void addPoint(LocationBean locationBean);
}
