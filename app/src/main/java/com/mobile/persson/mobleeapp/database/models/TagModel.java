package com.mobile.persson.mobleeapp.database.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by persson on 08/12/16.
 */

public class TagModel {
    @SerializedName("items")
    List<TagItemModel> items;

    public List<TagItemModel> getItems() {
        return items;
    }
}
