package com.javier.yahoowheather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by javiergonzalezcabezas on 21/11/15.
 */
public class Item {
    @SerializedName("condition")
    @Expose
    private Condition condition;

    /**
     *
     * @return
     * The condition
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     *
     * @param condition
     * The condition
     */
    public void setCondition(Condition condition) {
        this.condition = condition;
    }

}
