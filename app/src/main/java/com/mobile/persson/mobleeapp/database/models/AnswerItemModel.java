package com.mobile.persson.mobleeapp.database.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by persson on 08/12/16.
 */

public class AnswerItemModel extends RealmObject{
    @SerializedName("owner")
    public SearchOwner owner;
    @SerializedName("answer_id")
    private int answer_id;
    @SerializedName("body")
    private String body;
    @SerializedName("is_accepted")
    private boolean is_accepted;

    public String getBody() {
        return body;
    }

    public SearchOwner getOwner() {
        return owner;
    }

    public int getAnswer_id() {
        return answer_id;
    }

    public boolean is_accepted() {
        return is_accepted;
    }
}
