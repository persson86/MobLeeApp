package com.mobile.persson.mobleeapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.persson.mobleeapp.R;
import com.mobile.persson.mobleeapp.adapters.RecycleAnswersAdapter;
import com.mobile.persson.mobleeapp.adapters.RecycleQuestionsAdapter;
import com.mobile.persson.mobleeapp.database.dao.QuestionDAO;
import com.mobile.persson.mobleeapp.database.models.AnswerItemModel;
import com.mobile.persson.mobleeapp.database.models.AnswerModel;
import com.mobile.persson.mobleeapp.database.models.SearchItemModel;
import com.mobile.persson.mobleeapp.network.RestService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

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

    SearchItemModel itemQuestion;
    List<AnswerItemModel> answerItemModelList;

    private int questionId = 0;
    private final String ORDER = "desc";
    private final String SORT = "activity";
    private final String SITE = "stackoverflow";

    @Bean
    QuestionDAO questionDAO;
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
        startDialog();
        setActivityConfig();
        setScreenConfig();
        restGetAnswers();
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

    private void setActivityConfig() {
        context = getApplicationContext();
        questionId = (Integer) getIntent().getSerializableExtra("question_id");
        itemQuestion = questionDAO.getQuestionById(questionId);
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
        tvTitle.setText(itemQuestion.getTitle());
        tvBody.setText(convertHtmlToText(itemQuestion.getBody()));
    }

    private Spanned convertHtmlToText(String htmlContent) {
        String htmlAsString = htmlContent;
        Spanned htmlAsSpanned = Html.fromHtml(htmlAsString);
        return htmlAsSpanned;
    }

    @Background
    public void restGetAnswers() {
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
                answerItemModelList = answerModel.getItems();
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
        loadContent();

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvAnswers.setLayoutManager(layoutManager);
        rvAnswers.setHasFixedSize(true);
        recycleAdapter = new RecycleAnswersAdapter(context, answerItemModelList);
        rvAnswers.setAdapter(recycleAdapter);

        progressDialog.dismiss();
    }

}
