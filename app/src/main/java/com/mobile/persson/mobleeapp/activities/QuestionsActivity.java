package com.mobile.persson.mobleeapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.persson.mobleeapp.R;
import com.mobile.persson.mobleeapp.adapters.RecycleQuestionsAdapter;
import com.mobile.persson.mobleeapp.adapters.RecycleTagsAdapter;
import com.mobile.persson.mobleeapp.database.SearchItemModel;
import com.mobile.persson.mobleeapp.database.SearchTagModel;
import com.mobile.persson.mobleeapp.network.RestService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

@EActivity(R.layout.activity_questions)
public class QuestionsActivity extends AppCompatActivity {
    private RestService service;
    private Context context;
    private ProgressDialog progressDialog;

    private RecycleQuestionsAdapter recycleAdapter;
    private LinearLayoutManager layoutManager;

    SearchTagModel searchTagModel;
    List<SearchItemModel> searchItemModels;

    private String tag;
    private final String ORDER = "desc";
    private final String SORT = "activity";
    private final String SITE = "stackoverflow";
    private final String PAGESIZE = "20";

    @ViewById
    RecyclerView rvQuestions;

    @AfterViews
    void initialize() {
        startDialog();
        setActivityConfig();
        setScreenConfig();

        restGetQuestionsByTag(tag);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private void startDialog() {
        progressDialog = new ProgressDialog(QuestionsActivity.this);
        progressDialog.setTitle(getResources().getString(R.string.msg_retrieving_data));
        progressDialog.setMessage(getResources().getString(R.string.msg_wait));
        progressDialog.show();
    }

    private void setActivityConfig() {
        context = getApplicationContext();
        tag = (String) getIntent().getSerializableExtra("tag");
    }

    private void setScreenConfig() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //loadToolbar();
    }

    private void loadToolbar() {
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //tvToolbarTitle.setText(R.string.toolbar_title);
        //ivIcon.setVisibility(View.GONE);
    }

    private String formatTag(String tag) {
        tag = tag.toLowerCase();
        tag = tag.replace(' ', '-');
        return tag;
    }

    @Background
    public void restGetQuestionsByTag(String tag) {
        service = RestService.retrofit.create(RestService.class);
        final Call<SearchTagModel> call = service.getQuestionsByTag(ORDER, SORT, formatTag(tag), SITE, PAGESIZE);
        call.enqueue(new Callback<SearchTagModel>() {
            @Override
            public void onResponse(Response<SearchTagModel> response, Retrofit retrofit) {
                searchTagModel = response.body();
                searchItemModels = searchTagModel.getItems();
                setRecycleViewConfig();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(getString(R.string.log_error), t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    @UiThread
    public void setRecycleViewConfig() {
        layoutManager = new LinearLayoutManager(context);
        rvQuestions.setLayoutManager(layoutManager);
        rvQuestions.setHasFixedSize(true);
        recycleAdapter = new RecycleQuestionsAdapter(context, searchItemModels);
        rvQuestions.setAdapter(recycleAdapter);

        onClickListener();
        progressDialog.dismiss();
    }

    private void onClickListener() {
        recycleAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*
                QuestionsActivity_.intent(context)
                        .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .extra("tag", searchItemModels.get(position).toString())
                        .start();
*/

                Toast.makeText(context, searchItemModels.get(position).getTitle(), Toast.LENGTH_SHORT).show();

                //overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

    }

}
