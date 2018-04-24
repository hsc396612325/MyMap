package com.example.heshu.mymap.view;

import com.example.heshu.mymap.bean.MessageBean;

import java.util.List;

/**
 * Created by heshu on 2018/4/24.
 */

public interface  IShowMessageActivity {
    void showMessage(List<MessageBean> messageBeanList, final int type);
}
