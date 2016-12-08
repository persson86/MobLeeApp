package com.mobile.persson.mobleeapp.database.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by persson on 07/12/16.
 */

public class SearchTagModel {
    @SerializedName("items")
    private List<SearchItemModel> items;

    public List<SearchItemModel> getItems() {
        return items;
    }
}
