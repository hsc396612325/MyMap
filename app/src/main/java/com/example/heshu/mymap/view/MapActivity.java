package com.example.heshu.mymap.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.example.heshu.mymap.R;
import com.example.heshu.mymap.bean.LocationBean;
import com.example.heshu.mymap.presenter.MapPresenter;
import com.example.heshu.mymap.util.ImageUtil;

public class MapActivity extends BaseActivity implements IMapView,View.OnClickListener{

    private MapPresenter mPresenter;
    private  MapView mMapView;
    private BaiduMap mBaiduMap;
    private Button mAddButton;
    private Button mRecommendButton;
    private Button mNavigationButton;

    private PopupWindow mPopuWindow1;
    private View mContentView1;
    private static final int BAIDU_READ_PHONE_STATE =100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MapPresenter(this);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);

        initView();
    }
    private void initView(){
        mMapView = (MapView)findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mAddButton =(Button)findViewById(R.id.addButton);
        mRecommendButton = (Button)findViewById(R.id.recommendButton);
        mNavigationButton = (Button)findViewById(R.id.navigationButton);
        mAddButton.setOnClickListener(this);
        mRecommendButton.setOnClickListener(this);
        mNavigationButton.setOnClickListener(this);

        if(ContextCompat.checkSelfPermission(App.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MapActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }else {
            mPresenter.requestMapInfo();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addButton:
                initPopuWindow1(view);
                break;
            case R.id.recommendButton:
                break;
            case R.id.navigationButton:
                break;
            case R.id.commentButton:
                break;
            case R.id.imageButton:
                break;
            case R.id.voiceButton:
                break;
            case R.id.videoButton:
                break;
            default:break;
        }
    }

    //显示定位
    @Override
    public void showLocation(LocationBean locationBean) {
        mBaiduMap.setMyLocationEnabled(true);

        BitmapDescriptor mCurrentMode =   BitmapDescriptorFactory.fromResource(R.drawable.ic_geo);
        Log.d("222",""+locationBean);
        MyLocationData locData = new MyLocationData.Builder().accuracy(0)
                .direction(0).latitude(locationBean.getLatitude())
                .longitude(locationBean.getLongitude()).build();

        mBaiduMap.setMyLocationData(locData);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, mCurrentMode);
        mBaiduMap.setMyLocationConfiguration(config);

    }

    //取消定位
    @Override
    public void dissmissLocation() {
        mBaiduMap.setMyLocationEnabled(false);
    }

    //添加单个点标记
    @Override
    public void showOneSpot(LocationBean locationBean) {
        Bitmap sourBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_info);
        Bitmap textBitmap = ImageUtil.drawTextToCenter(this, sourBitmap, "1", 16, Color.WHITE);
        BitmapDescriptor bitmap =   BitmapDescriptorFactory.fromBitmap(textBitmap);

        OverlayOptions option = new MarkerOptions()
                .position(locationBean.getPoint())
                .icon(bitmap);
        Log.d("showOneSpot","1111"+locationBean.getPoint().latitude);
        mBaiduMap.addOverlay(option);
    }

    //删除单个点标记
    @Override
    public void dissmissOneSpot(LocationBean locationBean) {

    }

    //添加全部点标记
    @Override
    public void showAllSpot() {

    }

    //删除全部点标记
    @Override
    public void dissMissAllSpot() {
       // mBaiduMap.clear();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions,grantResults);
        switch(requestCode) {
            //requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case 1:
                if(grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPresenter.requestMapInfo();
                } else{
                    Toast.makeText(this,"无法定位，请同意定位权限",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    private void initPopuWindow1(View parent) {
        if (mPopuWindow1 == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(this);
            mContentView1 = mLayoutInflater.inflate(R.layout.popupwindow1, null);
            mPopuWindow1 = new PopupWindow(mContentView1, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        Button commentButton = (Button)mContentView1.findViewById(R.id.commentButton);
        Button imageButton = (Button)mContentView1.findViewById(R.id.imageButton);
        Button voiceButton = (Button)mContentView1.findViewById(R.id.voiceButton);
        Button videoButton = (Button)mContentView1.findViewById(R.id.videoButton);

        commentButton.setOnClickListener(this);
        imageButton.setOnClickListener(this);
        voiceButton.setOnClickListener(this);
        videoButton.setOnClickListener(this);

        ColorDrawable cd = new ColorDrawable(0x000000);
        mPopuWindow1.setBackgroundDrawable(cd);
        mPopuWindow1.setOutsideTouchable(true);
        mPopuWindow1.setFocusable(true);
        mPopuWindow1.showAtLocation((View)parent.getParent(), Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);

        mPopuWindow1.update();
    }
}