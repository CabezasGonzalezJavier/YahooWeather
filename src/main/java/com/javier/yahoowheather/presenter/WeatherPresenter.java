package com.javier.yahoowheather.presenter;

import com.javier.yahoowheather.model.WeatherResult;

/**
 * Created by javiergonzalezcabezas on 21/11/15.
 */
public interface WeatherPresenter {
    /**
     * executed the asyncTask
     * @param url
     * @param type
     */
    void executeAsync(String url, String type);

    /**
     * call the client
     * @param city
     * @param type
     * @return
     */
    WeatherResult callClient(final String city, final String type);
}
