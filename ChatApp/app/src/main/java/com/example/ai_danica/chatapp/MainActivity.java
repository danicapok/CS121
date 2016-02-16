package com.example.ai_danica.chatapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    SharedPreferences pref;
    private String user_id;
    public static String LOG_TAG = "My log tag";
   // public final static String Cuisine = "com.mycompany.myfirstapp.CuisineSearch";

    private LocationData locationData = LocationData.getLocationData();//store location to share between activities

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = settings.getString("user_id", null);
        if (user_id == null) {
            // Creates a random one, and sets it.
            SecureRandomString srs = new SecureRandomString();
            user_id = srs.nextString();
            SharedPreferences.Editor e = settings.edit();
            e.putString("user_id", user_id);
            e.commit();

        }

       // setNickname();

    }

    @Override
    public void onResume(){

        Log.i(LOG_TAG, "Inside resume of main activity");
        //setNickname();
        //check if user already gave permission to use location
        Boolean locationAllowed = checkLocationAllowed();

        Button searchButton = (Button) findViewById(R.id.startChatBtn);

        //if(locationAllowed){
            requestLocationUpdate();
        //}else
          //  searchButton.setEnabled(false);//search button must not be enabled until we have a location

        //Set the button text between "Enable Location" or "Disable Location"
        render();
        System.out.println("testing inside onResume");
        super.onResume();
    }

    /*
    Check users location sharing setting
     */
    private boolean checkLocationAllowed(){
        Log.i(LOG_TAG, "Inside check location allowed");
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        return settings.getBoolean("location_allowed", false);
    }

    /*
    Persist users location sharing setting
     */
    private void setLocationAllowed(boolean allowed){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("location_allowed", allowed);
        editor.commit();
    }

    /*
    Set the button text between "Enable Location" or "Disable Location"
     */
    private void render(){
        Boolean locationAllowed = checkLocationAllowed();
        Button button = (Button) findViewById(R.id.startChatBtn);

        if(locationAllowed) {
            button.setEnabled(false);
        }
        else {
            button.setEnabled(true);
        }
    }

    /*
    Request location update. This must be called in onResume if the user has allowed location sharing
     */
    private void requestLocationUpdate(){
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Log.i(LOG_TAG, "requesting location update");
            }
        }
    }

    /*
    Remove location update. This must be called in onPause if the user has allowed location sharing
     */
    private void removeLocationUpdate() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {

                locationManager.removeUpdates(locationListener);
                Log.i(LOG_TAG, "removing location update");
            }
        }

    }


    @Override
    public void onPause(){
        if(checkLocationAllowed())
            removeLocationUpdate();//if the user has allowed location sharing we must disable location updates now
        super.onPause();
    }

    /**
     * Listens to the location, and gets the most precise recent location.
     * Copied from Prof. Luca class code
     */
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            Location lastLocation = locationData.getLocation();

            // Do something with the location you receive.
            double newAccuracy = location.getAccuracy();

            long newTime = location.getTime();
            // Is this better than what we had?  We allow a bit of degradation in time.
            boolean isBetter = ((lastLocation == null) ||
                    newAccuracy < lastLocation.getAccuracy() + (newTime - lastLocation.getTime()));
            if (newAccuracy < 50) {
                // We replace the old estimate by this one.
                locationData.setLocation(location);

                //Now we have the location.
                Button searchButton = (Button) findViewById(R.id.startChatBtn);
                if(checkLocationAllowed())
                    searchButton.setEnabled(true);//We must enable search button
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };

    private void setNickname(){
        EditText nickname = (EditText) findViewById(R.id.nickname);
        String nick = nickname.toString();
        pref = getSharedPreferences("nickname", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("nickname", nick);
        editor.commit();
        //startChat.setEnabled(true);
    }

    public void clickedStartChat(View v){

        Intent intent = new Intent(this, ChatActivity.class);
        EditText editText = (EditText) findViewById(R.id.nickname);

        intent.putExtra("nickname", editText.getText().toString());
        startActivity(intent);//pass the cuisine to the search activity for searching
    }
}

