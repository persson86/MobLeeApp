package com.mobile.persson.mobleeapp.database.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by persson on 07/12/16.
 */

public class QuestionModel {
    @SerializedName("items")
    private List<QuestionItemModel> items;

    public List<QuestionItemModel> getItems() {
        return items;
    }
}
