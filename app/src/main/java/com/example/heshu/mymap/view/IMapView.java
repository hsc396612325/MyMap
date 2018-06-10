package com.example.heshu.mymap.view;

import com.example.heshu.mymap.bean.LocationBean;

import java.util.List;

/**
 * Created by heshu on 2018/1/29.
 */

public interface IMapView {
    void showLocation(LocationBean locationBean);

    void dissmissLocation();

    void dissmissOneSpot(LocationBean locationBean);
    void showAllSpot(List<LocationBean> locationBeanList);

    void dissMissAllSpot();

}
