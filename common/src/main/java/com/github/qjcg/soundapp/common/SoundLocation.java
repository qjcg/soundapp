package com.github.qjcg.soundapp.common;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by raphael on 2014-10-25.
 */
public class SoundLocation implements LocationListener {
    private LocationManager locationManager;
    private String provider;

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("SoundApp", "onProviderEnabled with " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("SoundApp", "onProviderDisabled with " + provider);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("SoundApp", "onLocationChange: " + "Latitude: " + location.getLatitude() + " | Longitude: " + location.getLongitude());
        int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());

        // code what you need
    }
}

