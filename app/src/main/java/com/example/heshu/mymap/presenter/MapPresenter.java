package com.example.heshu.mymap.presenter;

import android.app.Activity;
import android.os.SystemClock;
import android.util.Log;

import com.example.heshu.mymap.bean.LocationBean;
import com.example.heshu.mymap.model.IMapImpl;
import com.example.heshu.mymap.model.IMapModel;
import com.example.heshu.mymap.view.App;
import com.example.heshu.mymap.view.IMapView;

/**
 * Created by heshu on 2018/1/29.
 */

public class MapPresenter  {
    IMapModel mModel;
    IMapView mView;


    public MapPresenter(IMapView mView){
        this.mView = mView;
        mModel = new IMapImpl();
    }
    //供View层调用，来显示定位
    public void requestMapInfo(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                do{
                    localLocation();
                }while (localLocation()==null);
                showLocation(localLocation());
                LocationBean locationBean = new LocationBean();
                locationBean.setLatitude(localLocation().getLatitude()+0.1);
                locationBean.setLongitude(localLocation().getLongitude()+0.01);
                showOneSpot(locationBean);
            }
        }).start();

    }

    //显示定位 p-->v
    private void showLocation(LocationBean locationBean){
        if(mView != null&&locationBean!=null){
            mView.showLocation( locationBean);
        }
    }

    //取消定位
    private void dissmissLocation(){
        if(mView != null){
            mView.dissmissLocation();
        }
    }

    private void showOneSpot(LocationBean locationBean){
        if(mView!= null && locationBean!=null){
            mView.showOneSpot(locationBean);
        }
    }
    //保存位置
    private void saveLocation(LocationBean locationBean){
        mModel.setLocation(locationBean);
    }

    //获取位置
    private LocationBean localLocation(){
        return mModel.getLocation();
    }


}
