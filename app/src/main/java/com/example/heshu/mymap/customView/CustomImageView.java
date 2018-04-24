package com.example.heshu.mymap.customView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.bumptech.glide.Glide;

/**
 * Created by heshu on 2018/3/22.
 */

public class CustomImageView extends android.support.v7.widget.AppCompatImageView {
    private String url;
    private int intUri;
    private boolean isAttachedToWindow;
    private static final String TAG = "CustomImageView";

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Drawable drawable = getDrawable();
                if (drawable != null) {
                    drawable.mutate().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
                //falls through
            case MotionEvent.ACTION_UP:
                Drawable drawable1Up = getDrawable();
                if (drawable1Up != null) {
                    drawable1Up.mutate().clearColorFilter();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onAttachedToWindow() {
        isAttachedToWindow = true;
        if(url !=null) {
            setImageUrl(url);
        }else {
            setImageUrl(intUri);
        }
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        isAttachedToWindow = false;
        setImageBitmap(null);
        super.onDetachedFromWindow();
    }

    public void setImageUrl(String url) {
        this.url = url;
        if (isAttachedToWindow) {
            Glide.with(getContext()).load(url).placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(this);
        }
        Log.d(TAG, "setImageUrl: " + url);
    }

    public void setImageUrl(int url) {
        this.url = null;
        this.intUri = url;
        if (isAttachedToWindow) {
            Glide.with(getContext()).load(url).placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(this);
        }
        Log.d(TAG, "setImageUrl int: " + url);
    }
}
