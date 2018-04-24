package com.example.heshu.mymap.bean;

/**
 * Created by heshu on 2018/3/22.
 */

public class ImageBean {
    private String url;
    private int width;
    private int height;
    private int drawable;
    private int type; // 1是网络图片、本地图片  ，2是drawable

    public ImageBean(String url,int type, int width, int height) {
        this.type=type;
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public ImageBean(int drawable,int type, int width, int height) {
        this.type=type;
        this.drawable = drawable;
        this.width = width;
        this.height = height;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
