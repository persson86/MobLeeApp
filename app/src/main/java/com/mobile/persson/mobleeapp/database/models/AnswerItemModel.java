package com.mobile.persson.mobleeapp.database.models;

import io.realm.RealmObject;

/**
 * Created by persson on 08/12/16.
 */

public class AnswerItemModel extends RealmObject {
    public OwnerModel owner;
    private int answer_id;
    private int question_id;
    private String body;
    private boolean is_accepted;

    public String getBody() {
        return body;
    }

    public OwnerModel getOwner() {
        return owner;
    }

    public int getAnswer_id() {
        return answer_id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public boolean is_accepted() {
        return is_accepted;
    }
}
