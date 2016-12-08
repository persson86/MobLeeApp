package com.mobile.persson.mobleeapp.database.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by persson on 08/12/16.
 */

public class AnswerModel {
    @SerializedName("items")
    List<AnswerItemModel> items;

    public List<AnswerItemModel> getItems() {
        return items;
    }
}
