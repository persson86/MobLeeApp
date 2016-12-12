package com.mobile.persson.mobleeapp.database.dao;

import android.content.Context;

import com.mobile.persson.mobleeapp.database.DatabaseHelper;
import com.mobile.persson.mobleeapp.database.models.QuestionItemModel;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

import io.realm.Realm;

/**
 * Created by persson on 08/12/16.
 */

@EBean(scope = EBean.Scope.Singleton)
public class QuestionDAO {
    @RootContext
    Context context;

    @Bean
    DatabaseHelper dbHelper;

    public void saveQuestionsByTag(List<QuestionItemModel> model) {
        Realm realm = dbHelper.getRealm();
        realm.beginTransaction();
        realm.where(QuestionItemModel.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        realm.beginTransaction();
        realm.copyToRealm(model);
        realm.commitTransaction();
    }

    public List<QuestionItemModel> getQuestionsByTag(String tag) {
        return dbHelper.getRealm().where(QuestionItemModel.class).equalTo("tag", tag).findAll();
    }

    public QuestionItemModel getQuestionById(long question_id) {
        return dbHelper.getRealm().where(QuestionItemModel.class).equalTo("question_id", question_id).findFirst();
    }

    public void deleteAllQuestions() {
        Realm realm = dbHelper.getRealm();
        realm.beginTransaction();
        realm.where(QuestionItemModel.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
    }
}