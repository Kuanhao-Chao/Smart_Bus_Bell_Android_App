package com.example.android.map.Cluster;

import com.example.android.map.AllBusStopInfo.SetJSONToArrayList;
import com.example.android.map.MapsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

/**
 * Created by Howard on 2017/7/21.
 */

public class Cluster implements ClusterItem {

    private LatLng mPosition;
    private String mTitle;
    private String mTag;

    public Cluster(LatLng latLng, String string, String tag) {
        mPosition = latLng;
        mTitle = string;
        mTag = tag;
    }
    @Override
    public LatLng getPosition() {
        return mPosition;
    }
    public String getTitle(){
        return mTitle;
    }
    public String getTag(){
        return  mTag;
    }
}

