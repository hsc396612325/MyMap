package com.example.heshu.mymap.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.example.heshu.mymap.R;
import com.example.heshu.mymap.adapter.MessageAdapter;
import com.example.heshu.mymap.bean.LocationBean;
import com.example.heshu.mymap.bean.MessageBean;
import com.example.heshu.mymap.presenter.ShowMessagePresenter;

import java.util.List;

/**
 * Created by heshu on 2018/4/24.
 */

public class ShowMessageActivity extends  BaseActivity implements IShowMessageActivity, View.OnClickListener  {
    private static final int TYPE_COMMENT = 0;
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_VOICE = 2;
    private static final int TYPE_VIDEO = 3;
    private int fileType;

    private LayoutInflater mLayoutInflater;
    private RecyclerView mRecyclerView;

    private ShowMessagePresenter mPresenter;
    private LocationBean mLocationBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_message);
        Intent intent = getIntent();
        fileType = Integer.parseInt(intent.getStringExtra("fileType"));
        mLocationBean = (LocationBean) intent.getSerializableExtra("Point");

        mPresenter = new ShowMessagePresenter(this);
        mPresenter.acquireMessage(mLocationBean.getId(),fileType);

        initView();

    }

    private void initView(){
        mLayoutInflater = LayoutInflater.from(this);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_comment);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

    }
    @Override
    public void onClick(View view) {

    }

    @Override
    public void showMessage(List<MessageBean> messageBeanList, final int type) {
        MessageAdapter adapter = new MessageAdapter(messageBeanList, fileType);
        mRecyclerView.setAdapter(adapter);
    }
}
