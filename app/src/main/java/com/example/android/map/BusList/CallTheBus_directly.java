package com.example.android.map.BusList;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.map.Buttons.ClickButtonsToCallBus;
import com.example.android.map.MapsActivity;
import com.example.android.map.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Howard on 2017/7/15.
 */

public class CallTheBus_directly extends AppCompatActivity {

    private static final String LOG_TAG = CallTheBus_directly.class.getSimpleName();
    private static final String USGS_REQUEST_URL = "http://192.168.0.110:5000/time/";
    private BusParameterAdaptor mAdapter;
    private List<BusParameter> result = new ArrayList<BusParameter>();
    //private List<BusParameter>busList = new ArrayList<BusParameter>();
    private static String busNumber = new String();

//    private static String shareBusStopLocationId = "";
    private static String BusStopLocationId = "";
    //the value that get when the user is in the stop and click the bus stop.
    //the value will be change once the user click the bus stop.
    //this value will be send to call the bus when the user click the button " call the bus".
    MapsActivity mapsActivity = new MapsActivity();
    private String theBusStopName = new String();

    private Handler mHandler;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {


            //setTitle(theBusStopName);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callthebus_directly);

//        *final ArrayList<BusParameter> busList = new ArrayList<BusParameter>();
//        busList.add(new BusParameter(/*R.drawable.color_black,*/ "4 mins", "低底盤公車", "台北車站", "南港", "藍114"));
//        busList.add(new BusParameter(/*R.drawable.color_black,*/"4 mins", "低底盤公車", "台北車站", "南港", "藍114"));
//        busList.add(new BusParameter(/*R.drawable.color_black,*/ "4 mins", "低底盤公車", "台北車站", "南港", "藍114"));
//        busList.add(new BusParameter(/*R.drawable.color_black,*/ "4 mins", "低底盤公車", "台北車站", "南港", "藍114"));
//        busList.add(new BusParameter(/*R.drawable.color_black,*/ "4 mins", "低底盤公車", "台北車站", "南港", "藍114"));
//        busList.add(new BusParameter(/*R.drawable.color_black,*/ "4 mins", "低底盤公車", "台北車站", "南港", "藍114"));
//        busList.add(new BusParameter(/*R.drawable.color_black,*/ "4 mins", "低底盤公車", "台北車站", "南港", "藍114"));
//        busList.add(new BusParameter(/*R.drawable.color_black,*/ "4 mins", "低底盤公車", "台北車站", "南港", "藍114"));
//        busList.add(new BusParameter(/*R.drawable.color_black,*/"4 mins", "低底盤公車", "台北車站", "南港", "藍114"));

        Intent intent = this.getIntent();
        BusStopLocationId = intent.getStringExtra("shareBusStopLocationId");
        ListView callTheBusDirectlyListView = findViewById(R.id.list);
        mAdapter = new BusParameterAdaptor(this, new ArrayList<BusParameter>());
        callTheBusDirectlyListView.setAdapter(mAdapter);

        callTheBusDirectlyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                busNumber = result.get((int)id).getmBusNumber();
                BusStopLocationId = result.get(0).getmStopLocationId();
                Intent theButtons = new Intent();
                theButtons.setClass(CallTheBus_directly.this, ClickButtonsToCallBus.class);
                Bundle bundle = new Bundle();
                bundle.putString("BusStopLocationId", BusStopLocationId);
                bundle.putString("busNumber", busNumber);
                theButtons.putExtras(bundle);
                startActivity(theButtons);
            }
        });

        //check the internet connection
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //activate the http request to do in the background
        mHandler = new Handler();
        BusAsyncTask task01 = new BusAsyncTask();
        task01.execute(USGS_REQUEST_URL);
    }
    public static String getmBusName () {
        return busNumber;
    }




    private class BusAsyncTask extends AsyncTask<String, Void, List<BusParameter>> {

        @Override
        protected List<BusParameter> doInBackground (String... urls){
            if (urls.length < 1 || urls[0] == null ){
                return null;
            }
            int busStopLocationId;
            double currentLatitude;
            double currentLongitude;
            //currentLatitude = mcurrentLatitude;
            //currentLongitude = mcurrentLongitude;
            urls[0] = urls[0] + BusStopLocationId;
            //urls[1] = urls[1] + currentLatitude +'/' + currentLongitude;
            Log.i(LOG_TAG, urls[0]);
            //Log.i(LOG_TAG, urls[1]);
            result = QueryUtils.fetchBusInfo(urls[0]);
            return result;
        }


        @Override
        protected  void onPostExecute(List<BusParameter> data){

            //List<BusParameter> getData = new ArrayList<BusParameter>();
            /*for (int i=0; i < data.size(); i++){
                getResult.set(i, data.get(i));
            }*/
            mAdapter.clear();
            if (data != null && !data.isEmpty()){
                mAdapter.addAll(data);

                /*Log.v("Howard87", "1");
                for ( int i=0; i<mapsActivity.getTheNumberOfTheBusStops() ; i++){
                    Log.v("Howard87", "2");
                    if ( mapsActivity.getmBusmStopLocationId()[i] == shareBusStopLocationId){
                        Log.v("Howard87", "3");
                        theBusStopName = mapsActivity.getmBusStopName()[i];
                        break;
                    }
                }*/

                //mHandler.post(mRunnable);


            }

        }
    }
}
