package com.example.android.map.AllBusStopInfo;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Howard on 2017/7/18.
 */

public class BusStopParameter {
    private int mStopLocationId;
    private String mBusStopName;
    private double mLatitude;
    private double mLongitude;

    public BusStopParameter(int stopLocationId, String busStopName, double latitude, double longitude){
        mStopLocationId = stopLocationId;
        mBusStopName = busStopName;
        mLatitude = latitude;
        mLongitude = longitude;
        mLatitude = latitude;
        mLongitude = longitude;
    }
    public int getmStopLocationId() { return mStopLocationId;}
    public String getmBusStopName() { return mBusStopName;}
    public LatLng getmLatLng (){
        LatLng busStopLatLng = new LatLng(mLatitude, mLongitude);
        return  busStopLatLng;
    }
}
