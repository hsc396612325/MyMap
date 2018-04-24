package com.example.heshu.mymap.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.heshu.mymap.bean.LocationBean;
import com.example.heshu.mymap.gson.RetrofitReturnGetPoints;
import com.example.heshu.mymap.gson.RetrofitReturnPoint;
import com.example.heshu.mymap.network.IPointRequest;
import com.example.heshu.mymap.network.RequestFactory;
import com.example.heshu.mymap.presenter.MapPresenter;
import com.example.heshu.mymap.view.App;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 获取经纬度
 * Created by heshu on 2018/1/29.
 */

public class MapModel implements IMapModel {


    private LocationClient mLocationClient;
    private static final String TAG = "MapModel";
    private MapPresenter mMapPresenter;

    public MapModel(MapPresenter presenter) {
        mMapPresenter = presenter;
    }

    //获取经纬度
    @Override
    public void pointInit() {
        mLocationClient  = new LocationClient(App.getContext());
        MyLocationListener myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }
    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            LocationBean locationBean;
            locationBean = new LocationBean();
            locationBean.setLatitude(bdLocation.getLatitude());
            locationBean.setLongitude(bdLocation.getLongitude());
            locationBean.setPointName(bdLocation.getCity() + bdLocation.getDistrict() + bdLocation.getStreet());

            Log.d(TAG, "onReceiveLocation: " + bdLocation.getDistrict());

            float radius = bdLocation.getRadius(); //获取定位精度
            int errorCode = bdLocation.getLocType(); //获得错误返回码

            if (errorCode == 61 || errorCode == 161) {
                mMapPresenter.showLocation(locationBean);
                mLocationClient.stop();
            }
        }
    }

    //获取点
    @Override
    public void pointListInit(LocationBean locationBean) {
        final List<LocationBean> mLocationBeanList = new ArrayList<>();;
        int range = 1000;
        IPointRequest request = RequestFactory.getRetrofit().create(IPointRequest.class);
        Call<RetrofitReturnGetPoints> call = request.getPoint(locationBean.getLongitude(), locationBean.getLatitude(), range);
        Log.d("" + locationBean.getLongitude(), "" + locationBean.getLatitude());

        call.enqueue(new Callback<RetrofitReturnGetPoints>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<RetrofitReturnGetPoints> call, Response<RetrofitReturnGetPoints> response) {
                RetrofitReturnGetPoints retrofitReturn = response.body();
                Log.d("RetrofitReturnGetPoints", "" + response);

                if (retrofitReturn.status == 1) {
                } else if (retrofitReturn.status == 0) {
                    for (RetrofitReturnGetPoints.RetrofitReturnData dataReturn : retrofitReturn.dataList) {
                        LocationBean locationBean2 = new LocationBean();
                        locationBean2.setPointName(dataReturn.name);
                        locationBean2.setLongitude(dataReturn.longitude);
                        locationBean2.setLatitude(dataReturn.latitude);
                        locationBean2.setId(dataReturn.id);
                        locationBean2.setCommentNum(dataReturn.mesCount);
                        locationBean2.setImageNum(dataReturn.phoCount);
                        locationBean2.setVideoNum(dataReturn.vidCount);
                        locationBean2.setVoiceNum(dataReturn.audCount);
                        mLocationBeanList.add(locationBean2);
                    }
                    mMapPresenter.showAllSpot(mLocationBeanList);
                }
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<RetrofitReturnGetPoints> call, Throwable throwable) {
                Log.d("RetrofitReturnGetPoints", "连接失败 " + throwable);
            }
        });
    }

    //添加点
    @Override
    public void addPoint(LocationBean locationBean) {

        SharedPreferences preferences = App.getContext().getSharedPreferences("token", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        Log.d(TAG, "Token: " + token);
        IPointRequest request = RequestFactory.getRetrofit().create(IPointRequest.class);

        Call call = request.addPoint(token, locationBean.getPointName(), locationBean.getLongitude(), locationBean.getLatitude());
        //发送网络请求(异步)
        call.enqueue(new Callback<RetrofitReturnPoint>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<RetrofitReturnPoint> call, Response<RetrofitReturnPoint> response) {
                RetrofitReturnPoint retrofitPointReturn = response.body();
                Log.d("pushComment", "" + response);
                Log.d("pushComment", "" + retrofitPointReturn);
                Log.d("pushComment", "" + retrofitPointReturn.message);
                Log.d("pushComment", "" + retrofitPointReturn.data);
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<RetrofitReturnPoint> call, Throwable throwable) {
                Log.d("连接失败", "" + throwable.toString());

            }
        });
    }

}
