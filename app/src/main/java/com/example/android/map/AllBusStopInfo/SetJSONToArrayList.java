package com.example.android.map.AllBusStopInfo;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;



/**
 * Created by Howard on 2017/7/18.
 */

public class SetJSONToArrayList {

    public SetJSONToArrayList(){
    }
    public static ArrayList<BusStopParameter> extractJsonfrom(Context context){
        String json = "";
        try{
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("List_Map_Stop_Path.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        ArrayList<BusStopParameter> busStopsList = new ArrayList<BusStopParameter>();
        try{
            JSONObject root = new JSONObject(json);
            JSONArray wrapper = root.getJSONArray("wrapper");
            for (int i=0; i<wrapper.length(); i++){
                try {
                    JSONObject elementsInWrapper = wrapper.getJSONObject(i);
                    int getStopLocationId = elementsInWrapper.getInt("stopLocationId");
                    String getName = elementsInWrapper.getString("name");
                    JSONObject location = elementsInWrapper.getJSONObject("location");
                    double getLatitude = location.getDouble("lat");
                    double getLongitude = location.getDouble("lng");
                    BusStopParameter busStop = new BusStopParameter(getStopLocationId, getName, getLatitude, getLongitude);
                    busStopsList.add(busStop);
                }catch (JSONException e){
                    Log.e("", "", e);
                }
            }
        }
        catch (JSONException e){
            Log.e("SetJSONToArrayList", "Problem parsing the earthquake JSON results", e);
        }
        return busStopsList;
    }
}
