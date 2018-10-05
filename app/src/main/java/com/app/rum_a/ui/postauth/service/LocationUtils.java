package com.app.rum_a.ui.postauth.service;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Harish on 10/3/15.
 */
public class LocationUtils {

    private static Context context;
    private static LocationUtils locationUtils;

    public static LocationUtils getInstance(Context context) {
        LocationUtils.context = context;
        if (locationUtils == null) {
            locationUtils = new LocationUtils();
        }
        return locationUtils;
    }

    public boolean isGooglePlayServicesAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (ConnectionResult.SUCCESS == resultCode) {
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            return false;
        }
    }

    // to get complete current address

    public String getAddress(double latitude,
                             double longitude) {
        String location = "";
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude,
                    longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String admin = address.getAdminArea();
                String subLocality = address.getSubLocality();
                String locality = address.getLocality();
                if (admin.length() > 10) {
                    admin = admin.substring(0, 10) + "..";
                }
                if (locality != null && subLocality != null) {
                    location = subLocality + "," + locality;
                } else if (subLocality != null) {
                    location = subLocality + "," + admin;
                } else {
                    location = locality + "," + admin;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return location;
    }

    public double CalculateDistance(LatLng src, LatLng des) {
        double distance;
        double finalValue = 0;
        try {
            Location locationA = new Location("");
            locationA.setLatitude(src.latitude);
            locationA.setLongitude(src.longitude);
            Location locationB = new Location("");
            locationB.setLatitude(des.latitude);
            locationB.setLongitude(des.longitude);
            distance = locationA.distanceTo(locationB) / 1000;
            finalValue = Math.round(distance * 100.0) / 100.0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return finalValue;
    }

    //convert kms to miles
    public double kmToMiles(String distance) {
        try {
            double dist = Double.parseDouble(distance);
            double miles = dist * 0.621371;
            return (double) Math.round(miles * 100) / 100;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public interface Constants {
        double Feet_300_in_miles = 0.0568182;
        double Miles_In_One_Meter = 0.000621371;
    }
}
