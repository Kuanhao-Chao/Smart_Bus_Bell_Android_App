package com.example.android.map.GeoIntentService;

import com.example.android.map.AllBusStopInfo.BusStopParameter;
import com.example.android.map.MapsActivity;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.android.map.MapsActivity.getAllBusStop;

/**
 * Created by Howard on 2017/7/20.
 */

public final class Constants {
    private Constants(){}
    public static final int GEOFENCE_RADIUS_IN_METERS = 150;
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 2000000;
    public static final int GEOFENCE_TRANSITION_ENTER = 1;
    public static final int GEOFENCE_TRANSITION_EXIT = 2;
    public static final HashMap<String, LatLng > BAY_AREA_LANDMARKS = new HashMap<>();
    static{


        MapsActivity mapsActivity = new MapsActivity();
        int theNumberOfTheBusStops = MapsActivity.getTheNumberOfTheBusStops();
        LatLng[] busStopLatLng = MapsActivity.getmBusStoplatLngs();
        String[] busStopName = MapsActivity.getmBusStopName();
        ArrayList<BusStopParameter> busStopParameters = getAllBusStop();

        //LatLng NTUlatlng = new LatLng(25.069895471987962, 121.52026891708374);
        //BAY_AREA_LANDMARKS.put("NTU", NTUlatlng);
        BAY_AREA_LANDMARKS.put(busStopName[1135], busStopLatLng[1135]);

    }
}
