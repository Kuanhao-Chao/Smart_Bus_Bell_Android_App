package com.example.android.map;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.map.AllBusStopInfo.BusStopParameter;
import com.example.android.map.AllBusStopInfo.SetJSONToArrayList;
import com.example.android.map.BusList.CallTheBus_directly;
import com.example.android.map.Cluster.Cluster;
import com.example.android.map.GeoIntentService.Constants;
import com.example.android.map.GeoIntentService.GeoFenceErrorMessage;
import com.example.android.map.RouteDesign.SearchResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Map;

import static com.example.android.map.GeoIntentService.Constants.BAY_AREA_LANDMARKS;
import static com.example.android.map.R.id.IDSend;
import static com.example.android.map.R.id.map;
import static com.google.android.gms.location.Geofence.NEVER_EXPIRE;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status> {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    //private final String LOG_TAG = "CurrentLocation";
    private TextView txtOutput;
    private  GoogleApiClient mGoogleApiClient;       //the entry point to Google Play Service. Used the Place API and the Fused Location Provider
    private  LocationRequest mLocationRequest;       //&&&&not know yet
    private Location mLastLocation;                 //the last-know location retrieved by FusedLocation provider

    //key to store the activity state
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    /////////////////////////////////////////////////////////////////////for the map

    private static String shareBusStopLocationId;
    public static double mcurrentLatitude;
    public static double mcurrentLongitude;
    private LatLng mCurrentLoation_location;
    /*private MarkerOptions mBusStop1_marker;
    private MarkerOptions mBusStop2_marker;
    private MarkerOptions mBusStop3_marker;
    private MarkerOptions mBusStop4_marker;*/

    /*private LatLng mBusStop1_location= new LatLng(25.0382, 121.5300);
    private LatLng mBusStop2_location= new LatLng(25.0372, 121.5300);
    private LatLng mBusStop3_location= new LatLng(25.0402, 121.5300);
    private LatLng mBusStop4_location= new LatLng(25.0412, 121.5300);*/
    ////////////////////////////////////////////////////////////////////test information for the bus

    private static final LatLng mDefaultLocation = new LatLng(25.069895471987962, 121.52026891708374);// a default location if the location permission is not allowed
    private static final int DEFAULT_ZOOM = 16;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    static final CameraPosition TAIPEI = CameraPosition.builder()
            .target(mDefaultLocation)
            .zoom(DEFAULT_ZOOM)
            .bearing(0)
            .tilt(0)
            .build();
    /////////////////////////////////////////////////////////////////for the map initial position
    private static final String LOG_TAG = MapsActivity.class.getSimpleName();
    /////////////////////////////////////////////////////////////////

    private SetJSONToArrayList setJSONToArrayList = new SetJSONToArrayList();
    private static ArrayList<BusStopParameter> allBusStop = new ArrayList<BusStopParameter>();
    private static int theNumberOfTheBusStops = allBusStop.size();
    //private MarkerOptions[] mBusStop_marker = new MarkerOptions[theNumberOfTheBusStops];    //to set the list of the markerOption
    //private MarkerOptions[] getmBusStop_marker = new MarkerOptions[theNumberOfTheBusStops];
    private  static LatLng[] mBusStoplatLngs = new LatLng[theNumberOfTheBusStops];          //to set the list of the LatLng of the bus stop
    private  static String[] mBusStopName = new String[theNumberOfTheBusStops];
    private static int[] mBusmStopLocationId = new int[theNumberOfTheBusStops];

    //geofence list to get the constant BAY_AREA_LANDMARKS
    private static ArrayList<Geofence> mGeofenceList = new ArrayList<Geofence>();

    public static boolean buttonChecker = false;

    private static String answerYouEnter= new String();
    public static String UserInput = new String();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //retrieve location and camera position from saved instance state
        if (savedInstanceState != null) {
            mLastLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        setContentView(R.layout.activity_maps);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
//        txtOutput = findViewById(R.id.txtOutput);
        allBusStop = SetJSONToArrayList.extractJsonfrom(MapsActivity.this);
        //above are for the map

        //to let all the bus stop get the LatLng
        /*mBusStop1_marker = new MarkerOptions().position(mBusStop1_location).title("Bus stop 1");//.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
        mBusStop2_marker = new MarkerOptions().position(mBusStop2_location).title("Bus stop 2");//.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
        mBusStop3_marker = new MarkerOptions().position(mBusStop3_location).title("Bus stop 3");//.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
        mBusStop4_marker = new MarkerOptions().position(mBusStop4_location).title("Bus stop 4");//.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));*/

//        TextView callTheBus = findViewById(R.id.IDcallTheBus_button);
//        callTheBus.setOnClickListener((new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent callTheBus = new Intent();
//                callTheBus.setClass(MapsActivity.this, CallTheBus_directly.class);
//                startActivity(callTheBus);
//            }
//        }));
//        callTheBus.setClickable(buttonChecker);
        //the onclickeListener for the call the bus right away button


        //the button to determine the Route design
        Button button = findViewById(IDSend);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = findViewById( R.id.IDSearch);
                UserInput = editText.getText().toString();
                if(UserInput.equals("") ){
                    Toast.makeText(MapsActivity.this, "請再輸入一次", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent callTheBus = new Intent(MapsActivity.this, SearchResult.class);
                    callTheBus.putExtra("input", UserInput);
                    startActivity(callTheBus);
                }
            }
        });
    }


    //to save the map state when the map is paused
    @Override
    protected void onSaveInstanceState(Bundle outState){
        if (mMap != null){
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        theNumberOfTheBusStops = allBusStop.size();
        mBusStoplatLngs = new LatLng[theNumberOfTheBusStops];
        mBusStopName = new String[theNumberOfTheBusStops];
        mBusmStopLocationId = new int[theNumberOfTheBusStops];
        for ( int i=0; i < theNumberOfTheBusStops; i++){
            mBusStopName[i] = allBusStop.get(i).getmBusStopName();
            mBusStoplatLngs[i] = allBusStop.get(i).getmLatLng();
            mBusmStopLocationId[i] = allBusStop.get(i).getmStopLocationId();
            if (mBusmStopLocationId[i] == 1462 ){
                Log.i(LOG_TAG,String.valueOf(i)+"   ASDFGHJ");
            }
            //Log.i(LOG_TAG, String.valueOf(mBusmStopLocationId[i]));
        }
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
        allBusStop = SetJSONToArrayList.extractJsonfrom(MapsActivity.this);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(1000);  // the unit is millisecond
        mLocationRequest.setFastestInterval(200);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }//check if the prmission is right
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.current_place_menu, menu);
//        return true;
//    }

    /**
     * Handles a click on the menu option to get a place.
     * @param //item The menu item to handle.
     * @return Boolean.
     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        if(item.getItemId() == R.id.option_get_place){
//            showCurrentPlace();
//        }
//        return true;
//    }

    @Override
    public void onLocationChanged(Location location){
        Log.i(LOG_TAG, location.toString());
//        txtOutput.setText(Double.toString(location.getLatitude()));
        mcurrentLatitude = location.getLatitude();     //to show the latitude of the current location
        mcurrentLongitude = location.getLongitude();   //to show the longitude of the current location
        mCurrentLoation_location = new LatLng(mcurrentLatitude, mcurrentLongitude);

        for ( int i=0; i< theNumberOfTheBusStops; i++){

        }

    }


    //public  double getMcurrentLatitude() { return  mcurrentLatitude; }
    //public  double getMcurrentLongitude() { return mcurrentLongitude; }
    //to get the latitude and the longitude


    @Override
    public void onConnectionSuspended(int i){
        Log.i(LOG_TAG, "GoogleApiClient connection has been suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "GoogleAPI connection has failed");
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
        /*mMap.addMarker(mBusStop1_marker);
        mMap.addCircle(new CircleOptions().center(mBusStop1_location).radius(50).strokeColor(Color.GREEN).fillColor(Color.argb(64, 0, 255,0)));
        mMap.addMarker(mBusStop2_marker);
        mMap.addCircle(new CircleOptions().center(mBusStop2_location).radius(50).strokeColor(Color.GREEN).fillColor(Color.argb(64, 0, 255,0)));
        mMap.addMarker(mBusStop3_marker);
        mMap.addCircle(new CircleOptions().center(mBusStop3_location).radius(50).strokeColor(Color.GREEN).fillColor(Color.argb(64, 0, 255,0)));
        mMap.addMarker(mBusStop4_marker);
        mMap.addCircle(new CircleOptions().center(mBusStop4_location).radius(50).strokeColor(Color.GREEN).fillColor(Color.argb(64, 0, 255,0)));*/

        setUpClusterer();
//        for (int i=0; i< 50; i++){
//        //    drawMarker( mBusStoplatLngs[i], mBusStopName[i], mBusmStopLocationId[i]);
////            mMap.addCircle(new CircleOptions().center(mBusStoplatLngs[i]).radius(600).strokeColor(Color.GREEN).fillColor(Color.argb(64, 0, 255, 0)));
//            Log.i(LOG_TAG, String.valueOf(mBusmStopLocationId[i]));
//        }
      //  LatLng NTUlatlng = new LatLng(25.069895471987962, 121.52026891708374);
        MarkerOptions NTUOption = new MarkerOptions();
        Marker NTUMarker = mMap.addMarker(NTUOption.position(mBusStoplatLngs[1135]).title(mBusStopName[1135]));
        mMap.addCircle(new CircleOptions().center(mBusStoplatLngs[1135]).radius(150).strokeColor(Color.RED).fillColor(Color.argb(64, 0, 255, 0)));
        populateGeofenceList();
        addGeofenceButtonHandler();

        //onResult();
        //flyTo(TAIPEI);
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();
        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    /*private void drawMarker (LatLng latLng, String busStopName,  int busStopLocationId ){
        /////////////set tag on to the map

        MarkerOptions markerOptions = new MarkerOptions();
        Marker marker = mMap.addMarker(markerOptions.position(latLng).title(busStopName));
        marker.setTag(busStopLocationId);
        mMap.addCircle(new CircleOptions().center(latLng).radius(20).strokeColor(Color.GREEN).fillColor(Color.argb(64, 0, 255, 0)));
        //mMap.addCircle(new CircleOptions().center(latLng).radius(100).strokeColor(Color.RED).fillColor(Color.argb(64, 0, 255, 0)));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                shareBusStopLocationId = (Integer)marker.getTag();
                Intent busList = new Intent();
                busList.setClass(MapsActivity.this, CallTheBus_directly.class);
                startActivity(busList);
                return false;
            }
        });
    }*/

    private void flyTo(CameraPosition target){
        //mMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(target), 20000, null);
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
            Log.d(TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }
    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        //updateLocationUI();
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

    public void populateGeofenceList(){
        for (Map.Entry<String, LatLng> entry : BAY_AREA_LANDMARKS.entrySet()){
            mGeofenceList.add(new Geofence.Builder()
            .setRequestId(entry.getKey()).setCircularRegion(entry.getValue().latitude, entry.getValue().longitude,Constants.GEOFENCE_RADIUS_IN_METERS)
            .setTransitionTypes(Constants.GEOFENCE_TRANSITION_ENTER |Constants.GEOFENCE_TRANSITION_EXIT)
            .setExpirationDuration(NEVER_EXPIRE)
            .build());
        }
    }

    private GeofencingRequest getfencingRequest(){
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        //the INITIAL_TRIGGER_ENTER indicates that geofencing service should trigger a
        //GEOFENCE_TRANSITION_ENTER notification when the geofence is added and the device is already in the geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return  builder.build();
    }

    private PendingIntent getGeofencePendingIntent(){
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void onResult(Status status){
        if (status.isSuccess()){
            Toast.makeText(this, "Geofence Added", Toast.LENGTH_SHORT ).show();
        }else{
            String errorMessage = GeoFenceErrorMessage.getErrorString(this, status.getStatusCode());
            Log.e(TAG, errorMessage);
        }

    }

    public void addGeofenceButtonHandler(){
        if (!mGoogleApiClient.isConnected()){
            Toast.makeText(this, "Not Connected", Toast.LENGTH_LONG).show();
            return;
        }
        try{
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    //the GeofenceRequest object
                    getfencingRequest(), getGeofencePendingIntent()
            ).setResultCallback(this);  //unsure
        }catch (SecurityException securityException){
            longSecurityException(securityException);
        }
    }

    private void longSecurityException(SecurityException securityException){
        Log.e(TAG, "Invalid location permission", securityException);
    }

    //to let hte Constants.java get the parameter
    public static int getTheNumberOfTheBusStops(){ return theNumberOfTheBusStops;}
    public static LatLng[] getmBusStoplatLngs() { return mBusStoplatLngs;}
    public static String[] getmBusStopName() { return mBusStopName;}
//    public static String getmcurrentLatitude(){return String.valueOf(mcurrentLatitude);}
//    public static String getmcurrentLocation() {return String.valueOf(mcurrentLongitude);}
    public static ArrayList<BusStopParameter> getAllBusStop() { return allBusStop;}
    public static String getanswerYouEnter() {return answerYouEnter;}
    public static int[] getmBusmStopLocationId() { return getmBusmStopLocationId();}

    //cluster the marker
    // Declare a variable for the cluster manager.
    private ClusterManager<Cluster> mClusterManager;

    private void setUpClusterer() {

        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.048347, 121.5272), 5));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<Cluster>(this, mMap);

        // Add cluster items (markers) to the cluster manager.
        addItems();

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterItemClickListener(mClusterItemClickListener);

    }
    public ClusterManager.OnClusterItemClickListener<Cluster> mClusterItemClickListener
            =
            new ClusterManager.OnClusterItemClickListener<Cluster>() {
        @Override
        public boolean onClusterItemClick(Cluster item) {

            shareBusStopLocationId = item.getTag();

            Intent busList = new Intent();
            busList.setClass(MapsActivity.this, CallTheBus_directly.class);
            busList.putExtra("shareBusStopLocationId", shareBusStopLocationId);
            startActivity(busList);
            return false;
        }
    };

    private void addItems() {
        // Add ten cluster items in close proximity, for purposes of this example.
        //for (HashMap.Entry<String, BusStopParameter> busStopParameterEntry : SET_ARRTIBUTE_TO_MARKER.entrySet()) {
        //    MarkerOptions markerOptions = new MarkerOptions().position()

        for (int i=0; i<theNumberOfTheBusStops; i++){
        Cluster offsetItem = new Cluster(mBusStoplatLngs[i],mBusStopName[i], String.valueOf(mBusmStopLocationId[i]));
            mClusterManager.addItem(offsetItem);
        }
    }
    ///////////////////////////////////////////////////////the method to handle the input
//    @Override
//    public View onCreateInputView(){
//        KeyboardView inputView = (KeyboardView) getLayoutInflater().inflate(R.layout.activity_maps, null);
//        inputView.setOnKeyboardActionListener(this);
//        inputView.setKeyboard();
//        return
//    }

}



//    private void showCurrentPlace() {
//        if (mMap == null) {
//            return;
//        }
//
//        if (mLocationPermissionGranted) {
//            // Get the likely places - that is, the businesses and other points of interest that
//            // are the best match for the device's current location.
//            @SuppressWarnings("MissingPermission")
//            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
//                    .getCurrentPlace(mGoogleApiClient, null);
//            result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
//                @Override
//                public void onResult(@NonNull PlaceLikelihoodBuffer likelyPlaces) {
//                    int i = 0;
//                    mLikelyPlaceNames = new String[mMaxEntries];
//                    mLikelyPlaceAddresses = new String[mMaxEntries];
//                    mLikelyPlaceAttributions = new String[mMaxEntries];
//                    mLikelyPlaceLatLngs = new LatLng[mMaxEntries];
//                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
//                        // Build a list of likely places to show the user. Max 5.
//                        mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
//                        mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace().getAddress();
//                        mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
//                                .getAttributions();
//                        mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();
//
//                        i++;
//                        if (i > (mMaxEntries - 1)) {
//                            break;
//                        }
//                    }
//                    // Release the place likelihood buffer, to avoid memory leaks.
//                    likelyPlaces.release();
//
//                    // Show a dialog offering the user the list of likely places, and add a
//                    // marker at the selected place.
//                    openPlacesDialog();
//                }
//            });
//        } else {
//            // Add a default marker, because the user hasn't selected a place.
//            mMap.addMarker(new MarkerOptions()
//                    .title(getString(R.string.default_info_title))
//                    .position(mDefaultLocation)
//                    .snippet(getString(R.string.default_info_snippet)));
//        }
//    }
//
//    private void openPlacesDialog() {
//        // Ask the user to choose the place where they are now.
//        DialogInterface.OnClickListener listener =
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // The "which" argument contains the position of the selected item.
//                        LatLng markerLatLng = mLikelyPlaceLatLngs[which];
//                        String markerSnippet = mLikelyPlaceAddresses[which];
//                        if (mLikelyPlaceAttributions[which] != null) {
//                            markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
//                        }
//                        // Add a marker for the selected place, with an info window
//                        // showing information about that place.
//                        mMap.addMarker(new MarkerOptions()
//                                .title(mLikelyPlaceNames[which])
//                                .position(markerLatLng)
//                                .snippet(markerSnippet));
//
//                        // Position the map's camera at the location of the marker.
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
//                                DEFAULT_ZOOM));
//                    }
//                };
//
//        // Display the dialog.
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle(R.string.pick_place)
//                .setItems(mLikelyPlaceNames, listener)
//                .show();
//    }
//
//    /**
//     * Updates the map's UI settings based on whether the user has granted location permission.
//     */
//}
