package com.mobile.persson.mobleeapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
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
    private RestService service;

    private RecycleAnswersAdapter recycleAdapter;
    private LinearLayoutManager layoutManager;

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

    @AfterViews
    void initialize() {
        startDialog();
        setActivityConfig();
        setScreenConfig();
        loadContent();
        restGetAnswers();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //loadToolbar();
    }

    private void loadContent() {
        tvTitle.setText(itemQuestion.getTitle());

        String htmlAsString = itemQuestion.getBody();
        Spanned htmlAsSpanned = Html.fromHtml(htmlAsString);
        tvBody.setText(htmlAsSpanned);

        progressDialog.dismiss();
    }

    @Background
    public void restGetAnswers() {
        service = RestService.retrofit.create(RestService.class);
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
        layoutManager = new LinearLayoutManager(context);
        rvAnswers.setLayoutManager(layoutManager);
        rvAnswers.setHasFixedSize(true);
        recycleAdapter = new RecycleAnswersAdapter(context, answerItemModelList);
        rvAnswers.setAdapter(recycleAdapter);

        onClickListener();
        progressDialog.dismiss();
    }

    //remover
    private void onClickListener() {
        recycleAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*                QuestionDetailActivity_.intent(context)
                        .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .extra("question_id", searchItemModels.get(position).getQuestion_id())
                        .start();*/

                //overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

    }

}
