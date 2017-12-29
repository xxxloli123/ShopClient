package com.zsh.shopclient.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by Administrator on 2017/1/23 0023.
 */

public class AddressEntity implements Parcelable {

    /**
     * id : 402881f05bf61ad8015bf61c610a0000
     * pro : 重庆市
     * city : 重庆市
     * district : 江北区
     * street : 500105003000
     * houseNumber : 32
     * lat : 29.588157
     * lng : 106.483779
     * type : 0
     * count : 0
     * addtype : 0
     * personname : ww
     * personphone : 13111111111
     */


    private String id;
    private String pro;
    private String city;
    private String district;
    private String street;
    private String houseNumber;
    private double lat;
    private double lng;
    private int type;
    private int count;
    private int addtype;
    private String personname;
    private String personphone;
    private String defaultAddress;

    public AddressEntity() {
    }

    protected AddressEntity(Parcel in) {
        id = in.readString();
        pro = in.readString();
        city = in.readString();
        district = in.readString();
        street = in.readString();
        houseNumber = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
        type = in.readInt();
        count = in.readInt();
        addtype = in.readInt();
        personname = in.readString();
        personphone = in.readString();
    }

    public static final Creator<AddressEntity> CREATOR = new Creator<AddressEntity>() {
        @Override
        public AddressEntity createFromParcel(Parcel in) {
            return new AddressEntity(in);
        }

        @Override
        public AddressEntity[] newArray(int size) {
            return new AddressEntity[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getAddtype() {
        return addtype;
    }

    public void setAddtype(int addtype) {
        this.addtype = addtype;
    }

    public String getPersonname() {
        return personname;
    }

    public void setPersonname(String personname) {
        this.personname = personname;
    }

    public String getPersonphone() {
        return personphone;
    }

    public void setPersonphone(String personphone) {
        this.personphone = personphone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(String defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public boolean isDefault() {
        return TextUtils.equals("yes", defaultAddress);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(pro);
        dest.writeString(city);
        dest.writeString(district);
        dest.writeString(street);
        dest.writeString(houseNumber);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeInt(type);
        dest.writeInt(count);
        dest.writeInt(addtype);
        dest.writeString(personname);
        dest.writeString(personphone);
    }
}
