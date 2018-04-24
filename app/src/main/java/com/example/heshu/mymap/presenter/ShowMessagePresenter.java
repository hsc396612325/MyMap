package com.example.heshu.mymap.presenter;

import com.example.heshu.mymap.bean.MessageBean;
import com.example.heshu.mymap.model.IMessageModel;
import com.example.heshu.mymap.model.MessageModel;
import com.example.heshu.mymap.view.IShowMessageActivity;

import java.util.List;

/**
 * Created by heshu on 2018/4/24.
 */

public class ShowMessagePresenter {
    private IShowMessageActivity mView;
    private IMessageModel mModel;

    public ShowMessagePresenter(IShowMessageActivity view){
        mView = view;
        mModel = new MessageModel(this);
    }

    public void acquireMessage(int pointId, final int type) {
        mModel.pointMessageInit(pointId, type);

    }

    public void showMessage(List<MessageBean> messageBeanList, final int type){
        mView.showMessage( messageBeanList, type);
    }
}
