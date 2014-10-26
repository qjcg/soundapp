package com.github.qjcg.soundapp.common;

import android.content.Context;
import android.location.Criteria;
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
    private Location location;

    public SoundLocation(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            onLocationChanged(location);
        }

        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    public void stopListening(){
        locationManager.removeUpdates(this);
    }

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
        this.location = location;

        // conserve battery, stop listening after you get one reading.
        stopListening();
    }

    public Location getLocation() {
        return this.location;
    }
}

