package com.javier.yahoowheather.view;

import android.content.Context;

import com.javier.yahoowheather.model.Condition;
import com.javier.yahoowheather.model.WeatherResult;

/**
 * Created by javiergonzalezcabezas on 21/11/15.
 */
public interface WeatherView {
    void showLoading(boolean state);
    void onRequestSuccess(Condition condition);
    void onRequestError(WeatherResult object);
    void onGeneralError();

    Context getContext();
}
