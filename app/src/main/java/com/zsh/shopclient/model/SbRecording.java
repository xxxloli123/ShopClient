package com.zsh.shopclient.model;

/**
 * Created by xxxloli on 2018/1/8.
 */

public class SbRecording {

    /**
     * id : 2c9ad843600620d10160065d2ffe004a
     * userId : 2c9ad8435ff13d8b015ffc1660710203
     * createDate : 2017-11-29 14:00:59
     * recordsdeail : 用户：13312312312于：2017-11-29 14:00:58确认收货,获得掌币84个
     * number : 84
     */

    private String id;
    private String userId;
    private String createDate;
    private String recordsdeail;
    private int number;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getRecordsdeail() {
        return recordsdeail;
    }

    public void setRecordsdeail(String recordsdeail) {
        this.recordsdeail = recordsdeail;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
