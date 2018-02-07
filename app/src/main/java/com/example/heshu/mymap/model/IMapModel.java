package com.example.heshu.mymap.model;

import com.example.heshu.mymap.bean.LocationBean;

/**
 * Created by heshu on 2018/1/29.
 */

public interface IMapModel {
    //提供此时的定位点
    public LocationBean getLocation();
    //存储此时的定位点
    public void setLocation(LocationBean location);
}
