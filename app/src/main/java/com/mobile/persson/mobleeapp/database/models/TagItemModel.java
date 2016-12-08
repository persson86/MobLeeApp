package com.mobile.persson.mobleeapp.database.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by persson on 08/12/16.
 */

public class TagItemModel {
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }
}
