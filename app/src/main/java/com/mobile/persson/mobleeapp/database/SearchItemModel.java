package com.mobile.persson.mobleeapp.database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.security.acl.Owner;
import java.util.List;

/**
 * Created by persson on 07/12/16.
 */

public class SearchItemModel {
    @SerializedName("owner")
    @Expose
    public Owner owner;
    @SerializedName("score")
    private int score;
    @SerializedName("last_activity_date")
    private String last_activity_date;
    @SerializedName("question_id")
    private int question_id;
    @SerializedName("title")
    private String title;

    public Owner getOwner() {
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

    /*    "items": [
    {
        "tags": [
        "android",
                "android-studio",
                "adb"
        ],
        "owner": {}
        "is_answered": true,
            "view_count": 20634,
            "accepted_answer_id": 37657082,
            "answer_count": 5,
            "score": 8,
            "last_activity_date": 1481114838,
            "creation_date": 1462312397,
            "question_id": 37015030,
            "link": "http://stackoverflow.com/questions/37015030/session-app-error-installing-apk",
            "title": "Session &#39;app&#39;: Error Installing APK"
    }
    }
    ],
            "has_more": true,
            "quota_max": 300,
            "quota_remaining": 197*/
}
