package com.javier.yahoowheather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.javier.yahoowheather.model.Condition;
import com.javier.yahoowheather.model.WeatherResult;
import com.javier.yahoowheather.presenter.WeatherPresenter;
import com.javier.yahoowheather.presenter.WeatherPresenterImpl;
import com.javier.yahoowheather.utils.Constants;
import com.javier.yahoowheather.utils.Utils;
import com.javier.yahoowheather.view.WeatherView;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnCameraChangeListener,LocationListener, OnMapReadyCallback, WeatherView {


    private static final String sTAG = "MapsActivity";
    private GoogleMap mMap;
    private LatLng mUserLocation;
    private Location mLocation;
    private WeatherPresenter mWeatherPresenter;
    private View mLoading;
    private TextView mForecast;
    private RelativeLayout mRelativeLayout;
    private LocationManager mLocationManager;
    Long mLastTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mLastTime = System.currentTimeMillis();
        mLocation = new Location("");
        mWeatherPresenter = new WeatherPresenterImpl(this);
        mLoading = findViewById(R.id.activity_main_loading);
        mForecast = (TextView) findViewById(R.id.activity_main_forecast_text);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.activity_main_relative_layout);

        mForecast.setVisibility(View.GONE);
        mLoading.setVisibility(View.GONE);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Constants.LOCATION_REFRESH_TIME, Constants.LOCATION_REFRESH_DISTANCE, this);
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

        setUpMapIfNeeded();


    }

    /**
     * got the location
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        // Check if we were successful in obtaining the map.
        if (mMap != null) {
            mMap.setMyLocationEnabled(true);

            mMap.setOnMyLocationChangeListener(myLocationChangeListener);

            if (mMap.getMyLocation() != null && mUserLocation == null) {
                Log.d(sTAG, "" + mMap.getMyLocation().getLatitude() + mMap.getMyLocation().getLongitude());
                mUserLocation = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
                mLocation = new Location(mMap.getMyLocation());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mUserLocation, Constants.ZOOM_MAP);
                mMap.animateCamera(cameraUpdate);

            }
        }
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {

            mLocation = location;
            mUserLocation = new LatLng(location.getLatitude(), location.getLongitude());
            if (mMap != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mUserLocation, Constants.ZOOM_MAP));
                // Turn off after zoomed into current location
                mMap.setOnMyLocationChangeListener(null);
            }
        }
    };


    /**
     * got the temperature
     */
    public void getTemperature() {
        String city = Utils.getCity(mLocation, MapsActivity.this);
        mWeatherPresenter.executeAsync(city, Constants.TYPE);
        showLoading(true);
    }

    @Override
    public void showLoading(boolean state) {
        int visibility = (state) ? (View.VISIBLE) : (View.GONE);
        mLoading.setVisibility(visibility);
    }

    @Override
    public void onRequestSuccess(Condition condition) {
        mForecast.setText(condition.getText());
        mForecast.setVisibility(View.VISIBLE);
        int temperature = Integer.parseInt(condition.getTemp());
        colorBackground(temperature);
        showLoading(false);
        mMap.setOnCameraChangeListener(this);
    }

    @Override
    public void onRequestError(WeatherResult object) {
        Toast.makeText(this, R.string.activity_main_on_request_error, Toast.LENGTH_LONG).show();
        showLoading(false);
    }

    @Override
    public void onGeneralError() {
        if (Utils.isOnline(this)) {
            Toast.makeText(this, R.string.activity_main_general_error, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.activity_main_no_internet, Toast.LENGTH_LONG).show();
        }
        showLoading(false);
    }

    @Override
    public Context getContext() {
        return this;
    }

    /**
     * set the color
     * @param temperature
     */
    public void colorBackground(int temperature) {
        if (temperature < 0) {
            mRelativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorFrozen));
        } else if (temperature < 14) {
            mRelativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorCold));
        } else if (temperature < 20) {
            mRelativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorTempered));
        } else {
            mRelativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorHot));
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        getWeather(location);
    }

    public void getWeather(Location location) {
        mLocation = location;
        mUserLocation = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mUserLocation, Constants.ZOOM_MAP);
        mMap.animateCamera(cameraUpdate);
        Log.d(sTAG, "onLocationChanged");
        getTemperature();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.removeUpdates(this);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Log.d(sTAG, "onCameraChange");
        Long current = System.currentTimeMillis();
        if (current >mLastTime+9000){

                mWeatherPresenter = new WeatherPresenterImpl(this);
                Location location = new Location("");
                location.setLatitude(mMap.getCameraPosition().target.latitude);
                location.setLongitude(mMap.getCameraPosition().target.longitude);
                getWeather(location);
        }
        mLastTime = current;
    }
}
