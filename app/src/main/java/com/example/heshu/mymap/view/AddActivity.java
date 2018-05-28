package com.example.heshu.mymap.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.example.heshu.mymap.R;
import com.example.heshu.mymap.bean.ImageBean;
import com.example.heshu.mymap.bean.LocationBean;
import com.example.heshu.mymap.customView.NineGridlayout;
import com.example.heshu.mymap.presenter.ReleasePresenter;
import com.example.heshu.mymap.util.MediaPlayerUtil;
import com.example.heshu.mymap.util.UriToPathUtil;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by heshu on 2018/2/12.
 */

public class AddActivity extends BaseActivity implements View.OnClickListener, IAddActivity {
    //共有
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private Button mReleaseButton;
    private ImageView mBackImage;
    private TextView mSiteText;
    private ProgressBar mProgressBar;

    private ReleasePresenter mPresenter;
    private LocationBean mLocationBean;

    private static final String TAG = "AddActivity";
    private static final int TYPE_COMMENT = 0;
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_VOICE = 2;
    private static final int TYPE_VIDEO = 3;

    private int fileType;
    private boolean mReleaseFlag = true;
    //评论
    private EditText mEditText;
    private TextView mTvLeftNum;
    private int MAX_NUM = 140;

    //照片
    public NineGridlayout mMore;
    private ArrayList<String> imageUris;
    private List<String> stringUrl;
    private static final int REQUEST_IMAGE= 20;// 从相册中选择
    List<ImageBean> mImageList = new ArrayList<>();

    //录音
    private String mFilePath;
    private String date;
    private MediaPlayer mMediaPlayer;
    private Button mPlayVoiceButton;
    private AnimationDrawable mFrameAnim;
    //视频
    private VideoView mVideoView;
    private ImageView mImageView;
    private Button mPlayVideoButton;
    private Uri VideoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());

        Intent intent = getIntent();
        fileType = Integer.parseInt(intent.getStringExtra("fileType"));
        Log.d(TAG, "onCreate: "+fileType);
        if (fileType == TYPE_COMMENT) {
            setContentView(R.layout.activity_add_comment);
        } else if (fileType == TYPE_IMAGE) {
            setContentView(R.layout.activity_add_image);
            imageUris =intent.getStringArrayListExtra("image");

        } else if (fileType == TYPE_VOICE) {
            setContentView(R.layout.activity_add_voice);
            mFilePath = intent.getStringExtra("filePath");
            date = intent.getStringExtra("date");
            MediaPlayerUtil.initPaly(mFilePath);
        } else if (fileType == TYPE_VIDEO) {
            setContentView(R.layout.activity_add_video);
            VideoUri = Uri.parse(intent.getStringExtra("video"));
        }

        mLocationBean = (LocationBean) getIntent().getSerializableExtra("Point");
        initView();
        showLocation(mLocationBean);

        mPresenter = new ReleasePresenter(this);
    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18));


        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);

        mReleaseButton = (Button) findViewById(R.id.release_button);
        mReleaseButton.setOnClickListener(this);

        mBackImage = (ImageView) findViewById(R.id.back_image);
        mBackImage.setOnClickListener(this);
        mSiteText = (TextView) findViewById(R.id.site_text);
        mSiteText.setText(mLocationBean.getPointName());

        if (fileType == TYPE_COMMENT) {
            mTvLeftNum = (TextView) findViewById(R.id.tv_left_num);
            mEditText = (EditText) findViewById(R.id.edit_text);
            mEditText.addTextChangedListener(watcher);
        } else if (fileType == TYPE_IMAGE) {
            mTvLeftNum = (TextView) findViewById(R.id.tv_left_num);
            mEditText = (EditText) findViewById(R.id.edit_text);
            mEditText.addTextChangedListener(watcher);
            MAX_NUM = 10;
            mMore = (NineGridlayout) findViewById(R.id.iv_ngrid_layout);
            stringUrl = new ArrayList<>();
            for(String uri :imageUris){
                addImageUrlList(uri);
            }

        } else if (fileType == TYPE_VOICE) {
            mPlayVoiceButton = (Button) findViewById(R.id.play_voice_button);
            mPlayVoiceButton.setText(date);
            mPlayVoiceButton.setOnClickListener(this);

            mFrameAnim = (AnimationDrawable) getResources().getDrawable(R.drawable.sound);
            mFrameAnim.setBounds(0, 0, mFrameAnim.getMinimumWidth(), mFrameAnim.getMinimumHeight());
            mPlayVoiceButton.setCompoundDrawables(mFrameAnim, null, null, null);


        } else if (fileType == TYPE_VIDEO) {
            mTvLeftNum = (TextView) findViewById(R.id.tv_left_num);
            mEditText = (EditText) findViewById(R.id.edit_text);
            mEditText.addTextChangedListener(watcher);
            MAX_NUM = 10;
            mPlayVideoButton = (Button) findViewById(R.id.play_video_button);
            mPlayVideoButton.setOnClickListener(this);
            mImageView = (ImageView) findViewById(R.id.image);
            mVideoView = (VideoView) findViewById(R.id.video);

            mVideoView.setMediaController(new MediaController(this));
            mVideoView.setOnCompletionListener(new MyPlayerOnCompletionListener());
            mVideoView.setVideoURI(VideoUri);

            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(UriToPathUtil.getImageAbsolutePath(this, VideoUri));
            Bitmap bitmap = media.getFrameAtTime();
            mImageView.setImageBitmap(bitmap);
        }
    }

    TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //只要编辑框内容有变化就会调用该方法，s为编辑框变化后的内容
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //编辑框内容变化之前会调用该方法，s为编辑框内容变化之前的内容
        }

        @Override
        public void afterTextChanged(Editable s) {
            //编辑框内容变化之后会调用该方法，s为编辑框内容变化后的内容
            Log.i("afterTextChanged", s.toString());
            if (s.length() > MAX_NUM) {
                s.delete(MAX_NUM, s.length());
            }
            int num = s.length();
            mTvLeftNum.setText(String.valueOf(num) + "/" + MAX_NUM);
        }
    };


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
            case R.id.release_button:
                if(mReleaseFlag == true) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mReleaseFlag =false;
                    if (fileType == TYPE_COMMENT) {
                        if (!mEditText.getText().toString().equals("")) {
                            mPresenter.requestPushComment(mEditText.getText().toString(), mLocationBean.getId()); //将数据发送
                        }
                    } else if (fileType == TYPE_IMAGE) {
                        mPresenter.requestPushImage(stringUrl, mLocationBean.getId());
                    } else if (fileType == TYPE_VOICE) {

                            mPresenter.requestPushVoice(mFilePath, mLocationBean.getId(),"");


                    } else if (fileType == TYPE_VIDEO) {
                        mPlayVideoButton.setVisibility(View.INVISIBLE);
                        if (!mEditText.getText().toString().equals("")) {
                            mPresenter.requestPushVideo(VideoUri, mLocationBean.getId(),mEditText.getText().toString());
                        }

                    }

                }
                break;
            case R.id.back_image:
                finish();
                break;
            case R.id.play_voice_button:
                MediaPlayerUtil.mediaPlayerStart();
                MediaPlayerUtil.midiaPlayerSetListenrt((new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Log.d("tag", "播放完毕");
                        mFrameAnim.stop();
                        mFrameAnim.selectDrawable(0);
                    }
                }));
                mFrameAnim.start();
                break;
            case R.id.play_video_button:
                mPlayVideoButton.setVisibility(View.INVISIBLE);
                mImageView.setVisibility(View.INVISIBLE);
                mVideoView.setVisibility(View.VISIBLE);
                mVideoView.start();
                break;

            default:
                break;
        }
    }


    //显示定位
    private void showLocation(LocationBean locationBean) {
        mBaiduMap.setMyLocationEnabled(true);

        MyLocationData locData = new MyLocationData.Builder().accuracy(0)
                .direction(0).latitude(locationBean.getLatitude())
                .longitude(locationBean.getLongitude()).build();

        mBaiduMap.setMyLocationData(locData);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null);
        mBaiduMap.setMyLocationConfiguration(config);
    }

    //回调添加照片
    public void addImage() {
        MultiImageSelector.create(this)
                .origin(imageUris) // 默认已选择图片. 只有在选择模式为多选时有效
                .start(this, REQUEST_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if(resultCode == RESULT_OK){
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);

                for(String uri :path){
                    addImageUrlList(uri);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addImageUrlList(String Url) {

        stringUrl.add(Url);
        if (stringUrl.size() > 1 && stringUrl.size() < 10) {
            mImageList.remove(mImageList.size() - 1);
        }
        ImageBean imageBean = new ImageBean(Url, 1, 380, 380);
        mImageList.add(imageBean);

        if (stringUrl.size() < 10) {
            ImageBean imageAdd = new ImageBean(R.drawable.ic_add_image, 2, 380, 380);
            mImageList.add(imageAdd);
        }
        mMore.setImagesData(mImageList);
    }

    //视频
    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {

        }
    }

    //上传成功
    @Override
    public void fileOK() {
        Log.d(TAG, "fileOK: ");
        Toast.makeText(App.getContext(),"上传成功",Toast.LENGTH_SHORT).show();
        mReleaseFlag =true;
        Intent intent = new Intent(AddActivity.this, MapActivity.class);
        startActivity(intent);
    }


    //上传失败
    @Override
    public void fileNO() {
        Toast.makeText(this,"上传失败",Toast.LENGTH_SHORT);
        mReleaseFlag =true;
        Intent intent = new Intent(AddActivity.this, MapActivity.class);
        startActivity(intent);
    }
}
