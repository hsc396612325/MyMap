package com.example.heshu.mymap.presenter;

import com.example.heshu.mymap.bean.LocationBean;
import com.example.heshu.mymap.model.IMapModel;
import com.example.heshu.mymap.model.MapModel;
import com.example.heshu.mymap.view.IMapView;

import java.util.List;

/**
 * Created by heshu on 2018/1/29.
 */

public class MapPresenter {
    IMapModel mModel;
    IMapView mView;


    private static final String TAG = "MapPresenter";

    public MapPresenter(IMapView mView) {
        this.mView = mView;
        mModel = new MapModel(this);
    }

    //供View层调用，来显示定位 + 周围标记点
    public void requestMapInfo() {
        mModel.pointInit();
    }
    //显示定位 p-->v
    public void showLocation(LocationBean locationBean) {
        if (mView != null && locationBean != null) {
            mView.showLocation(locationBean);
            mModel.pointListInit(locationBean);
        }
    }
    //添加点
    public void addPoint(LocationBean locationBean){
        mModel.addPoint(locationBean);
    }


    //显示所有标记
    public void showAllSpot(List<LocationBean> locationBeanList) {
        if (mView != null && locationBeanList != null) {
            mView.showAllSpot(locationBeanList);
        }
    }


    //取消定位
    private void dissmissLocation() {
        if (mView != null) {
            mView.dissmissLocation();
        }
    }

}
