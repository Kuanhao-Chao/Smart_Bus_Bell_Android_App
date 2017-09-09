package com.example.android.map.Success_Screen_Cancel_Screen;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.map.Buttons.BusStopOnTheRouteParameter;
import com.example.android.map.CancelBell.Cancel_Bus_Successfully;
import com.example.android.map.R;

import java.util.List;

/**
 * Created by Howard on 2017/7/21.
 */

public class Help extends AppCompatActivity {
    private static final String USGS_REQUEST_URL_BUS_BELL = "http://192.168.0.110:5000/bell/";
    private final String LOG_TAG = Help.class.getSimpleName();
    private static String BusStopLocationId = "";
    private static int BusBell = 0;
    private static int RouteId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_call);
        Bundle bundle = getIntent().getExtras();
        BusStopLocationId = bundle.getString("BusStopLocationId");
        BusBell = bundle.getInt("BusBell");
        RouteId = bundle.getInt("RouteId");
        TextView succeedCallBus = findViewById(R.id.IDSuccess);
        Button cancelButtonDisable = findViewById(R.id.IDCancelBus_Disable);
        cancelButtonDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Help.this, Cancel_Bus_Successfully.class);
                Bundle bundle = new Bundle();
                bundle.putString("BusStopLocationId",BusStopLocationId);
                bundle.putInt("BusBell",BusBell);
                bundle.putInt("RouteId", RouteId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        BusAsyncTask task03 = new BusAsyncTask();
        task03.execute(USGS_REQUEST_URL_BUS_BELL);
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

            urls[0] = urls[0]+ routeId+'/'+stopLocationId + '/' + busBell;
            Log.i(LOG_TAG, urls[0]);
            //Log.i(LOG_TAG, urls[1]);
            QueryUtils_Transmit_BusBell.fetchBusInfo(urls[0]);

            return null;
        }

        @Override
        protected  void onPostExecute(List<BusStopOnTheRouteParameter> data){
            if (data != null && !data.isEmpty()){

            }
        }
    }
}
