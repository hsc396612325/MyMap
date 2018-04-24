package com.example.heshu.mymap.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.heshu.mymap.R;
import com.example.heshu.mymap.bean.ImageBean;
import com.example.heshu.mymap.bean.MessageBean;
import com.example.heshu.mymap.customView.CustomImageView;
import com.example.heshu.mymap.customView.NineGridlayout;
import com.example.heshu.mymap.customView.ScreenTools;
import com.example.heshu.mymap.gson.RetrofitReturn;
import com.example.heshu.mymap.network.ILikeAndRemarksRequest;
import com.example.heshu.mymap.network.RequestFactory;
import com.example.heshu.mymap.util.MediaPlayerUtil;
import com.example.heshu.mymap.view.App;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heshu on 2018/3/8.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context mContext;
    private List<MessageBean> mMessageBeanList;
    private static final String TAG = "MessageAdapter";

    private static final int TYPE_COMMENT = 0;
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_VOICE = 2;
    private static final int TYPE_VIDEO = 3;

    private static int mType;
    private String mToken;

    private boolean playFlag = false;
    public MediaPlayer mMediaPlayer = new MediaPlayer();

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameText;
        TextView commentText;
        TextView date;
        TextView likeNum;
        TextView comtNum;
        Button likeButton;
        Button comtButton;

        public NineGridlayout imageGridlayout;
        public CustomImageView imageConView;

        Button playButton;

        VideoView videoView;
        ImageView videoImage;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.head_portrait);
            nameText = (TextView) itemView.findViewById(R.id.name);
            date = (TextView) itemView.findViewById(R.id.date);
            likeNum = (TextView) itemView.findViewById(R.id.like_num);
            likeButton = (Button) itemView.findViewById(R.id.like_icon);
            comtButton = (Button) itemView.findViewById(R.id.comt_icon);
            comtNum = (TextView) itemView.findViewById(R.id.comt_num);
            if (mType == TYPE_COMMENT) {
                commentText = (TextView) itemView.findViewById(R.id.comment_text);
            } else if (mType == TYPE_IMAGE) {
                imageGridlayout = (NineGridlayout) itemView.findViewById(R.id.iv_ngrid_layout);
                imageConView = (CustomImageView) itemView.findViewById(R.id.iv_oneimage);
            } else if (mType == TYPE_VOICE) {
                playButton = (Button) itemView.findViewById(R.id.play_button);
            } else if (mType == TYPE_VIDEO) {
                videoView = (VideoView)itemView.findViewById(R.id.video);
                videoImage = (ImageView)itemView.findViewById(R.id.video_image);
            }
        }
    }

    public MessageAdapter(List<MessageBean> messageBeanList, int type) {
        mMessageBeanList = messageBeanList;
        mType = type;
        SharedPreferences preferences = App.getContext().getSharedPreferences("token", Context.MODE_PRIVATE);
        mToken = preferences.getString("token", "");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }

        View view = null;
        if (mType == TYPE_COMMENT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.activity_show_message_comment_item, parent, false);
        } else if (mType == TYPE_IMAGE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.activity_show_message_image_item, parent, false);
        } else if (mType == TYPE_VOICE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.activity_show_message_voice_item, parent, false);
        } else if (mType == TYPE_VIDEO) {
            view = LayoutInflater.from(mContext).inflate(R.layout.activity_show_message_video_item, parent, false);
        }
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MessageBean messageBean = mMessageBeanList.get(position);
        holder.nameText.setText(messageBean.getName());
        holder.date.setText(messageBean.getDate());
        holder.comtNum.setText("(" + messageBean.getComtNum() + ")");
        holder.likeNum.setText("(" + messageBean.getLikeNum() + ")");
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!messageBean.isLikeFlag()) {
                    holder.likeNum.setText("(" + (messageBean.getLikeNum() + 1) + ")");
                    messageBean.setLikeFlag(true);
                    isLike(messageBean.getCommentId());
                }
            }
        });
        final String Prefix = "http://47.95.207.40/markMapFile";
        if (mType == TYPE_COMMENT) {
            holder.commentText.setText(messageBean.getCommentText());
        } else if (mType == TYPE_IMAGE) {
            if (messageBean.getCommentText() != null) {
                String Uri[] = messageBean.getCommentText().split("&");
                List<ImageBean> list = new ArrayList<>();
                for (int i = 0; i < Uri.length; i++) {
                    ImageBean imageBean = new ImageBean(Prefix + Uri[i], 1, 200, 200);
                    list.add(imageBean);
                }
                if(list.size() ==1){
                    holder.imageGridlayout.setVisibility(View.GONE);
                    holder.imageConView.setVisibility(View.VISIBLE);
                    handlerOneImage(holder,list.get(0));
                }else {
                    holder.imageGridlayout.setVisibility(View.VISIBLE);
                    holder.imageConView.setVisibility(View.GONE);
                    holder.imageGridlayout.setImagesData(list);
                }
            }
        } else if (mType == TYPE_VOICE) {
            MediaPlayerUtil.initPaly(Prefix + messageBean.getCommentText());
            holder.playButton.setText(MediaPlayerUtil.mediaPlayerDate());
            MediaPlayerUtil.mediaPlayerStop();
            holder.playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MediaPlayerUtil.initPaly(Prefix + messageBean.getCommentText());
                    MediaPlayerUtil.mediaPlayerStart();
                }
            });
        } else if(mType == TYPE_VIDEO){
            holder.videoView.setMediaController(new MediaController(mContext));
            holder.videoView.setOnCompletionListener( new MyPlayerOnCompletionListener());
            holder.videoView.setZOrderOnTop(true);
            holder.videoImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.videoImage.setVisibility(View.INVISIBLE);
                    holder.videoView.setVisibility(View.VISIBLE);

//                  holder.videoView.setVideoURI(Uri.parse(Prefix + messageBean.getCommentText()));
                    holder.videoView.setVideoURI(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
                    holder.videoView.start();
                    Log.d(TAG, "onClick: "+"http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
                    holder.videoView.requestFocus();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mMessageBeanList.size();
    }

    //点赞
    private void isLike(int CommentId) {
        ILikeAndRemarksRequest request = RequestFactory.getRetrofit().create(ILikeAndRemarksRequest.class);

        Call call = request.addLike(mToken, 1, CommentId);
        Log.d(TAG, "isLike: " + CommentId);
        //发送网络请求(异步)
        call.enqueue(new Callback<RetrofitReturn>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<RetrofitReturn> call, Response<RetrofitReturn> response) {
                RetrofitReturn retrofitPoint = response.body();
                Log.d("pushComment", "" + response);
                Log.d("pushComment", "" + retrofitPoint);
                Log.d("pushComment", "" + retrofitPoint.message);
                Log.d("pushComment", "" + retrofitPoint.status);
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<RetrofitReturn> call, Throwable throwable) {
                Log.d("连接失败", "" + throwable.toString());
            }
        });
    }

    //加载图片
    private void handlerOneImage(final ViewHolder holder, ImageBean imageBean) {
        int totalWidth;
        int imageWidth;
        int imageHeight;
        ScreenTools screenTools = ScreenTools.instance(mContext);
        totalWidth = screenTools.getScreenWidth() - screenTools.dip2px(80);
        imageWidth = screenTools.dip2px(imageBean.getWidth());
        imageHeight = screenTools.dip2px(imageBean.getHeight());
        if (imageBean.getWidth() <= imageBean.getHeight()) {
            if (imageHeight > totalWidth) {
                imageHeight = totalWidth;
                imageWidth = (imageHeight * imageBean.getWidth()) / imageBean.getHeight();
            }
        } else {
            if (imageWidth > totalWidth) {
                imageWidth = totalWidth;
                imageHeight = (imageWidth * imageBean.getHeight()) / imageBean.getWidth();
            }
        }

        ViewGroup.LayoutParams layoutParams = holder.imageConView.getLayoutParams();
        layoutParams.height = imageHeight;
        layoutParams.width = imageWidth;
        holder.imageConView.setLayoutParams(layoutParams);
        holder.imageConView.setClickable(true);
        holder.imageConView.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.imageConView.setImageUrl(imageBean.getUrl());
        Log.d(TAG, "handlerOneImage: " + imageBean.getUrl());
    }
    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText( mContext, "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }
}