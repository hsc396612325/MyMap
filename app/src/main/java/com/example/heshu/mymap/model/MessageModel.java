package com.example.heshu.mymap.model;

import android.util.Log;

import com.example.heshu.mymap.bean.MessageBean;
import com.example.heshu.mymap.gson.RetrofitReturnGetMessage;
import com.example.heshu.mymap.network.IMessageRequest;
import com.example.heshu.mymap.network.RequestFactory;
import com.example.heshu.mymap.presenter.ShowMessagePresenter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heshu on 2018/4/24.
 */

public class MessageModel implements IMessageModel{
    private List<MessageBean> mMessageBeanList;
    private ShowMessagePresenter mPresenter;

    private static final String TAG = "MessageModel";
    public MessageModel(ShowMessagePresenter messagePresenter){
        mPresenter = messagePresenter;
    }


    //获得该点的消息
    @Override
    public void pointMessageInit(int pointId, final int type) {
        mMessageBeanList = null;
        IMessageRequest request = RequestFactory.getRetrofit().create(IMessageRequest.class);
        Call<RetrofitReturnGetMessage> call = request.getMessage("none/getMessage/" + pointId, pointId,type);

        call.enqueue(new Callback<RetrofitReturnGetMessage>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<RetrofitReturnGetMessage> call, Response<RetrofitReturnGetMessage> response) {
                RetrofitReturnGetMessage retrofitReturn = response.body();
                Log.d("RetrofitReturnGetPoints", ""+response);
                Log.d("RetrofitReturnGetPoints", ""+retrofitReturn);
                Log.d("RetrofitReturnGetPoints", ""+retrofitReturn.status);
                if(retrofitReturn.status==1){
                    Log.d("RetrofitReturnGetPoints",retrofitReturn.message);
                }else if(retrofitReturn.status==0) {
                    mMessageBeanList = new ArrayList<>();
                    for(RetrofitReturnGetMessage.Data data: retrofitReturn.dataList){
                        MessageBean messageBean = new MessageBean();
                        messageBean.setName(data.userName);
                        messageBean.setCommentText(data.content);
                        messageBean.setDate(data.createAt);
                        messageBean.setLikeNum(data.likeNum);
                        messageBean.setComtNum(data.comtNum);
                        messageBean.setLikeFlag(data.likeFlag);
                        messageBean.setCommentId(data.dataId);
                        Log.d(TAG, "onResponse: "+data.content);
                        mMessageBeanList.add(messageBean);
                    }
                }

                mPresenter.showMessage(mMessageBeanList,type);
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<RetrofitReturnGetMessage> call, Throwable throwable) {
                Log.d("RetrofitReturnGetPoints", "连接失败 " + throwable);
            }
        });
    }


}
