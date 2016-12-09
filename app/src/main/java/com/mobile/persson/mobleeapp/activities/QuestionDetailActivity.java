package com.mobile.persson.mobleeapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.persson.mobleeapp.R;
import com.mobile.persson.mobleeapp.adapters.RecycleAnswersAdapter;
import com.mobile.persson.mobleeapp.database.dao.AnswerDAO;
import com.mobile.persson.mobleeapp.database.dao.QuestionDAO;
import com.mobile.persson.mobleeapp.database.models.AnswerItemModel;
import com.mobile.persson.mobleeapp.database.models.AnswerModel;
import com.mobile.persson.mobleeapp.database.models.QuestionItemModel;
import com.mobile.persson.mobleeapp.network.RestService;
import com.mobile.persson.mobleeapp.utils.StringUtil;

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

@EActivity(R.layout.activity_question_detail)
public class QuestionDetailActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private Context context;
    private RecycleAnswersAdapter recycleAdapter;

    List<AnswerItemModel> answerItemModelList;

    private final String ORDER = "desc";
    private final String SORT = "activity";
    private final String SITE = "stackoverflow";

    @Extra
    int questionId;
    @Bean
    QuestionDAO questionDAO;
    @Bean
    AnswerDAO answerDAO;
    @ViewById
    TextView tvTitle;
    @ViewById
    TextView tvBody;
    @ViewById
    RecyclerView rvAnswers;
    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView tvToolbarTitle;

    @AfterViews
    void initialize() {
        context = getApplicationContext();

        startDialog();
        setScreenConfig();
        getAnswersByQuestion();
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
        progressDialog = new ProgressDialog(QuestionDetailActivity.this);
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
        tvToolbarTitle.setText(R.string.tit_question_detail);
    }

    private void loadContent() {
        QuestionItemModel questionItemModel = questionDAO.getQuestionById(questionId);
        tvTitle.setText(questionItemModel.getTitle());
        tvBody.setText(StringUtil.convertHtmlToText(questionItemModel.getBody()));
    }

    @Background
    public void getAnswersByQuestion() {
        answerItemModelList = new ArrayList<>();
        answerItemModelList = answerDAO.getAnswersByQuestion(questionId);

        if (answerItemModelList.size() > 0) {
            setRecycleViewConfig(true);
            return;
        }

        RestService service = RestService.retrofit.create(RestService.class);
        final Call<AnswerModel> call = service.getAnswers(
                questionId,
                ORDER,
                SORT,
                SITE,
                getString(R.string.filter_body_answer));

        call.enqueue(new Callback<AnswerModel>() {
            @Override
            public void onResponse(Response<AnswerModel> response, Retrofit retrofit) {
                AnswerModel answerModel = response.body();

                if (answerModel != null) {
                    answerItemModelList = answerModel.getItems();
                    saveDataIntoRealm();
                    setRecycleViewConfig(false);
                } else
                    Toast.makeText(context, getString(R.string.msg_server_not_responding), Toast.LENGTH_LONG).show();
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
        answerDAO.deleteAnswersByQuestion(questionId);
        answerDAO.saveAnswers(answerItemModelList);
    }

    @UiThread
    public void setRecycleViewConfig(boolean fromRealm) {
        loadContent();

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvAnswers.setLayoutManager(layoutManager);
        rvAnswers.setHasFixedSize(true);
        recycleAdapter = new RecycleAnswersAdapter(context, adjustContentList(fromRealm));
        rvAnswers.setAdapter(recycleAdapter);

        progressDialog.dismiss();
    }

    private List<AnswerItemModel> adjustContentList(boolean fromRealm) {
        List<AnswerItemModel> contentList = new ArrayList<>();

        if (fromRealm) {
            List<AnswerItemModel> realmList = answerDAO.getAnswersByQuestion(questionId);
            for (AnswerItemModel model : realmList)
                contentList.add(model);
        } else
            contentList = answerItemModelList;

        return contentList;
    }
}
