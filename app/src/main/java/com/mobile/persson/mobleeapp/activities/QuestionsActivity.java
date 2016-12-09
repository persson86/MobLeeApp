package com.mobile.persson.mobleeapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.persson.mobleeapp.R;
import com.mobile.persson.mobleeapp.adapters.RecycleQuestionsAdapter;
import com.mobile.persson.mobleeapp.database.dao.AnswerDAO;
import com.mobile.persson.mobleeapp.database.dao.QuestionDAO;
import com.mobile.persson.mobleeapp.database.models.QuestionItemModel;
import com.mobile.persson.mobleeapp.database.models.QuestionModel;
import com.mobile.persson.mobleeapp.network.RestService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

@EActivity(R.layout.activity_questions)
public class QuestionsActivity extends AppCompatActivity {
    private Context context;
    private ProgressDialog progressDialog;
    private RecycleQuestionsAdapter recycleAdapter;

    List<QuestionItemModel> questionItemModelList;

    private final String ORDER = "desc";
    private final String SORT = "activity";
    private final String SITE = "stackoverflow";
    private final String PAGESIZE = "20";

    @Extra
    String tag;
    @Bean
    QuestionDAO questionDAO;
    @Bean
    AnswerDAO answerDAO;
    @ViewById
    RecyclerView rvQuestions;
    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView tvToolbarTitle;

    @AfterViews
    void initialize() {
        context = getApplicationContext();

        startDialog();
        setScreenConfig();
        getQuestionsByTag();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startDialog() {
        progressDialog = new ProgressDialog(QuestionsActivity.this);
        progressDialog.setTitle(getResources().getString(R.string.msg_retrieving_data));
        progressDialog.setMessage(getResources().getString(R.string.msg_wait));
        progressDialog.show();
    }

    private void setScreenConfig() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        loadToolbar();
    }

    private void loadToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvToolbarTitle.setText(R.string.tit_questions);
    }

    @Background
    public void getQuestionsByTag() {
        questionItemModelList = new ArrayList<>();
        questionItemModelList = questionDAO.getQuestionsByTag(tag);

        if (questionItemModelList.size() > 0) {
            setRecycleViewConfig(true);
            return;
        }

        RestService service = RestService.retrofit.create(RestService.class);
        final Call<QuestionModel> call = service.getQuestionsByTag(
                ORDER,
                SORT,
                tag,
                SITE,
                PAGESIZE,
                getString(R.string.filter_body_tag));

        call.enqueue(new Callback<QuestionModel>() {
            @Override
            public void onResponse(Response<QuestionModel> response, Retrofit retrofit) {
                QuestionModel questionModel = response.body();

                if (questionModel != null) {
                    questionItemModelList = questionModel.getItems();
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
        for (QuestionItemModel m : questionItemModelList)
            m.setTag(tag);

        questionDAO.deleteQuestionsByTag(tag);
        questionDAO.saveQuestionsByTag(questionItemModelList);
    }

    @UiThread
    public void setRecycleViewConfig(boolean fromRealm) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvQuestions.setLayoutManager(layoutManager);
        rvQuestions.setHasFixedSize(true);
        recycleAdapter = new RecycleQuestionsAdapter(context, adjustContentList(fromRealm));
        rvQuestions.setAdapter(recycleAdapter);

        onClickListener(fromRealm);
        progressDialog.dismiss();
    }

    private void onClickListener(boolean fromRealm) {
        if (fromRealm){
            questionItemModelList = new ArrayList<>();
            questionItemModelList = questionDAO.getQuestionsByTag(tag);
        }

        recycleAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuestionDetailActivity_
                        .intent(context)
                        .questionId(questionItemModelList.get(position).getQuestion_id())
                        .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .start();

                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

    }

    private List<QuestionItemModel> adjustContentList(boolean fromRealm) {
        List<QuestionItemModel> contentList = new ArrayList<>();

        if (fromRealm) {
            List<QuestionItemModel> realmList = questionDAO.getQuestionsByTag(tag);
            for (QuestionItemModel model : realmList)
                contentList.add(model);
        } else
            contentList = questionItemModelList;

        return contentList;
    }
}
