package com.javier.yahoowheather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by javiergonzalezcabezas on 21/11/15.
 */
public class Channel {
    @SerializedName("item")
    @Expose
    private Item item;

    /**
     *
     * @return
     * The item
     */
    public Item getItem() {
        return item;
    }

    /**
     *
     * @param item
     * The item
     */
    public void setItem(Item item) {
        this.item = item;
    }

}
