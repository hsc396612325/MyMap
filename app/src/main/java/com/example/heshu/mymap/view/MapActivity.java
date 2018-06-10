package com.example.heshu.mymap.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.heshu.mymap.R;
import com.example.heshu.mymap.bean.LocationBean;
import com.example.heshu.mymap.customView.Config;
import com.example.heshu.mymap.presenter.MapPresenter;
import com.example.heshu.mymap.util.AudioRecoderUtil;
import com.example.heshu.mymap.util.FileUtil;
import com.example.heshu.mymap.util.ImageUtil;
import com.example.heshu.mymap.util.RegisterTokenUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import q.rorbin.badgeview.QBadgeView;

public class MapActivity extends BaseActivity implements IMapView, View.OnClickListener {
    private static final String TAG = "MapActivity";

    private RelativeLayout mMaoLayout;

    private MapPresenter mPresenter;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Button mAddButton;
    private Button mRecommendButton;
    private Button mNavigationButton;
    private Button mShootButton;
    private Button mStorageButton;
    private LinearLayout mLinearLayout;

    PopupWindow mPopupWindow;

    private static final int REQUEST_IMAGE = 10;// 从相册中选择

    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private static final int RECORD_SYSTEM_VIDEO = 30; //拍摄短视频
    private static final int RECORD_STORAGE_VIDEO = 40; //从图库中打开视频
    private static final int ZOOM = 18;

    private static final int TYPE_COMMENT = 0;
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_VOICE = 2;


    private static final int TYPE_VIDEO = 3;

    private enum status_button {
        ADD_POINT,
        ADD_MESSAGE,
        MESSAGE_CANCEL
    }

    private status_button buttonStatus = status_button.ADD_POINT;

    private File mTempFile;

    private List<LocationBean> mLocationBeanList;
    private LocationBean mTodayPoint;
    private LocationBean mMarkerPoint;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MapPresenter(this);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);

        Log.d(TAG, "onCreate: ");
        initView();
        initPermission();
        RegisterTokenUtil.Register();
        initMarkerClick();
        getDeviceDensity();
    }


    //初始化控件
    private void initView() {
        mMaoLayout = (RelativeLayout) findViewById(R.id.map_layout);

        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(ZOOM));
        mAddButton = (Button) findViewById(R.id.addButton);
        mRecommendButton = (Button) findViewById(R.id.recommendButton);
        mNavigationButton = (Button) findViewById(R.id.navigationButton);
        mShootButton = (Button) findViewById(R.id.shoot_button);
        mStorageButton = (Button) findViewById(R.id.storage_button);

        mAddButton.setOnClickListener(this);
        mRecommendButton.setOnClickListener(this);
        mNavigationButton.setOnClickListener(this);
        mShootButton.setOnClickListener(this);
        mStorageButton.setOnClickListener(this);

        mLinearLayout = (LinearLayout) findViewById(R.id.gone_linear);
    }

    //权限申请
    private void initPermission() {
        AndPermission.with(this)
                .requestCode(100)
                .permission(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                )
                .callback(mListenerr)
                .start();

    }

    private PermissionListener mListenerr = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            mPresenter.requestMapInfo();
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {

        }
    };

    //初始化点的点击
    private void initMarkerClick() {

        LayoutInflater mLayoutInflater = LayoutInflater.from(this);
        final View popuWindowMaeker = mLayoutInflater.inflate(R.layout.popup_window_marker, null);
        RelativeLayout relativeLayout = (RelativeLayout) popuWindowMaeker.findViewById(R.id.popup_window_marker_relative);

        LinearLayout comLinearLayout = (LinearLayout) popuWindowMaeker.findViewById(R.id.comment_layout);
        LinearLayout imageLinearLayout = (LinearLayout) popuWindowMaeker.findViewById(R.id.image_layout);
        LinearLayout voiceLinearLayout = (LinearLayout) popuWindowMaeker.findViewById(R.id.voice_layout);
        LinearLayout videoLinearLayout = (LinearLayout) popuWindowMaeker.findViewById(R.id.video_layout);
        final QBadgeView comBadge = new QBadgeView(this);
        comBadge.bindTarget(comLinearLayout).setGravityOffset(0, -2, true);
        final QBadgeView imageBagde = new QBadgeView(this);
        imageBagde.bindTarget(imageLinearLayout).setGravityOffset(0, -2, true);
        final QBadgeView voiceBagde = new QBadgeView(this);
        voiceBagde.bindTarget(voiceLinearLayout).setGravityOffset(0, -2, true);
        final QBadgeView videoBagde = new QBadgeView(this);
        videoBagde.bindTarget(videoLinearLayout).setGravityOffset(0, -2, true);

        Button commentButton = (Button) popuWindowMaeker.findViewById(R.id.commentButton2);
        Button imageButton = (Button) popuWindowMaeker.findViewById(R.id.imageButton2);
        Button voiceButton = (Button) popuWindowMaeker.findViewById(R.id.voiceButton2);
        Button videoButton = (Button) popuWindowMaeker.findViewById(R.id.videoButton2);

        commentButton.setOnClickListener(this);
        imageButton.setOnClickListener(this);
        voiceButton.setOnClickListener(this);
        videoButton.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                LatLng ll = marker.getPosition();

                for (LocationBean locationBean : mLocationBeanList) {
                    if (locationBean.getLatitude() == ll.latitude && locationBean.getLongitude() == ll.longitude) {
                        mMarkerPoint = locationBean;
                    }
                }
                Log.d(TAG, "onMarkerClick: " + mMarkerPoint.getId());
                Log.d(TAG, "onMarkerClick: " + mMarkerPoint.getCommentNum());
                InfoWindow infoWindow = new InfoWindow(popuWindowMaeker, ll, 40);
                comBadge.setBadgeNumber(mMarkerPoint.getCommentNum());
                imageBagde.setBadgeNumber(mMarkerPoint.getImageNum());
                videoBagde.setBadgeNumber(mMarkerPoint.getVideoNum());
                voiceBagde.setBadgeNumber(mMarkerPoint.getVoiceNum());
                mBaiduMap.showInfoWindow(infoWindow);
                buttonStatus = status_button.ADD_MESSAGE;
                mAddButton.setText("添加信息");
                return false;
            }
        });

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


    //显示弹窗
    private void initPopupWindowAdd(View parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View contentView = layoutInflater.inflate(R.layout.popup_window_add, null);
        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        Button commentButton = (Button) contentView.findViewById(R.id.commentButton);
        Button imageButton = (Button) contentView.findViewById(R.id.imageButton);
        Button voiceButton = (Button) contentView.findViewById(R.id.voiceButton);
        Button videoButton = (Button) contentView.findViewById(R.id.videoButton);
        commentButton.setOnClickListener(this);
        imageButton.setOnClickListener(this);
        voiceButton.setOnClickListener(this);
        videoButton.setOnClickListener(this);

        ColorDrawable cd = new ColorDrawable(0x000000);
        mPopupWindow.setBackgroundDrawable(cd);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.showAtLocation((View) parent.getParent(), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                buttonStatus = status_button.ADD_POINT;
                mAddButton.setText("添加点");
            }
        });
        mPopupWindow.update();


    }

    //显示录音弹窗
    @SuppressLint("ClickableViewAccessibility")
    private void initPopupWindowVoice(View parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View contentView = layoutInflater.inflate(R.layout.popup_window_voice, null);
        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        Button voiceButton = contentView.findViewById(R.id.voice_button);
        final TextView timeText = contentView.findViewById(R.id.time_text);
        final AudioRecoderUtil audioRecoderUtil = new AudioRecoderUtil();
        final TextView stateText = contentView.findViewById(R.id.state_text);
        final String[] filePath1 = new String[1];
        final String[] date = new String[1];
        audioRecoderUtil.setOnAudioStatusUpdateListener(new AudioRecoderUtil.OnAudioStatusUpdateListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onUpdate(double db, long time) {
                java.text.SimpleDateFormat format = new SimpleDateFormat("mm:ss");
                timeText.setText("" + format.format(time));
                date[0] = "" + format.format(time);
            }

            @Override
            public void onStop(String filePath) {
                Toast.makeText(MapActivity.this, "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
                filePath1[0] = filePath;
            }
        });

        voiceButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        audioRecoderUtil.startRecord();
                        stateText.setText("松开发送");
                        break;
                    case MotionEvent.ACTION_UP:
                        audioRecoderUtil.stopRecord();//结束录音
                        if (filePath1[0] != null) {
                            Intent intent = new Intent(MapActivity.this, AddActivity.class);
                            intent.putExtra("filePath", filePath1[0]);
                            intent.putExtra("date", date[0]);
                            intent.putExtra("Point", mMarkerPoint);
                            intent.putExtra("fileType", "" + TYPE_VOICE);
                            startActivity(intent);
                            mPopupWindow.dismiss();
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        ColorDrawable cd = new ColorDrawable(0x000000);
        mPopupWindow.setBackgroundDrawable(cd);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.showAtLocation((View) parent.getParent(), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);

        mPopupWindow.update();
    }


    // 拍摄短视频
    private void customVideo() {
        Uri fileUri = Uri.fromFile(FileUtil.getOutPutMediaFile());
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10); // 设置拍摄时间为10s
        startActivityForResult(intent, RECORD_SYSTEM_VIDEO);

    }

    // 从图库中选择短视频
    private void choiceVideo() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RECORD_STORAGE_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE) {  // 拍照
            if (FileUtil.hasSdcard()) {
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);

                Intent intent = new Intent(MapActivity.this, AddActivity.class);
                intent.putExtra("fileType", "" + TYPE_IMAGE);
                intent.putExtra("Point", mMarkerPoint);
                intent.putStringArrayListExtra("image", (ArrayList<String>) path);
                startActivity(intent);
            } else {
                Toast.makeText(MapActivity.this, "未找到储存卡", Toast.LENGTH_SHORT);
            }
        } else if (requestCode == RECORD_SYSTEM_VIDEO || requestCode == RECORD_STORAGE_VIDEO) { // 拍摄短视频或者从图库中获得
            Log.d("video", "" + data.getData().toString());
            Intent intent = new Intent(MapActivity.this, AddActivity.class);
            intent.putExtra("fileType", "" + TYPE_VIDEO);
            intent.putExtra("Point", mMarkerPoint);
            intent.putExtra("video", data.getData().toString());
            startActivity(intent);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showInputDialog() {
    /*@setView 装入一个EditView
     */
        final EditText editText = new EditText(MapActivity.this);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(MapActivity.this);
        inputDialog.setTitle("输入该点的名字").setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTodayPoint.setPointName(editText.getText().toString());
                        mPresenter.addPoint(mTodayPoint);
                    }
                });
        inputDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addButton:
                if (buttonStatus == status_button.ADD_POINT) {
                    showInputDialog();
                } else if (buttonStatus == status_button.ADD_MESSAGE) {
                    initPopupWindowAdd(view);
                    mAddButton.setText("取消");
                    mBaiduMap.hideInfoWindow();
                    buttonStatus = status_button.MESSAGE_CANCEL;
                } else if (buttonStatus == status_button.MESSAGE_CANCEL) {
                    mPopupWindow.dismiss();
                    mAddButton.setText("添加");
                    buttonStatus = status_button.ADD_MESSAGE;
                }

                break;
            case R.id.recommendButton:
                break;
            case R.id.navigationButton:
                break;
            case R.id.commentButton: {
                Intent intent = new Intent(MapActivity.this, AddActivity.class);
                intent.putExtra("fileType", "" + TYPE_COMMENT);
                intent.putExtra("Point", mMarkerPoint);
                startActivity(intent);
            }
            mPopupWindow.dismiss();
            break;
            case R.id.imageButton:
//                mAddButton.setVisibility(View.GONE);
//                mLinearLayout.setVisibility(View.VISIBLE);
//                mShootButton.setText("拍摄");
//                mStorageButton.setText("图库");
//                flag = true;
//                mPopupWindow.dismiss();
                MultiImageSelector.create(MapActivity.this)
                        .start(MapActivity.this, REQUEST_IMAGE);

                break;
            case R.id.voiceButton:
                mPopupWindow.dismiss();
                initPopupWindowVoice(view);

                break;
            case R.id.videoButton:
                mAddButton.setVisibility(View.GONE);
                mLinearLayout.setVisibility(View.VISIBLE);
                mShootButton.setText("拍摄");
                mStorageButton.setText("图库");
                flag = false;
                mPopupWindow.dismiss();
                break;
            case R.id.shoot_button:
                mAddButton.setVisibility(View.VISIBLE);
                mLinearLayout.setVisibility(View.GONE);

                customVideo();

                break;
            case R.id.storage_button:
                mAddButton.setVisibility(View.VISIBLE);
                mLinearLayout.setVisibility(View.GONE);

                choiceVideo();

                break;
            case R.id.popup_window_marker_relative:
                Log.d(TAG, "onClick: popup_window_marker");
                mBaiduMap.hideInfoWindow();
                buttonStatus = status_button.ADD_POINT;
                mAddButton.setText("添加点");
                break;
            case R.id.commentButton2:
                if (mMarkerPoint.getCommentNum() != 0) {
                    Intent intent = new Intent(MapActivity.this, ShowCommentActivity.class);
                    startActivity(intent);
                }

                mBaiduMap.hideInfoWindow();
                buttonStatus = status_button.ADD_POINT;
                mAddButton.setText("添加点");
                break;

            case R.id.imageButton2:
                if (mMarkerPoint.getImageNum() != 0) {
                    Intent intent = new Intent(MapActivity.this, ShowMessageActivity.class);
                    intent.putExtra("fileType", "" + TYPE_IMAGE);
                    intent.putExtra("Point", mMarkerPoint);
                    startActivity(intent);
                }
                mBaiduMap.hideInfoWindow();
                buttonStatus = status_button.ADD_POINT;
                mAddButton.setText("添加点");
                break;
            case R.id.voiceButton2:
                if (mMarkerPoint.getVoiceNum() != 0) {
                    Intent intent = new Intent(MapActivity.this, ShowMessageActivity.class);
                    intent.putExtra("fileType", "" + TYPE_VOICE);
                    intent.putExtra("Point", mMarkerPoint);
                    startActivity(intent);
                }
                mBaiduMap.hideInfoWindow();
                buttonStatus = status_button.ADD_POINT;
                mAddButton.setText("添加点");
                break;
            case R.id.videoButton2:
                if (mMarkerPoint.getVideoNum() != 0) {
                    Intent intent = new Intent(MapActivity.this, ShowMessageActivity.class);
                    intent.putExtra("fileType", "" + TYPE_VIDEO);
                    intent.putExtra("Point", mMarkerPoint);
                    startActivity(intent);
                }
                mBaiduMap.hideInfoWindow();
                buttonStatus = status_button.ADD_POINT;
                mAddButton.setText("添加点");
                break;
            case R.id.like_icon:
                Log.d(TAG, "onClick: like_icon");
                break;
            case R.id.comt_icon:
                Log.d(TAG, "onClick: comt_icon");
                break;
            default:
                break;
        }
    }


    //显示定位
    @Override
    public void showLocation(LocationBean locationBean) {
        mBaiduMap.setMyLocationEnabled(true);

        mTodayPoint = locationBean;
        //BitmapDescriptor mCurrentMode = BitmapDescriptorFactory.fromResource(R.drawable.ic_geo);//自定义图标
        MyLocationData locData = new MyLocationData.Builder().accuracy(0)
                .direction(0).latitude(locationBean.getLatitude())
                .longitude(locationBean.getLongitude()).build();
        Log.d(TAG, "showLocation: " + locationBean.getLongitude());
        Log.d(TAG, "showLocation: " + locationBean.getLatitude());
        mBaiduMap.setMyLocationData(locData);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null);
        mBaiduMap.setMyLocationConfiguration(config);
    }

    //取消定位
    @Override
    public void dissmissLocation() {
        mBaiduMap.setMyLocationEnabled(false);
    }


    //删除单个点标记
    @Override
    public void dissmissOneSpot(LocationBean locationBean) {

    }

    //添加全部点标记
    @Override
    public void showAllSpot(List<LocationBean> locationBeanList) {
        mLocationBeanList = locationBeanList;
        List<OverlayOptions> options = new ArrayList<>();
        Bitmap sourBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_info);

        for (LocationBean locationBean : locationBeanList) {
            int num = locationBean.getCommentNum() + locationBean.getImageNum() + locationBean.getVoiceNum() + locationBean.getVideoNum();
            Bitmap textBitmap = ImageUtil.drawTextToCenter(this, sourBitmap, "" + num, 16, Color.WHITE);
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(textBitmap);

            OverlayOptions option = new MarkerOptions()
                    .position(locationBean.getPoint())
                    .icon(bitmap);

            options.add(option);
        }
        mBaiduMap.addOverlays(options);

    }

    //删除全部点标记
    @Override
    public void dissMissAllSpot() {
        // mBaiduMap.clear();
    }
    protected void getDeviceDensity() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Config.EXACT_SCREEN_HEIGHT = metrics.heightPixels;
        Config.EXACT_SCREEN_WIDTH = metrics.widthPixels;
    }
}