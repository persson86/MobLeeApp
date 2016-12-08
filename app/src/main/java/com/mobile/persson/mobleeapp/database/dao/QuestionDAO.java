package com.mobile.persson.mobleeapp.database.dao;

import android.content.Context;

import com.mobile.persson.mobleeapp.database.DatabaseHelper;
import com.mobile.persson.mobleeapp.database.models.SearchItemModel;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by persson on 08/12/16.
 */

@EBean(scope = EBean.Scope.Singleton)
public class QuestionDAO {
    @RootContext
    Context context;

    @Bean
    DatabaseHelper dbHelper;

    public void saveQuestions(List<SearchItemModel> model) {
        Realm realm = dbHelper.getRealm();
        realm.beginTransaction();
        realm.where(SearchItemModel.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        realm.beginTransaction();
        realm.copyToRealm(model);
        realm.commitTransaction();
    }

    public List<SearchItemModel> getQuestions() {
        return dbHelper.getRealm().where(SearchItemModel.class).findAll();
    }

    public SearchItemModel getQuestionById(long question_id) {
        return dbHelper.getRealm().where(SearchItemModel.class).equalTo("question_id", question_id).findFirst();
    }

    public void deleteQuestions() {
        Realm realm = dbHelper.getRealm();
        realm.beginTransaction();
        realm.where(SearchItemModel.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
    }

}