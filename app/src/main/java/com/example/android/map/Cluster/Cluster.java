package com.example.android.map.Cluster;


import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

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

