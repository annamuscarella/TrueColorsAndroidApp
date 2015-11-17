package com.example.pinkprincess.meetmetest2;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

public class MapsActivity extends FragmentActivity implements LocationProvider.LocationCallback, OnMarkerClickListener, OnMapReadyCallback, HttpResponseInterface {

    public static final String TAG = MapsActivity.class.getSimpleName();

    HttpRequestInterface httpRequests = new HttpRequestSender();
    HttpRequestInterface offlineRequest = new OfflineTester();

    public static final boolean connectionToServer = true; //HIER ANGEBEN, ob Server connected ist oder nicht!!

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationProvider mLocationProvider; //class is used to get user's current location

    public Context context = this;
    Boolean handle_new_location_free = true;

    private Marker lastMarkerClicked;

    Thread thread;


    private ArrayList<Marker> markers = new ArrayList();

    private final float MAX_DISTANCE = 2000;

    private Button submit; //submit entry in editText field (codeEingabe)
    private Button cancel; //hide codeEingabe
    private EditText codeEingabe; //Text Entry Field for entring the other user's code
    private String otherUserCode; //String as first character might be null: int --> 886; string --> "0886"
    private String userCode; //String for storing own code locally (recieved from server at login later)
    private LinearLayout eingabefeld; //windows that appears if user is clicked and users might have met
    public static AssetManager assetManager;
    Handler mHandler;
    static Timestamp old;
    static Timestamp current;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_radar);
        if (OwnUser.loggedIn==false) {
            startActivity(new Intent(MapsActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            this.finish();
        }
        else {
            setUpMapIfNeeded();

            eingabefeld = (LinearLayout) findViewById(R.id.CodeEingabefeld);
            submit = (Button) findViewById(R.id.submit_button);
            submit.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              codeEingabe = (EditText) findViewById(R.id.editText);
                                              if (codeEingabe == null){
                                                  Log.d(TAG, "eingabe is null");
                                              }
                                              else {
                                                  otherUserCode = codeEingabe.getText().toString();
                                                  httpRequests.doGetUserMeeting(context, lastMarkerClicked.getTitle(), otherUserCode);
                                                  eingabefeld.setVisibility(View.GONE);
                                                  codeEingabe.setText("");
                                              }
                                          }
                                      }
        /*If submit button in eingabefeld is clicked, user's entry in codeEingabe will be stored in otherUserCode as String
          eingabefeld is hidden again */
            );

            cancel = (Button) findViewById(R.id.cancel_button);
            cancel.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              eingabefeld.setVisibility(View.GONE);
                                              if (codeEingabe == null){
                                                  Log.d(TAG, "eingabe is null");
                                              }
                                              else{codeEingabe.setText("");}
                                          }
                                      }
                    //If cancel Button in eingabefeld is clicked, eingabefeld is hidden again
            );





            mLocationProvider = new LocationProvider(this, this);

            Calendar cal = Calendar.getInstance();
            java.util.Date now = cal.getTime();
            old = new Timestamp(now.getTime());

            thread = new Thread() {
                @Override
                public void run() {
                    while (true){
                        Calendar cal = Calendar.getInstance();
                        java.util.Date now = cal.getTime();
                        current = new Timestamp(now.getTime());
                        long diff = current.getTime() - old.getTime();
                        if (diff > 500 && handle_new_location_free){  Message login_success_message = mHandler.obtainMessage(0);
                            login_success_message.sendToTarget();
                            try {
                                sleep(9000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                        else {
                            try {
                                sleep(9000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            };
            thread.start();
            mHandler = new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(android.os.Message inputMessage){
                    switch (inputMessage.what){
                        case 0:
                            Location location_handler = new Location("");
                            location_handler.setLatitude(OwnUser.ownLocation.latitude);
                            location_handler.setLongitude(OwnUser.ownLocation.longitude);
                            handleNewLocation(location_handler);
                            break;
                    /*case 1:
                        waiting_dialog.dismiss();
                        OwnUser.loggedIn = true;
                        startActivity(new Intent(LoginActivity.this, MapsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                        LoginActivity.this.finish();
                        break;
                    default:
                        waiting_dialog.dismiss();
                        break;*/

                    }
                }
            };

        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_call:
                Intent dialer= new Intent(Intent.ACTION_DIAL);
                startActivity(dialer);
                return true;

            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Settings Clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_profile:
                startActivity(new Intent(MapsActivity.this, PersonalStatistics.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                return true;

            case R.id.action_score:
                startActivity(new Intent(MapsActivity.this, PersonalScore.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                return true;

            case R.id.action_ranking:
                startActivity(new Intent(MapsActivity.this, TeamRanking.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        InputStream stream = null;
        try {
            stream = getAssets().open("otherusers.json"); //open JSON document (later: HTTP response, not asset)

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mLocationProvider.connect();
    } //if screen is turned on, GoogleApiClient connects

    @Override
    protected void onPause() {
        super.onPause();
        mLocationProvider.disconnect();
    } //if screen is turned off, GoogleApiClient disconnects to save energy

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that map is not already initiated
        if (mMap == null) {

                mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                        .getMap();

            // getMap() is deprecated but working
            if (mMap != null) {
                Log.d(TAG, "Map is already set up");
            }
        }
    }




    public void handleNewLocation(Location location) {

        handle_new_location_free = false;
        /*
        method is called from LocationProvider.onConnected() (as soon as GoogleApiClient is connected successfully)
        and from LocationProvider.onLocationChanged() (as soon as user moved)
         */

        Log.d(TAG, location.toString());
        OwnUser.ownLocation = new LatLng(location.getLatitude(), location.getLongitude()); //save location as LatLng

        if (location == null) {Log.d(TAG, "Location is null!");return;}
        if (checkIfOwnLocationAlreadyDisplayed())
            {return;} //findOwnLocationMarker().remove();} //if current user location is already displayed, remove marker

        MarkerOptions options = new MarkerOptions()
                .position(OwnUser.ownLocation)
                .title("I am here!");
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(OwnUser.ownLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(OwnUser.ownLocation, 14.5f));

        //add new marker at current user position

        context = this;
        if (connectionToServer) //send http request only if connected to server
        {
            httpRequests.doGetOtherUsers(context);
        }

        else { //if not connected, use local JSON dokument (assets/otherusers.json)
            assetManager = getAssets();
            offlineRequest.doGetOtherUsers(context);
        }

    }


    private boolean checkIfOwnLocationAlreadyDisplayed() {
        boolean answer = false;
        answer = findOwnLocationMarker() != null;
        return answer;
    }

    private Marker findOwnLocationMarker() {
        Marker answer = null;
        for (int i = 0; i<markers.size(); i++) {
            if (markers.get(i).getTitle().equals("I am here!")){answer = markers.get(i);}
        }
        return answer;
    }

@Override
    public void displayOtherUser(ArrayList<OtherUser> userArray) {
    if (markers.isEmpty()) {
    OwnUser.nearestUserArray = userArray;
        for(int i = 0; i<userArray.size(); i++) {
                LatLng userLoc = userArray.get(i).loc;
                String userName = userArray.get(i).name;
                String userColor = userArray.get(i).color;
                String iconColor = userColor + ".gif"; //gif files are stored in the asset folder, iconColor = name of required gif file
            newMarker(userLoc, userName, iconColor);
          }
    }
    else {
        while(!markers.isEmpty()){
            int a = markers.size()-1;
            markers.get(a).remove();
            markers.remove(a);
        }
            displayOtherUser(OwnUser.nearestUserArray);
    }

    old = current;
    Toast.makeText(getApplicationContext(), "Map aktualisiert", Toast.LENGTH_SHORT).show();
    handle_new_location_free = true;
    }

    @Override
    public void userMeetingValidation(String otherUserName, Boolean userMeeting) {
        if (userMeeting) {
            displayOtherUser(OwnUser.nearestUserArray);}
        else{Log.d(TAG, "User nicht gefunden");}
    }

    @Override
    public void displayBestUserRanking(String[][] bestUserArray) {
        //not needed
    }

    @Override
    public void displayTeamRanking(String[][] teamRankingArray) {
        //not needed
    }

    @Override
    public void displayFriends(String[][] friendArray) {

    }

    @Override
    public void verificationCompleted(Boolean result) {

    }


    private void newMarker(LatLng location, final String title, String graphic) {
        MarkerOptions options = new MarkerOptions()
                .position(location) //other user's location
                .title(title) //other user's user ID/name
                .icon(BitmapDescriptorFactory.fromAsset(graphic)); //icon = gif file depending on user's color

        mMap.setOnMarkerClickListener(new OnMarkerClickListener() { //if marker is clicked, do the following:
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d(TAG, "Marker " + marker + "wurde angeklickt");
                lastMarkerClicked = marker;

                if (checkIfPossibleMeeting(marker)){ //check if distance between users is less than 2km and other user's color is green
                    eingabefeld.setVisibility(View.VISIBLE);} //if users might have met, display field for entering the other user's code
                else{marker.setTitle(""+marker.getPosition());} //if user meeting is not possible, show other user's position (only for testing)

                return false; //default return statement
            }
        });
        markers.add(mMap.addMarker(options)); //create new marker, display on map and add it to the markers array
    }



    public boolean checkIfPossibleMeeting(Marker marker){
        boolean answer = false;
        /*
        for comparing two positions, Google provides a method distanceTo(Location location)
        therefore, two Objects of type Location must be created:
        otherUserLocation stores the position of the other user
        myUserLocation stores the own user's last known position
         */
        Location otherUserLocation = new Location("");
        otherUserLocation.setLatitude(marker.getPosition().latitude);
        otherUserLocation.setLongitude(marker.getPosition().longitude);

        Location myUserLocation = new Location("");
        myUserLocation.setLatitude(OwnUser.ownLocation.latitude);
        myUserLocation.setLongitude(OwnUser.ownLocation.longitude);

        float distance = myUserLocation.distanceTo(otherUserLocation);

        if (distance < MAX_DISTANCE) {
            for (int a=0; a < OwnUser.nearestUserArray.size(); a++){
                if(OwnUser.nearestUserArray.get(a).name.equals(marker.getTitle())){
                    answer = OwnUser.nearestUserArray.get(a).color.equals("grey");
                }
            }
        }
        /*
        if distance between users is less than 2km, the users might have been able to meet each other. Find the OtherUser Object in the userArray's array that
        is equal to the marker that was clicked (OtherUser's name is equal to marker's title) and check, if matching OtherUser's color is grey.
        If OtherUser's color is grey and distance is less than 2km, the users have not met each other before and are therefore allowed to exchange codes.
         */
        else{answer = false;}
        return answer;
    }


    @Override
    public boolean onMarkerClick(Marker marker) { //method needs to be implemented if onMarkerClickListener interface is used
        Log.d(TAG, "Marker " + marker + "wurde angeklickt");
        marker.setTitle("angeklickt");
        return false;
    }

}

    /*
    protected void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
    ;
    }

    @Override
    protected synchronized void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        conected = mGoogleApiClient.isConnecting();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


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
    /*
    @Override
    public void onConnected(Bundle connectionHint) {
        location = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        Log.d("Anna", location.toString());
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        }
        else {handleNewLocation(location);}
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult r) {
        Log.d("Anna", r.toString());
    }

    public GoogleApiClient getClient() {return mGoogleApiClient;}



   @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        /*mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText = mLastLocation.getLatitude();
            mLongitudeText = mLastLocation.getLongitude();
        }*/

        // Add a marker in Sydney and move the camera
    /*
        location = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        mLatitudeText = 80.91;
        mLongitudeText = 80.91;
        LatLng current = new LatLng(mLatitudeText, mLongitudeText);
        mMap.addMarker(new MarkerOptions().position(current).title("Current Position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        mLatitudeText= location.getLatitude();
        mLongitudeText = location.getLongitude();
        LatLng latLng = new LatLng(mLatitudeText, mLongitudeText);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }
}*/
