package com.app.rum_a.ui.postauth.service;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.app.rum_a.utils.Log4Android;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


/**
 * Created by tarun on 25/6/15.
 */
public class LocationTracker implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Context context;
    private GoogleApiClient mGoogleApiClient;
    private static int UPDATE_INTERVAL = 0;
    private static int FATEST_INTERVAL = 0;
    private static int DISPLACEMENT = 2000; // 2KM
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private onLocationListener listener;

    public LocationTracker(Context context) {
        this.context = context;
        if (LocationUtils.getInstance(context).isGooglePlayServicesAvailable()) {
            buildGoogleApiClient();
            createLocationRequest();
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        }
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        displayLocation();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log4Android.e(this, connectionResult.toString());
    }

    /**
     * Creating location request object
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Method to display the location on UI
     */
    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            Log4Android.e(this, latitude + "," + longitude);
            listener.onConnected(mLastLocation);
        }
    }

    /**
     * Starting the location updates
     */
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, locationListener);
        Log4Android.e(this, "startLocationUpdates");
    }

    /**
     * Stopping location updates
     */
    public void stopLocationUpdates() {
        try {
            LocationServices.FusedLocationApi
                    .removeLocationUpdates(mGoogleApiClient, locationListener);
            Log4Android.e(this, "stopLocationUpdates");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLastLocation = location;
            displayLocation();
        }
    };

    public void setLocationListener(onLocationListener listener) {
        this.listener = listener;
    }

    public interface onLocationListener {
        void onConnected(Location location);
    }
}
