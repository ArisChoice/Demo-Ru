package com.app.rum_a.ui.postauth.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ResultReceiver;
import android.util.Log;

import com.app.rum_a.core.RumApplication;
import com.app.rum_a.di.DaggerValues;
import com.app.rum_a.model.modelutils.AddressResponse;
import com.app.rum_a.model.req.UpdateUserLocation;
import com.app.rum_a.model.resp.DefaultResponse;
import com.app.rum_a.model.resp.PropertyListResponseModel;
import com.app.rum_a.model.resp.UserResponseModel;
import com.app.rum_a.net.RestCallback;
import com.app.rum_a.net.RestProcess;
import com.app.rum_a.net.RestService;
import com.app.rum_a.ui.postauth.activity.ProfileActivity;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.Consts;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.Log4Android;
import com.app.rum_a.utils.PreferenceManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by Harish on 25/6/15.
 */
public class LocationService extends Service implements LocationTracker.onLocationListener, RestCallback {
    @Named(DaggerValues.AUTH)
    @Inject
    RestService apiInterface;
    @Inject
    PreferenceManager mPref;
    private LocationTracker locationTracker;
    private Location currentLocation;
    private String mAddressOutput;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((RumApplication) getApplication()).getMyComponent().inject(this);
        Log4Android.e(this, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationTracker = new LocationTracker(this);
        locationTracker.setLocationListener(this);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationTracker.stopLocationUpdates();
    }

    @Override
    public void onConnected(Location location) {
        Log.e("onConnected ", " : location : " + location.getLatitude() + " " + location);
        currentLocation = location;
        getAddressFromLatLong(location);
    }

    private void getAddressFromLatLong(Location location) {
        try {
            getAddressFromLocation(location, location.getLatitude(), location.getLongitude(), this, new GeocoderHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Call<AddressResponse> call = apiInterface.getAddress(makeUrl(location.getLatitude(), location.getLongitude()));
//        call.enqueue(new RestProcess<AddressResponse>(AppConstants.ServiceModes.SERVICE_MODE_GET_NEAR_BY_RESULT,
//                this, this, false));
    }

//    private AddressResultReceiver mResultReceiver;

   /* protected void startIntentService(Location location) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(AppConstants.LocationRequest.RECEIVER, mResultReceiver);
        intent.putExtra(AppConstants.LocationRequest.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }*/

    /*class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultData == null) {
                return;
            }

            // Display the address string
            // or an error message sent from the intent service.
            mAddressOutput = resultData.getString(AppConstants.LocationRequest.RESULT_DATA_KEY);
            if (mAddressOutput == null) {
                mAddressOutput = "";
            }
            Log.e("Location Address Loader", "          " + mAddressOutput);
//            // Show a toast message if an address was found.
//            if (resultCode == AppConstants.RequestCode.SUCCESS_RESULT) {
//                showToast(getString(R.string.address_found));
//            }

        }
    }*/

    public void getAddressFromLocation(final Location location, final double latitude, final double longitude, final Context context, final Handler handler) {

        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
//                startIntentService(location);

                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude,
                            longitude, 1);
                    Log.e(" getAddressFromLocation ", " " + new Gson().toJson(addresses));
                    try {
                        if (addresses.size() > 0) {
                            Address address = addresses.get(0);
                            String admin = address.getAdminArea();
                            if (admin == null)
                                admin = address.getSubAdminArea();
                            String country = address.getCountryName();
                            result = admin + "," + country;
                            /*String subLocality = address.getSubLocality();
                            String locality = address.getLocality();
                            try {
                                if (admin.length() > 10) {
                                    admin = admin.substring(0, 10) + "..";
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                admin = address.getAdminArea();
                            }
                            if (locality != null && subLocality != null) {
                                result = subLocality + "," + locality;
                            } else if (subLocality != null) {
                                result = subLocality + "," + admin;
                            } else {
                                result = locality + "," + admin;
                            }*/
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                   /* List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getFeatureName()); //.append("\n");
                        }
                        sb.append(address.getLocality()).append(" ");
                        sb.append(address.getPostalCode()).append(" ");
                        sb.append(address.getCountryName());
                        result = sb.toString();

                    }*/

                } catch (IOException e) {
                    Log.e("Location Address Loader", "Unable connect to Geocoder", e);
                } finally {
                    Log.e("finally", " ::::::::::: " + result.toString());
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("address", result);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = " Unable to get address for this location.";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            Log.e("location Address=", locationAddress);
            if (mPref.getPrefrencesBoolean(AppConstants.ParmsType.isLogedIn) && currentLocation != null)
                if (!CommonUtils.getInstance(LocationService.this).isAppInBackground(LocationService.this)) {
                    mPref.setUserLocation(currentLocation.getLatitude() + "," + currentLocation.getLongitude());
                    mPref.setPrefrencesString(AppConstants.ParmsType.USER_ADDRESS, locationAddress);
                    String userId = String.valueOf(new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class).getUserId());
                    UpdateUserLocation locationModel = new UpdateUserLocation();
                    locationModel.userId = userId;
                    locationModel.Lat = String.valueOf(currentLocation.getLatitude());
                    locationModel.Long = String.valueOf(currentLocation.getLongitude());
                    locationModel.address = locationAddress;

//                    Call<DefaultResponse> call = apiInterface.updateUserLocation(userId, String.valueOf(currentLocation.getLatitude()),
//                            String.valueOf(currentLocation.getLongitude()), locationAddress);
                    Call<DefaultResponse> call = apiInterface.updateUserLocation(locationModel);
                    call.enqueue(new RestProcess<DefaultResponse>(AppConstants.ServiceModes.UPDATE_LOCATION, new RestCallback() {
                        @Override
                        public void onFailure(Call call, Throwable t, int serviceMode) {

                        }

                        @Override
                        public void onSuccess(Call call, Response model, int serviceMode) {
                            switch (serviceMode) {
                                case AppConstants.ServiceModes.UPDATE_LOCATION:
                                    break;
                            }
                        }

                        @Override
                        public void onLogout() {

                        }
                    }, LocationService.this, false));
                } else {
                    locationTracker.stopLocationUpdates();
                }
        }
    }
    /*private String makeUrl(double lat, double lng) {
        //  http://maps.googleapis.com/maps/api/geocode/json?latlng=28.620959,77.081805&sensor=true
        //http://maps.googleapis.com/maps/api/geocode/json?latlng=28.620959,77.081805&sensor=true
        StringBuilder builder = new StringBuilder();
        builder.append("http://maps.googleapis.com/maps/api/geocode/json?");
        builder.append("latlng=");
        builder.append(lat);
        builder.append(",");
        builder.append(lng);
        builder.append("&sensor=true");
        //builder.append("&key=" + getResources().getString(R.string.placesearchkey));
        return builder.toString();
    }*/
  /*  @Override
    public void onFailure(RetrofitError e, int serviceMode) {

    }

    @Override
    public void onSuccess(Object model, Response response, int serviceMode) {

        switch (serviceMode) {
            case Constants.ServiceMode.LOCATION_UPDATE:
              *//*  LoginResponseModel loginResponseModel = (LoginResponseModel) model;
                if (loginResponseModel.getStatus() == Constants.Status.SUCCESS) {
                    *//**//*TODO your stuff*//**//*
                } else {
                    locationTracker.stopLocationUpdates();
                }
                Log4Android.e(this, loginResponseModel.getMessage());*//*
                break;
        }
    }*/

    @Override
    public void onFailure(Call call, Throwable t, int serviceMode) {

    }

    @Override
    public void onSuccess(Call call, Response model, int serviceMode) {
        switch (serviceMode) {
            case AppConstants.ServiceModes.UPDATE_LOCATION:
                break;
            case AppConstants.ServiceModes.SERVICE_MODE_GET_NEAR_BY_RESULT:
                break;

        }

    }

    @Override
    public void onLogout() {

    }

}
