package com.example.android.map.Buttons;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
//import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.android.map.R;
import com.example.android.map.Success_Screen_Cancel_Screen.Help;
import com.example.android.map.Success_Screen_Cancel_Screen.NormalCall;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.android.map.MapsActivity.mcurrentLatitude;
import static com.example.android.map.MapsActivity.mcurrentLongitude;
import static com.example.android.map.R.id.map;

/**
 * Created by Howard on 2017/7/21.
 */

public class ClickButtonsToCallBus extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status> {

    private  GoogleApiClient mGoogleApiClient;
    private  LocationRequest mLocationRequest;
    private GoogleMap mMap;
    private static final String LOG_TAG = ClickButtonsToCallBus.class.getSimpleName();

    private LatLng mCurrentLoation_location;
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Location mLastLocation;
    private CameraPosition mCameraPosition;
    private static final int DEFAULT_ZOOM = 12;
    private static final LatLng mDefaultLocation = new LatLng(25.0392,121.5300);
    private static final String SECOND_URL_REQUEST = "http://192.168.0.110:5000/howard/";
    //private List<String> data = new ArrayList<>();

    private int theNumberOfTheStopInRoute;
    private  LatLng[] mBusStoplatLngs;
    private String [] mBusStopName;

    private static String busNumber = "";
    private static String BusStopLocationId = "";

    private static int BusBell = 0;
    private static int RouteId = 0;
    List<BusStopOnTheRouteParameter> result= new ArrayList<BusStopOnTheRouteParameter>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.click_button_to_call_bus);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        Bundle bundle = getIntent().getExtras();
        busNumber = bundle.getString("busNumber");
        BusStopLocationId = bundle.getString("BusStopLocationId");


        final ImageView normalCall = findViewById(R.id.normalCall);
        normalCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusBell = 1;
                RouteId = result.get(0).getmBusId();
                Intent intent = new Intent();
                intent.setClass(ClickButtonsToCallBus.this, NormalCall.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("BusStopLocationId",BusStopLocationId);
                bundle1.putInt("BusBell", BusBell);
                bundle1.putInt("RouteId", RouteId);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
        ImageView disable = findViewById(R.id.help);
        disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusBell = 2;
                RouteId = result.get(0).getmBusId();
                Intent intent = new Intent();
                intent.setClass(ClickButtonsToCallBus.this, Help.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("BusStopLocationId",BusStopLocationId);
                bundle2.putInt("BusBell", BusBell);
                bundle2.putInt("RouteId", RouteId);
                intent.putExtras(bundle2);
                startActivity(intent);
            }
        });

        BusAsyncTask task02 = new BusAsyncTask();
        task02.execute(SECOND_URL_REQUEST);
    }
    @Override
    protected void onStart(){
        super.onStart();
        //connect to the client
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause(){
        super.onPause();
        //stop the location update when Activity is no longer active
        if (mGoogleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    protected void onStop(){
        //disconnect the client
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);
        //Build the map and then call the onMapReady() automatically
        //allBusStop = setJSONToArrayList.extractJsonfrom(MapsActivity.this);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(1000);  // the unit is millisecond
        mLocationRequest.setFastestInterval(200);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }//check if the prmission is right
    }

    @Override
    public void onConnectionSuspended(int i){
        Log.i(LOG_TAG, "GoogleApiClient connection has been suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "GoogleAPI connection has failed");
    }

    @Override
    public void onLocationChanged(Location location){
        Log.i(LOG_TAG, location.toString());
        mcurrentLatitude = location.getLatitude();     //to show the latitude of the current location
        mcurrentLongitude = location.getLongitude();   //to show the longitude of the current location
        mCurrentLoation_location = new LatLng(mcurrentLatitude, mcurrentLongitude);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //while( theNumberOfTheStopInRoute == 0){}
        //set the sleep time to wait
        Integer var = 4;
        try {
            TimeUnit.SECONDS.sleep(var.longValue());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        theNumberOfTheStopInRoute = result.size();
        mBusStoplatLngs = new LatLng[theNumberOfTheStopInRoute];
        mBusStopName = new String[theNumberOfTheStopInRoute];

        for (int i = 0; i < theNumberOfTheStopInRoute; i++) {
            mBusStopName[i] = result.get(i).getmNameZh();
            mBusStoplatLngs[i] = result.get(i).getLatlng();
        }

        //LatLng NTUlatlng = new LatLng(25.006231, 121.426399);
        //MarkerOptions NTUOption = new MarkerOptions();
//        Marker NTUMarker = mMap.addMarker(NTUOption.position(NTUlatlng).title("NTUStation"));
//        mMap.addCircle(new CircleOptions().center(NTUlatlng).radius(30).strokeColor(Color.RED).fillColor(Color.argb(64, 0, 255, 0)));
        MarkerOptions markerOptions = new MarkerOptions();
        int a = 0;
        if (theNumberOfTheStopInRoute%2 ==1){
            a = (theNumberOfTheStopInRoute-1)/2;
        }
        if (theNumberOfTheStopInRoute%2 == 0){
            a = theNumberOfTheStopInRoute/2 - 1;
        }
        Marker marker = mMap.addMarker(markerOptions.position(mBusStoplatLngs[a-2]).title(mBusStopName[a-2]));
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.start));

        for (int i = 1; i < theNumberOfTheStopInRoute; i++) {
            drawMarker(mBusStoplatLngs[i], mBusStopName[i]);
        }
        MarkerOptions markerOptionsEnd = new MarkerOptions();
        Marker marker2 = mMap.addMarker(markerOptionsEnd.position(mBusStoplatLngs[theNumberOfTheStopInRoute-1]).title(mBusStopName[theNumberOfTheStopInRoute-1]));
        marker2.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.end));

        //LatLng NTUlatlng = new LatLng(25.006231, 121.426399 );
        //MarkerOptions NTUOption = new MarkerOptions();
        //Marker NTUMarker = mMap.addMarker(NTUOption.position(NTUlatlng).title("NTUStation"));
        //mMap.addCircle(new CircleOptions().center(NTUlatlng).radius(30).strokeColor(Color.RED).fillColor(Color.argb(64, 0, 255, 0)));


        //onResult();
        //flyTo(TAIPEI);
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();
        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    /*@Override
    public void onMapLoaded(){
        theNumberOfTheStopInRoute = result.size();
        mBusStoplatLngs = new LatLng [theNumberOfTheStopInRoute];
        mBusStopName = new String[theNumberOfTheStopInRoute];

        for ( int i=0; i < theNumberOfTheStopInRoute; i++){

            mBusStopName[i] = result.get(i).getmNameZh();
            mBusStoplatLngs[i] = result.get(i).getLatlng();
        }

        LatLng NTUlatlng = new LatLng(25.006231, 121.426399 );
        MarkerOptions NTUOption = new MarkerOptions();
        Marker NTUMarker = mMap.addMarker(NTUOption.position(NTUlatlng).title("NTUStation"));
        mMap.addCircle(new CircleOptions().center(NTUlatlng).radius(30).strokeColor(Color.RED).fillColor(Color.argb(64, 0, 255, 0)));

        for (int i=0; i< theNumberOfTheStopInRoute; i++){
            drawMarker( mBusStoplatLngs[i], mBusStopName[i]);
        }
    }*/

    private void drawMarker (LatLng latLng, String busStopName ){
        /////////////set tag on to the map

        MarkerOptions markerOptions = new MarkerOptions();
        Marker marker = mMap.addMarker(markerOptions.position(latLng).title(busStopName));
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.middle));
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }

        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastLocation = null;
        }
    }

    private void getDeviceLocation() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        if (mLocationPermissionGranted) {
            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }

        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastLocation.getLatitude(),
                            mLastLocation.getLongitude()), DEFAULT_ZOOM));
        } else {
            Log.d(LOG_TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }


    @Override
    public void onResult(@NonNull Status status) {
    }



    private class BusAsyncTask extends AsyncTask<String, Void, List<BusStopOnTheRouteParameter>> {

        @Override
        protected List<BusStopOnTheRouteParameter> doInBackground (String... urls){
            if (urls.length < 1 || urls[0] == null ){
                return null;
            }
            Log.i(LOG_TAG,busNumber);
            urls[0] = urls[0]+ busNumber;
            Log.i(LOG_TAG, urls[0]);
            //Log.i(LOG_TAG, urls[1]);
            result = QueryUtils_BusStopOnTheRoute.fetchBusInfo(urls[0]);

            return result;
        }

        @Override
        protected  void onPostExecute(List<BusStopOnTheRouteParameter> data){
            if (data != null && !data.isEmpty()){

            }
        }
    }

}
