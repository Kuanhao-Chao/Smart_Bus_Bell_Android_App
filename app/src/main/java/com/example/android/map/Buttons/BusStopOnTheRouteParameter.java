package com.example.android.map.Buttons;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Howard on 2017/7/21.
 */

public class BusStopOnTheRouteParameter {

    private String mShowLat;
    private String mShowLon;
    private String mNameZh;
    private int mBusId;
    private int mStopId;

    public BusStopOnTheRouteParameter( String showLat, String showLon, String nameZh,int busId, int stopId ){
        mShowLat = showLat;
        mShowLon = showLon;
        mNameZh = nameZh;
        mBusId = busId;
        mStopId = stopId;
    }


    public String getmShowLat() { return mShowLat;}
    public String getmShowLon() {return mShowLon;}
    public String getmNameZh() { return mNameZh;}
    public LatLng getLatlng(){
        LatLng busStopLatLng = new LatLng(Double.parseDouble(mShowLat), Double.parseDouble(mShowLon));
        return busStopLatLng;
    }
    public int getmBusId() { return mBusId;}
    public int getmStopId(){ return mStopId;}
}
