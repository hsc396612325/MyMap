package com.example.heshu.mymap.customView;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.heshu.mymap.bean.ImageBean;
import com.example.heshu.mymap.view.AddActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heshu on 2018/3/22.
 */

public class NineGridlayout extends ViewGroup {
    /**
     * 图片间的间隔
     */
    private int gap = 5;
    private int columns;
    private int rows;
    private List<ImageBean> listData;
    private int totalWidth;
    private Context mContext;
    private static final String TAG = "NineGridlayout";

    public NineGridlayout(Context context) {

        super(context);
        mContext = context;
    }

    public NineGridlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        ScreenTools screenTools = ScreenTools.instance(context);
        totalWidth = screenTools.getScreenWidth() - screenTools.dip2px(20);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    private void layoutChildrenView(List<ImageBean> lists) {
        int childrenCount = lists.size();
        int singleWidth;
        if ((totalWidth - gap * (3 - 1)) / 3 > lists.get(0).getWidth()) {
            singleWidth = lists.get(0).getWidth();
        } else {
            singleWidth = (totalWidth - gap * (3 - 1)) / 3;
        }


        int singleHeight = singleWidth;

        //根据子view数量确定高度
        LayoutParams params = getLayoutParams();
        params.height = singleHeight * rows + gap * (rows - 1);
        setLayoutParams(params);

        for (int i = 0; i < childrenCount && i < 9; i++) {
            Log.d(TAG, "layoutChildrenView: " + i);

            CustomImageView childrenView = (CustomImageView) getChildAt(i);
            Log.d(TAG, "layoutChildrenView: " + childrenView);

            if (childrenView != null) {
                if (((ImageBean) lists.get(i)).getType() == 1) {
                    childrenView.setImageUrl(((ImageBean) lists.get(i)).getUrl());
                } else {
                    childrenView.setImageUrl(((ImageBean) lists.get(i)).getDrawable());
                }

                int[] position = findPosition(i);
                int left = (singleWidth + gap) * position[1];
                int top = (singleHeight + gap) * position[0];
                int right = left + singleWidth;
                int bottom = top + singleHeight;

                childrenView.layout(left, top, right, bottom);
            }
        }
    }

    private int[] findPosition(int childNum) {
        int[] position = new int[2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i * columns + j) == childNum) {
                    position[0] = i;
                    position[1] = j;
                    break;
                }
            }
        }
        return position;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

    public void setImagesData(List<ImageBean> lists) {
        if (lists == null || lists.isEmpty()) {
            return;
        }

        //初始化布局
        generateChildrenLayout(lists.size());
        //这里做一个重用view的处理
        if (listData == null) {
            int i = 0;
            while (i < lists.size()) {
                if (lists.get(i).getType() == 1) {
                    CustomImageView iv = generateImageView(i);
                    addView(iv, generateDefaultLayoutParams());
                } else {
                    CustomImageView iv = generateAddImageView();
                    addView(iv, generateDefaultLayoutParams());
                }
                i++;
            }
        } else {
            int num = lists.size() - 1;
            removeViewAt(num - 1);
            if (lists.get(num).getType() == 2) {
                CustomImageView iv = generateImageView(0);
                addView(iv, generateDefaultLayoutParams());
                CustomImageView ivAdd = generateAddImageView();
                addView(ivAdd, generateDefaultLayoutParams());
            } else {
                CustomImageView iv = generateImageView(0);
                addView(iv, generateDefaultLayoutParams());
            }

        }

        listData = lists;
        Log.d(TAG, "setImagesData: " + listData.size());
        layoutChildrenView(lists);
    }

    /**
     * 根据图片个数确定行列数量
     * 对应关系如下
     * num	row	column
     * 1	   1	1
     * 2	   1	2
     * 3	   1	3
     * 4	   2	2
     * 5	   2	3
     * 6	   2	3
     * 7	   3	3
     * 8	   3	3
     * 9	   3	3
     *
     * @param length
     */
    private void generateChildrenLayout(int length) {
        if (length <= 3) {
            rows = 1;
            columns = length;
        } else if (length <= 6) {
            rows = 2;
            columns = 3;
            if (length == 4) {
                columns = 2;
            }
        } else {
            rows = 3;
            columns = 3;
        }
    }

    private CustomImageView generateImageView(final int i) {
        CustomImageView iv = new CustomImageView(getContext());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!listData.isEmpty()) {
                    List<String > URLS = new ArrayList<>();

                    for(ImageBean imageBean :listData){
                        URLS.add(imageBean.getUrl());
                    }
                    new ShowImagesDialog(mContext,URLS, i).show();
                    //showInputDialog();
                    Log.d(TAG, "onClick: "+i);
                }
            }
        });
        iv.setBackgroundColor(Color.parseColor("#f5f5f5"));
        return iv;
    }

    private CustomImageView generateAddImageView() {
        CustomImageView iv = new CustomImageView(getContext());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 2222" + getContext());
                AddActivity addActivity = (AddActivity) getContext();
                addActivity.addImage();
            }
        });
        iv.setBackgroundColor(Color.parseColor("#f5f5f5"));
        return iv;
    }

    private void showInputDialog() {
    /*@setView 装入一个EditView
     */
        final EditText editText = new EditText(mContext);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(mContext);
        inputDialog.setTitle("输入该点的名字").setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        inputDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }
}
