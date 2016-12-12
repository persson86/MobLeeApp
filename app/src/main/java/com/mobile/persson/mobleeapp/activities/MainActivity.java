package com.mobile.persson.mobleeapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.persson.mobleeapp.R;
import com.mobile.persson.mobleeapp.adapters.RecycleTagsAdapter;
import com.mobile.persson.mobleeapp.database.dao.AnswerDAO;
import com.mobile.persson.mobleeapp.database.dao.QuestionDAO;
import com.mobile.persson.mobleeapp.database.dao.TagDAO;
import com.mobile.persson.mobleeapp.database.models.TagItemModel;
import com.mobile.persson.mobleeapp.database.models.TagModel;
import com.mobile.persson.mobleeapp.network.RestService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private Context context;
    private ProgressDialog progressDialog;
    private RecycleTagsAdapter recycleAdapter;

    private List<String> tagList;
    private List<TagItemModel> tagItemModelList;

    private final String TAG = "android";
    private final String SITE = "stackoverflow";
    private final String PAGESIZE = "20";

    @Bean
    TagDAO tagDAO;
    @Bean
    QuestionDAO questionDAO;
    @Bean
    AnswerDAO answerDAO;
    @ViewById
    RecyclerView rvTags;
    @ViewById
    TextView tvTag;
    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView tvToolbarTitle;

    @AfterViews
    void initialize() {
        context = getApplicationContext();

        startDialog();
        cleanRealm();
        setScreenConfig();

        if (isConnectedToInternet())
            getGetRelatedTags();
    }

    private void startDialog() {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle(getResources().getString(R.string.msg_retrieving_data));
        progressDialog.setMessage(getResources().getString(R.string.msg_wait));
        progressDialog.show();
    }

    private void cleanRealm() {
        answerDAO.deleteAllAnswers();
        questionDAO.deleteAllQuestions();
        tagDAO.deleteTags();
    }

    private void setScreenConfig() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        loadToolbar();
    }

    private void loadToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvToolbarTitle.setText(R.string.tit_tags);
        tvToolbarTitle.setPadding(30, 0, 0, 0);
    }

    @Background
    public void getGetRelatedTags() {
        tagItemModelList = new ArrayList<>();
        tagItemModelList = tagDAO.getTags();

        if (tagItemModelList.size() > 0) {
            setRecycleViewConfig(true);
            return;
        }

        RestService service = RestService.retrofit.create(RestService.class);
        final Call<TagModel> call = service.getRelatedTags(
                TAG,
                SITE,
                PAGESIZE);

        call.enqueue(new Callback<TagModel>() {
            @Override
            public void onResponse(Response<TagModel> response, Retrofit retrofit) {
                TagModel tagModel = response.body();

                if (tagModel != null) {
                    tagItemModelList = tagModel.getItems();
                    saveDataIntoRealm();
                    setRecycleViewConfig(false);
                } else {
                    Toast.makeText(context, getString(R.string.msg_server_not_responding), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(getString(R.string.log_error), t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private void saveDataIntoRealm() {
        tagDAO.saveTags(tagItemModelList);
    }

    @UiThread
    public void setRecycleViewConfig(boolean fromRealm) {
        LinearLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
        rvTags.setLayoutManager(layoutManager);
        rvTags.setHasFixedSize(true);
        recycleAdapter = new RecycleTagsAdapter(context, adjustContentList(fromRealm));
        rvTags.setAdapter(recycleAdapter);

        onClickListener();
        progressDialog.dismiss();
    }

    private void onClickListener() {
        recycleAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuestionsActivity_
                        .intent(context)
                        .tag(tagList.get(position))
                        .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .start();

                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

    }

    private List<String> adjustContentList(boolean fromRealm) {
        tagList = new ArrayList<>();
        tagList.add("android");
        tagList.add("java");
        tagList.add("android-studio");
        tagList.add("marshmallow");
        tagList.add("nexus");

        if (fromRealm)
            tagItemModelList = new ArrayList<>();
        tagItemModelList = tagDAO.getTags();

        for (TagItemModel tag : tagItemModelList) {
            tagList.add(tag.getName());
        }

        tagList = new ArrayList<>(new LinkedHashSet<>(tagList));
        return tagList;
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null) {
            Toast.makeText(context, getString(R.string.msg_no_internet), Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;
    }
}
