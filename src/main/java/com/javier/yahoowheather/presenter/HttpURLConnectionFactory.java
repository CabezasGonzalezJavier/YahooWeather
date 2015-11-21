package com.javier.yahoowheather.presenter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by javiergonzalezcabezas on 21/11/15.
 */
public class HttpURLConnectionFactory {
    private static HttpURLConnection sConnection;
    private static boolean hasmockConnection;

    public static HttpURLConnection getHttpURLConnection(URL url) throws IOException {



        if(!hasmockConnection){
            sConnection = (HttpURLConnection) url.openConnection();
        } else {
            hasmockConnection = false;
        }

        return sConnection;
    }

    public static void setHttpURLConnection(HttpURLConnection mockReader) {
        sConnection = mockReader;
        hasmockConnection = true;
    }
}

