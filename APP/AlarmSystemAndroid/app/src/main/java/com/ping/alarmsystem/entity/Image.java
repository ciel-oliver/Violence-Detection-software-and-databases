package com.ping.alarmsystem.entity;

import java.util.Date;

public class Image {
    private Integer imageId;

    private String createTime;

    private String imageContentId;

    private String imageUrl;

    //1-介绍图片，2-目录图片
    private int imageType;

    public int getImageType() {
        return imageType;
    }

    public void setImageType(int imageType) {
        this.imageType = imageType;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getImageContentId() {
        return imageContentId;
    }

    public void setImageContentId(String imageContentId) {
        this.imageContentId = imageContentId == null ? null : imageContentId.trim();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }
}