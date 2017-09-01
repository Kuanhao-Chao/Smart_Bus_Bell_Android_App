//package com.example.android.map.Buttons;
//
//import android.content.Context;
//import android.content.res.AssetManager;
//import android.util.Log;
//
//import com.example.android.map.AllBusStopInfo.BusStopParameter;
//import com.example.android.map.BusList.CallTheBus_directly;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//
///**
// * Created by Howard on 2017/7/21.
// */
//
//public class SetJSONToArrayList_theRoute {
//
//    public static BusStopOnTheRouteParameter extractJsonfrom (Context context){
//
//        CallTheBus_directly callTheBus_directly = new CallTheBus_directly();
//        String busNumber = new String ();
//        busNumber = callTheBus_directly.getBusNumber();
//
//        String json = "";
//        try{
//            AssetManager assetManager = context.getAssets();
//            InputStream inputStream = assetManager.open("Howard.json");
//            int size = inputStream.available();
//            byte[] buffer = new byte[size];
//            inputStream.read(buffer);
//            inputStream.close();
//            json = new String(buffer, "UTF-8");
//
//        }catch (IOException e){
//            e.printStackTrace();
//            return null;
//        }
//        //ArrayList<BusStopOnTheRouteParameter> busStopsList = new ArrayList<BusStopOnTheRouteParameter>();
//        try{
//            JSONObject root = new JSONObject(json);
//            JSONArray wrapper = root.getJSONArray("wrapper");
//            for (int i=0; i<wrapper.length(); i++){
//                try {
//                    boolean flag = false;
//                    while(flag) {
//                        JSONObject elementsInWrapper = wrapper.getJSONObject(i);
//                        JSONArray getBusName = elementsInWrapper.getJSONArray("")
//                        if ( getBusName == busNumber ){
//                            flag = true;
//                        }
//                    }
//                    JSONObject getPath =
//                }catch (JSONException e){
//                    Log.e("", "", e);
//                }
//            }
//        }
//        catch (JSONException e){
//            Log.e("SetJSONToArrayList", "Problem parsing the earthquake JSON results", e);
//        }
//        return busStopsList;
//    }
//}
