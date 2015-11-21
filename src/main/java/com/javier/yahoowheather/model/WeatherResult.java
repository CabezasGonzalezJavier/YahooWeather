package com.javier.yahoowheather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by javiergonzalezcabezas on 21/11/15.
 */
public class WeatherResult {
    private int mCode;
    private String info;
    private boolean mInterneterror;
    @SerializedName("query")
    @Expose
    private Query query;

    public WeatherResult() {}

    public boolean checkStatusCode(int status) {

        boolean successful = false;

        if (status >=200 && status<300){
            successful = true;
        }
        return successful;
    }

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        this.mCode = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isInterneterror() {
        return mInterneterror;
    }

    public void setInterneterror(boolean interneterror) {
        this.mInterneterror = interneterror;
    }
    /**
     *
     * @return
     * The query
     */
    public Query getQuery() {
        return query;
    }

    /**
     *
     * @param query
     * The query
     */
    public void setQuery(Query query) {
        this.query = query;
    }

}
