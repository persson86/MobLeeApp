package com.mobile.persson.mobleeapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mobile.persson.mobleeapp.R;
import com.mobile.persson.mobleeapp.database.SearchItemModel;
import com.mobile.persson.mobleeapp.database.SearchTagModel;
import com.mobile.persson.mobleeapp.network.RestService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private RestService service;

    SearchTagModel searchTagModel;
    List<SearchItemModel> searchItemModels;

    @AfterViews
    void initialize() {
        restGetQuestionsByTag();
    }

    @Background
    public void restGetQuestionsByTag() {
        service = RestService.retrofit.create(RestService.class);
        final Call<SearchTagModel> call = service.getQuestionsByTag("desc", "activity", "android-studio", "stackoverflow");
        call.enqueue(new Callback<SearchTagModel>() {
            @Override
            public void onResponse(Response<SearchTagModel> response, Retrofit retrofit) {
                searchTagModel = response.body();
                searchItemModels = searchTagModel.getItems();
                int i = 0;
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("teste error", t.getMessage());
            }
        });
    }
}
