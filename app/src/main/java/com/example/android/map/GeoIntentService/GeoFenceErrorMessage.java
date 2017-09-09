package com.example.android.map.GeoIntentService;

import android.content.Context;
import android.content.res.Resources;

import com.google.android.gms.location.GeofenceStatusCodes;

/**
 * Created by Howard on 2017/7/20.
 */

public class GeoFenceErrorMessage {
    private GeoFenceErrorMessage(){}
    public static String getErrorString (Context context, int errorCode){
        Resources mResources = context.getResources();
        switch (errorCode){
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "Geofence service is not available now";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Your app has register to many geofences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "You have provide too many padding intent to geofences.";
            default:
                return "Geofence service is not available now";
        }
    }
}
