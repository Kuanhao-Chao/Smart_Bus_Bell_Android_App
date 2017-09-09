package com.example.android.map;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.map.GeoIntentService.GeoFenceErrorMessage;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.map.MapsActivity.buttonChecker;

/**
 * Created by Howard on 2017/7/20.
 */

public class GeofenceTransitionsIntentService extends IntentService {
    protected static final String TAG = "gfService";
    public GeofenceTransitionsIntentService(){
        super(TAG);
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }
    @Override
    public void onHandleIntent(Intent intent){
            GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()){
            String errorMessage = GeoFenceErrorMessage.getErrorString(this, geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }
        //to get the user's transition type
        int geofenceTransition = geofencingEvent.getGeofenceTransition();


        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ){
            //to hold the geofences that we strigger
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            //to get the transition details as String
            String geofenceTransitionDetails = getGeofenceTransitiondetails(this, geofenceTransition, triggeringGeofences);
            //Send notification ad log the transition details
            sendNotification(geofenceTransitionDetails);
            Log.e(TAG, geofenceTransitionDetails);
        }
        else {
            //Log the error
            Log.e(TAG, getString(geofenceTransition));
        }

    }

    private String getGeofenceTransitiondetails(Context context, int geofenceTransiton, List<Geofence> triggeringGeofences ){
        String geofenceTransitonString = getGeofenceTransitionString(geofenceTransiton);
        //get the IDs of each geofence that was triggered.
        ArrayList triggeringGeoFencesIdsList = new ArrayList();
        for (Geofence geofence : triggeringGeofences){
            triggeringGeoFencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeoFencesIdsString = TextUtils.join(",", triggeringGeoFencesIdsList);
        return geofenceTransitonString + ": " + triggeringGeoFencesIdsString;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification(String notificationDetails){
        //create the intent to start the main activity
        Intent notificationIntent = new Intent(getApplicationContext(), MapsActivity.class);

        //contruct a task stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //add the main activity to the task stack as the parent
        stackBuilder.addParentStack(MapsActivity.class);
        //push the content Intent ontp the stack
        stackBuilder.addNextIntent(notificationIntent);
        //get a PendingIntent containing the entire back stack
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
        //get a notification builder that's coompatible with platform version
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        //Define the notification setting
        builder.setSmallIcon(R.mipmap.ic_launcher).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText("進入app請按通知")
                .setContentIntent(notificationPendingIntent);
        builder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notifyID = 0;
        mNotificationManager.notify(notifyID, builder.build());
    }

    private String getGeofenceTransitionString(int transitonType){
        switch (transitonType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                buttonChecker = true;
                return "你剛進入了公車站";
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                buttonChecker = false;
                return "你剛離開了公車站";
            default:
                //Geofence.GEOFENCE_TRANSITION_DWELL
                return "GEOFENCE_TRANSITION_DWELL";
        }
    }
}
