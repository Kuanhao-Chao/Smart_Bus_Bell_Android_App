package com.example.android.map.RouteDesign;

import android.text.TextUtils;
import android.util.Log;


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
 * Created by Howard on 2017/7/15.
 */

//the helper function relating to requesting nad receiving the data from Azure
public final class QueryUtils_ResearchResult {
    private static final String LOG_TAG = QueryUtils_ResearchResult.class.getSimpleName();
    private QueryUtils_ResearchResult(){
    }

    public static List<SearchParameter_directly> fetchBusInfo(String requestUrl){
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try{
            jsonResponse = makeHTTPRequest(url);
        }
        catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<SearchParameter_directly> BusInfos = extractFeatureFormJson(jsonResponse);
        return  BusInfos;
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

    private static List<SearchParameter_directly> extractFeatureFormJson (String jsonResponse){
        if (TextUtils.isEmpty(jsonResponse)){
            return null;
        }
        // Create an empty ArrayList that we can start adding earthquakes to
        List<SearchParameter_directly> directlyList = new ArrayList<SearchParameter_directly>();
        List<SearchParameter_Google> GoogleList = new ArrayList<SearchParameter_Google>();
//        SearchParameter searchParameter = new SearchParameter(null, null);
        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // build up a list of Earthquake objects with the corresponding data.
//
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray direct = root.getJSONArray("direct");
            for ( int i=0; i<direct.length(); i++){
                JSONObject elementsWrapper = direct.getJSONObject(i);
                String getEstimateTime = elementsWrapper.getString("EstimateTime");
                JSONObject end = elementsWrapper.getJSONObject("end");
                String getEndNameZh = end.getString("nameZh");
//                String getEndRouteId = String.valueOf(end.getInt("routeId"));
//                int getEndStopLocationId = end.getInt("stopLocationId");
//                double getEndLatitude = end.getDouble("latitude");
//                double getEndlongitude = end.getDouble("longitude");
                String getFrom = elementsWrapper.getString("from");
//                double getLen_end = elementsWrapper.getDouble("len_end");
//                double getlen_start = elementsWrapper.getDouble("len_start");
                String getNumber = elementsWrapper.getString("number");
//                int getRouteId = elementsWrapper.getInt("routeId");
                JSONObject start = elementsWrapper.getJSONObject("start");
//                double getStartLatitude = start.getDouble("latitude");
//                double getStartLongitude = start.getDouble("longitude");
                String getStartNameZh = start.getString("nameZh");
                String getStopLocationIs = String.valueOf(start.getInt("stopLocationId"));
                String getTo = elementsWrapper.getString("to");

                SearchParameter_directly searchParameter_directly = new SearchParameter_directly( getEstimateTime, getNumber, getFrom, getTo, getEndNameZh, getStartNameZh,getStopLocationIs );

                directlyList.add(searchParameter_directly);
//            }
//            JSONArray google = root.getJSONArray("google");
//            for (int i =0; i<google.length(); i++){
//                JSONObject elementsWrapper = google.getJSONObject(i);
//                String getEstimateTime = elementsWrapper.getString("EstimateTime");
//                String getBusNumber = elementsWrapper.getString("num");
////                int getRouteId = elementsWrapper.getInt("routeId");
//                String getFrom = elementsWrapper.getString("from");
//                String getTo = elementsWrapper.getString("to");
//                String getTransfer_times = elementsWrapper.getString("transfer_times");
//                JSONObject start = elementsWrapper.getJSONObject("start");
//                String getStartName = start.getString("name");
//                JSONObject startLocation = start.getJSONObject("location");
////                double getStartLatitude = startLocation.getDouble("lat");
////                double getStartLongitude = startLocation.getDouble("lng");
//                JSONObject end = elementsWrapper.getJSONObject("end");
//                String getEndName = end.getString("name");
//                JSONObject endLocation = end.getJSONObject("location");
////                double getEndLatitude = endLocation.getDouble("lat");
////                double getEndLongitude = endLocation.getDouble("lng");
//                String getInstruction = elementsWrapper.getString("instruction");
//
//                SearchParameter_Google searchParameter_Google = new SearchParameter_Google(getEstimateTime, getBusNumber,
//                        getFrom, getTo, getTransfer_times, getStartName, getEndName, getInstruction);
//                GoogleList.add();
            }
//            SearchParameter searchParameter1 = new SearchParameter( directlyList, GoogleList);


        }catch (JSONException e){
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }
        return directlyList;
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
