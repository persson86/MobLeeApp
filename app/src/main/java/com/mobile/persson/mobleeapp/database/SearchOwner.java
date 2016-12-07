package com.mobile.persson.mobleeapp.database;

import com.google.gson.annotations.SerializedName;

/**
 * Created by persson on 07/12/16.
 */

public class SearchOwner {
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("profile_image")
    private String profile_image;
    @SerializedName("display_name")
    private String display_name;

    public int getUser_id() {
        return user_id;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public String getDisplay_name() {
        return display_name;
    }
}
