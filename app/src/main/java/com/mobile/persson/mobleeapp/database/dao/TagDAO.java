package com.mobile.persson.mobleeapp.database.dao;

import android.content.Context;

import com.mobile.persson.mobleeapp.database.DatabaseHelper;
import com.mobile.persson.mobleeapp.database.models.TagItemModel;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

import io.realm.Realm;

/**
 * Created by persson on 08/12/16.
 */

@EBean(scope = EBean.Scope.Singleton)
public class TagDAO {
    @RootContext
    Context context;

    @Bean
    DatabaseHelper dbHelper;

    public void saveTags(List<TagItemModel> model) {
        Realm realm = dbHelper.getRealm();
        realm.beginTransaction();
        realm.where(TagItemModel.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        realm.beginTransaction();
        realm.copyToRealm(model);
        realm.commitTransaction();
    }

    public List<TagItemModel> getTags() {
        return dbHelper.getRealm().where(TagItemModel.class).findAll();
    }

    public void deleteTags() {
        Realm realm = dbHelper.getRealm();
        realm.beginTransaction();
        realm.where(TagItemModel.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    public int getSize() {
        return dbHelper.getRealm().where(TagItemModel.class).findAll().size();
    }
}