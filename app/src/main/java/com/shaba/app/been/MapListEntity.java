package com.shaba.app.been;

import android.os.Parcel;
import android.os.Parcelable;

public class MapListEntity implements Parcelable {

    /**
     *
     */
    private static final long serialVersionUID = 3137631177813079254L;

    private String name;
    private String lat;
    private String lng;
    private String tel;
    private String address;

    protected MapListEntity(Parcel in) {
        name = in.readString();
        lat = in.readString();
        lng = in.readString();
        tel = in.readString();
        address = in.readString();
    }

    public static final Creator<MapListEntity> CREATOR = new Creator<MapListEntity>() {
        @Override
        public MapListEntity createFromParcel(Parcel in) {
            return new MapListEntity(in);
        }

        @Override
        public MapListEntity[] newArray(int size) {
            return new MapListEntity[size];
        }
    };

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "MapListEntity{" +
                "name='" + name + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", tel='" + tel + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(tel);
        dest.writeString(address);
    }
}
