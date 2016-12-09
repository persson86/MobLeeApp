package com.mobile.persson.mobleeapp.database.dao;

import android.content.Context;

import com.mobile.persson.mobleeapp.database.DatabaseHelper;
import com.mobile.persson.mobleeapp.database.models.AnswerItemModel;

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
public class AnswerDAO {
    @RootContext
    Context context;

    @Bean
    DatabaseHelper dbHelper;

    public void saveAnswers(List<AnswerItemModel> model) {
        Realm realm = dbHelper.getRealm();
        realm.beginTransaction();
        realm.where(AnswerItemModel.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        realm.beginTransaction();
        realm.copyToRealm(model);
        realm.commitTransaction();
    }

    public List<AnswerItemModel> getAnswersByQuestion(long questionId) {
        Realm realm = dbHelper.getRealm();
        RealmResults<AnswerItemModel> result = realm.where(AnswerItemModel.class)
                .equalTo("question_id", questionId)
                .findAll();
        return result;
    }

    public void deleteAnswersByQuestion(long questionId) {
        Realm realm = dbHelper.getRealm();
        realm.beginTransaction();
        realm.where(AnswerItemModel.class).equalTo("question_id", questionId).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
    }
}