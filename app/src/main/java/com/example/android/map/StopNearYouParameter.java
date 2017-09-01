package com.example.android.map;

import com.example.android.map.BusList.BusParameter;

import java.util.ArrayList;

/**
 * Created by Howard on 2017/7/17.
 */

public class StopNearYouParameter {
    private String mStopLocationID;
    private double mLengthToUser;
    private String mNearStopName;
    private double mLatitude;
    private double mLongitude;
    private ArrayList<BusParameter> mRouteList;

    public StopNearYouParameter (String stopLocationID, double lengthToUser, String nearStopName, double Latitude, double Longitude, ArrayList<BusParameter> routeList) {
        mStopLocationID = stopLocationID;
        mLengthToUser = lengthToUser;
        mNearStopName = nearStopName;
        mLatitude = Latitude;
        mLongitude = Longitude;
        mRouteList = routeList;
    }

    public  String getmStopLocationID() { return mStopLocationID;}
    public double getmLengthToUser(){ return mLengthToUser;}
    public double getmLongitude(){ return  mLongitude;}
    public double getmLatitude() {return  mLatitude;}
    public String getmNearStopName() { return mNearStopName;}
    public ArrayList<BusParameter> getmRouteList() {return mRouteList;}
}
