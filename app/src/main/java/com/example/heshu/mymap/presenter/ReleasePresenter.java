package com.example.heshu.mymap.presenter;

import android.net.Uri;

import com.example.heshu.mymap.model.IReleaseModel;
import com.example.heshu.mymap.model.ReleaseModel;
import com.example.heshu.mymap.view.IAddActivity;

import java.util.List;

/**
 * Created by heshu on 2018/2/25.
 */

public class ReleasePresenter {
    IReleaseModel rModel;
    IAddActivity view;
    private static final int COMMENT_TYPE = 1;
    private static final int IMAGE_TYPE = 1;
    private static final int VOICE_TYPE = 2;
    private static final int VIDEO_TYPE = 3;


    public ReleasePresenter(IAddActivity View) {
        this.view = View;
        rModel = new ReleaseModel(this);
    }


    //发送数据
    public void requestPushComment(final String eText, int id) {
        rModel.pushComment(id, eText);
    }

    public void requestPushImage(final Uri uri, final List<String> urlList, int id) {
        rModel.pushImageFile(id, urlList);
    }

    public void requestPushVideo(final Uri uri, int id) {
        rModel.pushFile(id, VIDEO_TYPE, uri);
    }

    public void requestPushVoice(final String uri, int id) {
        rModel.pushFile(id, VOICE_TYPE, uri);
    }

    public void fileOK(){
        view.fileOK();
    }

    public void fileNO(){
        view.fileNO();
    }

}
