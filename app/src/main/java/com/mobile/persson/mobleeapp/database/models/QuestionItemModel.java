package com.mobile.persson.mobleeapp.database.models;

import io.realm.RealmObject;

/**
 * Created by persson on 07/12/16.
 */

public class QuestionItemModel extends RealmObject {
    public OwnerModel owner;
    private int score;
    private String last_activity_date;
    private int question_id;
    private String title;
    private String body;

    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getBody() {
        return body;
    }

    public OwnerModel getOwner() {
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
