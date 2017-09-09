package com.example.android.map.Success_Screen_Cancel_Screen;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.map.BusList.BusParameter;
import com.example.android.map.BusList.QueryUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Howard on 2017/7/21.
 */

//the helper function relating to requesting nad receiving the data from Azure
public final class QueryUtils_Transmit_BusBell {
    private static final String LOG_TAG = QueryUtils_Transmit_BusBell.class.getSimpleName();
    private  QueryUtils_Transmit_BusBell(){
    }

    public static List<BusParameter> fetchBusInfo(String requestUrl){
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try{
            jsonResponse = makeHTTPRequest(url);
        }
        catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        //List<BusParameter> BusInfos = extractFeatureFormJson(jsonResponse);
        return  null;
    }

    private  static  URL createUrl (String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    private static String makeHTTPRequest(URL url) throws IOException{
        String jsonResponse = "";
        if (url == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(100000);
            urlConnection.setConnectTimeout(150000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else{
                Log.e(LOG_TAG, "Error response code:"+ urlConnection.getResponseCode());
            }
        }
        catch (IOException e ){
            Log.e(LOG_TAG, "Problem retrieving from the earthquake JSON results", e);
        }finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static List<BusParameter> extractFeatureFormJson (String jsonResponse){
        if (TextUtils.isEmpty(jsonResponse)){
            return null;
        }
//        // Create an empty ArrayList that we can start adding earthquakes to
//        ArrayList<BusParameter> routesInTheStopYouClick = new ArrayList<BusParameter>();
//        // Try to parse the JSON response string. If there's a problem with the way the JSON
//        // is formatted, a JSONException exception object will be thrown.
//        // Catch the exception so the app doesn't crash, and print the error message to the logs.
//        try {
//            // build up a list of Earthquake objects with the corresponding data.
//
//            JSONObject root = new JSONObject(jsonResponse);
//            String getStopLocationId = root.getString("stopLocationId");
//            JSONArray wrapper = root.getJSONArray("wrapper");
//            for (int i=0; i<wrapper.length(); i++) {
//                JSONObject elementsInWrapper = wrapper. getJSONObject(i);
//                String getEstimateTime = elementsInWrapper.getString("EstimateTime");
//                String getCarType = elementsInWrapper.getString("cartype");
//                String getFrom = elementsInWrapper.getString("from");
//                String getTo = elementsInWrapper.getString("to");
//                String getNumber = elementsInWrapper.getString("num");
//
//                BusParameter route = new BusParameter( getStopLocationId, getEstimateTime, getCarType, getFrom, getTo, getNumber);
//                routesInTheStopYouClick.add(route);
//            }
//
//        }catch (JSONException e){
//            // If an error is thrown when executing any of the above statements in the "try" block,
//            // catch the exception here, so the app doesn't crash. Print a log message
//            // with the message from the exception.
//            Log.e("QueryUtils", "Problem parsing the JSON results", e);
//        }
        return null;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while ( line !=  null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return  output.toString();
    }
}

