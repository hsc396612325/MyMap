package com.example.heshu.mymap.view;

import com.baidu.mapapi.model.LatLng;
import com.example.heshu.mymap.bean.LocationBean;

/**
 * Created by heshu on 2018/1/29.
 */

public interface IMapView {
    public void showLocation(LocationBean locationBean);

    public void dissmissLocation();

    public void showOneSpot(LocationBean locationBean);

    public void dissmissOneSpot(LocationBean locationBean);

    public void showAllSpot();

    public void dissMissAllSpot();

}
