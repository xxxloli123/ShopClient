package com.zsh.shopclient.model;

/**
 * Created by xxxloli on 2017/11/23.
 */

public class Intro {

    /**
     * id : 402880e85f9ee2a8015f9ef420090001
     * status : Normal
     * status_value : 已发布
     * productName : 8
     * platformsort : 88
     * singleIntegral : 88
     * createDate : 2017-11-09 12:05:20
     * details : 88
     * smallImg : ac80072f-ab04-4ed6-8613-a9d008f8e583.jpg
     * pictureLibraryId : 402880e55f6c5154015f6c62ee9d0000
     */

    private String id;
    private String status;
    private String status_value;
    private String productName;
    private int platformsort;
    private int singleIntegral;
    private String createDate;
    private String details;
    private String smallImg;
    private String pictureLibraryId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_value() {
        return status_value;
    }

    public void setStatus_value(String status_value) {
        this.status_value = status_value;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPlatformsort() {
        return platformsort;
    }

    public void setPlatformsort(int platformsort) {
        this.platformsort = platformsort;
    }

    public int getSingleIntegral() {
        return singleIntegral;
    }

    public void setSingleIntegral(int singleIntegral) {
        this.singleIntegral = singleIntegral;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getSmallImg() {
        return smallImg;
    }

    public void setSmallImg(String smallImg) {
        this.smallImg = smallImg;
    }

    public String getPictureLibraryId() {
        return pictureLibraryId;
    }

    public void setPictureLibraryId(String pictureLibraryId) {
        this.pictureLibraryId = pictureLibraryId;
    }
}
