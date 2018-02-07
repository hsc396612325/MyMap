package com.example.heshu.mymap.model;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.heshu.mymap.bean.LocationBean;
import com.example.heshu.mymap.view.App;
import com.example.heshu.mymap.view.MapActivity;

/**
 * Created by heshu on 2018/1/29.
 */

public class IMapImpl implements IMapModel {
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private LocationBean locationBean;

    public IMapImpl(){
        init();
    }
    private  void init(){
        mLocationClient = new LocationClient(App.getContext());
        mLocationClient.registerLocationListener(myListener);

        //设置参数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd0911");
        option.setScanSpan(10000);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);

        mLocationClient.start();
        Log.d("getLocation()","3333"+locationBean);
    }
    @Override
    public LocationBean getLocation() {
        if(locationBean!=null){
            mLocationClient.stop();
            return locationBean;
        }
        return null;
    }

    @Override
    public void setLocation(LocationBean location) {

    }

    public class MyLocationListener implements BDLocationListener {


        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            locationBean = new LocationBean();
            locationBean.setLatitude(bdLocation.getLatitude());
            locationBean.setLongitude(bdLocation.getLongitude());
            float radius = bdLocation.getRadius(); //获取定位精度
            int errorCode = bdLocation.getLocType(); //获得错误返回码

            Log.d("onReceiveLocation",""+locationBean);
            if(errorCode != 61&&errorCode!=161){
                Log.e("MyLocationListener错误码",""+errorCode);
                locationBean = null;
            }
        }

    }

}
