package com.mobile.persson.mobleeapp.database.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by persson on 07/12/16.
 */

public class SearchItemModel extends RealmObject {
    @SerializedName("owner")
    public SearchOwner owner;
    @SerializedName("score")
    private int score;
    @SerializedName("last_activity_date")
    private String last_activity_date;
    @SerializedName("question_id")
    private int question_id;
    @SerializedName("title")
    private String title;
    @SerializedName("body")
    private String body;

    public String getBody() {
        return body;
    }

    public SearchOwner getOwner() {
        return owner;
    }

    public int getScore() {
        return score;
    }

    public String getLast_activity_date() {
        return last_activity_date;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public String getTitle() {
        return title;
    }
}
