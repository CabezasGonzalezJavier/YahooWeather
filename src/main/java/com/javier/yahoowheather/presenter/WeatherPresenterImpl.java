package com.javier.yahoowheather.presenter;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.javier.yahoowheather.model.Condition;
import com.javier.yahoowheather.model.WeatherResult;
import com.javier.yahoowheather.utils.Constants;
import com.javier.yahoowheather.view.WeatherView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by javiergonzalezcabezas on 21/11/15.
 */
public class WeatherPresenterImpl extends AsyncTask<String, Void, WeatherResult> implements WeatherPresenter {
    private WeatherView mWeatherView;
    public WeatherPresenterImpl(WeatherView loginView) {
        this.mWeatherView = loginView;
    }

    @Override
    public void executeAsync(String city, String type) {
        this.execute(city, type);
    }
    @Override
    protected WeatherResult doInBackground(String... params) {

        final String city = params[0];
        final String type = params[1];

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Constants.BEFORE_URL);
        stringBuilder.append(city);
        stringBuilder.append(Constants.AFTER_URL);

        return callClient(stringBuilder.toString(), type);
    }

    public WeatherResult callClient(final String url, final String type) {

        WeatherResult responseModel = new WeatherResult();
        String responseJson;

        try {
            URL obj = new URL(url);

            HttpURLConnection connection = HttpURLConnectionFactory.getHttpURLConnection(obj);


            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            responseJson = response.toString();


            responseModel.setCode(connection.getResponseCode());
            responseModel.setInfo(responseJson);
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
            responseJson = e.toString();
            responseModel.setInterneterror(true);
        }

        return responseModel;
    }

    @Override
    protected void onPostExecute(WeatherResult response) {
        super.onPostExecute(response);

//        Log.v("onPostExecute", response.getCreatedAt().toString());

        if (response.isInterneterror()) {

            mWeatherView.onGeneralError();

        } else {
            if (response.checkStatusCode(response.getCode())) {
                Gson gson = new Gson();





                //convert the json string back to object
                WeatherResult fullObject = gson.fromJson(response.getInfo(), WeatherResult.class);
                Condition condition = fullObject.getQuery().getResults().getChannel().getItem().getCondition();

                mWeatherView.onRequestSuccess(condition);
            } else {
                mWeatherView.onRequestError(response);
            }

        }

    }
}
