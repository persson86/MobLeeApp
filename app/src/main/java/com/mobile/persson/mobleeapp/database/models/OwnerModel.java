package com.mobile.persson.mobleeapp.database.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by persson on 07/12/16.
 */

public class OwnerModel extends RealmObject{
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("profile_image")
    private String profile_image;
    @SerializedName("display_name")
    private String display_name;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }
}
