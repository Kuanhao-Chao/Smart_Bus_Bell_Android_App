package com.example.android.map.CancelBell;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.map.BusList.CallTheBus_directly;
import com.example.android.map.Buttons.BusStopOnTheRouteParameter;
import com.example.android.map.MapsActivity;
import com.example.android.map.R;

import java.util.List;


/**
 * Created by Howard on 2017/7/21.
 */

public class Cancel_Bus_Successfully extends AppCompatActivity {

    private String LOG_TAG = CallTheBus_directly.class.getSimpleName();
    private static final String CANCEL_URL_REQUEST = "http://192.168.0.110:5000/cancel/";

    private static String BusStopLocationId = "";
    private static int BusBell = 0;
    private static int RouteId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancel_bus_successful);
        Bundle bundle = getIntent().getExtras();
        BusStopLocationId = bundle.getString("BusStopLocationId");
        BusBell = bundle.getInt("BusBell");
        RouteId = bundle.getInt("RouteId");

        TextView succeedCallBus = findViewById(R.id.IDcancellTheBus_textview);
        Button cancelButtonDisable = findViewById(R.id.IDcancellTheBus_button);
        cancelButtonDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Cancel_Bus_Successfully.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        BusAsyncTask task02 = new BusAsyncTask();
        task02.execute(CANCEL_URL_REQUEST);
    }

    private class BusAsyncTask extends AsyncTask<String, Void, List<BusStopOnTheRouteParameter>> {

        @Override
        protected List<BusStopOnTheRouteParameter> doInBackground (String... urls){
            if (urls.length < 1 || urls[0] == null ){
                return null;
            }
            String busBell;
            String routeId;
            String stopLocationId;
            busBell = String.valueOf(BusBell);
            routeId = String.valueOf(RouteId);
            stopLocationId = BusStopLocationId;

            urls[0] = urls[0]+ routeId+'/'+stopLocationId + '/'+ busBell;
            Log.i(LOG_TAG, urls[0]);
            //Log.i(LOG_TAG, urls[1]);
            QueryUtils_Cancel_Bell.fetchBusInfo(urls[0]);
            return null;
        }

        @Override
        protected  void onPostExecute(List<BusStopOnTheRouteParameter> data){
            if (data != null && !data.isEmpty()){
            }
        }
    }
}
