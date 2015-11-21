package com.javier.yahoowheather.utils;

/**
 * Created by javiergonzalezcabezas on 21/11/15.
 */
public class Constants {
    public static final int LOCATION_REFRESH_TIME = 5000;
    public static final int LOCATION_REFRESH_DISTANCE = 15;
    public static final int TOTAL_TABS = 4;
    public static final float ZOOM_MAP = 10.0f;
    // London lat & long
    public static final double DEFAULT_LATITUDE = 51.500152;
    public static final double DEFAULT_LONGITUDE = -0.126236;


    public static final String URL = "https://query.yahooapis.com/v1/public/yql?q=select%20item.condition%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22nome%2C%20ak%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    public static final String BEFORE_URL = "https://query.yahooapis.com/v1/public/yql?q=select%20item.condition%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22";
    public static final String AFTER_URL = "2C%20ak%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

    public static final String TYPE = "GET";
}
