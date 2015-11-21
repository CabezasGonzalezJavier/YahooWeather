package com.javier.yahoowheather.utils;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by javiergonzalezcabezas on 21/11/15.
 */
public class Utils {
    private static final String sTAG = "MapsActivity";

    //    public static LatLng getLastLocation(Context context) {
//        LocationManager service = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        String provider = service.getBestProvider(criteria, false);
//        Location location = service.getLastKnownLocation(provider);
//        if (location != null) {
//            return new LatLng(location.getLatitude(),location.getLongitude());
//        }
//        else  {
//            return new LatLng(Constants.DEFAULT_LATITUDE, Constants.DEFAULT_LONGITUDE);
//        }
//    }

    /**
     * Checked the connection
     * @param activity
     * @return
     */
    public static boolean isOnline(Activity activity) {
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Got the City
     * @param location
     * @param activity
     * @return
     */
    public static String getCity(Location location, Activity activity) {
        String city = "";
        Toast.makeText(
                activity,
                "Location changed: Lat: " + location.getLatitude() + " Lng: "
                        + location.getLongitude(), Toast.LENGTH_SHORT).show();
        String longitude = "Longitude: " + location.getLongitude();
        Log.v(sTAG, longitude);
        String latitude = "Latitude: " + location.getLatitude();
        Log.v(sTAG, latitude);

        //To get city name from coordinates
        Geocoder gcd = new Geocoder(activity, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
            if (addresses.size() > 0)
                city = addresses.get(0).getLocality();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(activity, city, Toast.LENGTH_SHORT).show();
        return city;
    }

}
