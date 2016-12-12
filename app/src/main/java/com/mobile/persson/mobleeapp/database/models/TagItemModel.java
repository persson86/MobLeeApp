package com.mobile.persson.mobleeapp.database.models;

import io.realm.RealmObject;

/**
 * Created by persson on 08/12/16.
 */

public class TagItemModel extends RealmObject {
    private String name;

    public String getName() {
        return name;
    }
}
