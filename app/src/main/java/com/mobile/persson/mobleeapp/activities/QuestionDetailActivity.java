package com.mobile.persson.mobleeapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    TextView tvNumberAnswers;
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

    private void loadHeaderContent() {
        QuestionItemModel questionItemModel = questionDAO.getQuestionById(questionId);
        tvTitle.setText(questionItemModel.getTitle());
        tvBody.setText(StringUtil.convertHtmlToText(questionItemModel.getBody()));
    }

    @Background
    public void getAnswersByQuestion() {
        answerItemModelList = new ArrayList<>();
        answerItemModelList = answerDAO.getAnswersByQuestion(questionId);

        if (answerItemModelList.size() > 0) {
            setAnswersLayout(true);
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
                    setAnswersLayout(false);
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
        answerDAO.saveAnswersByQuestion(answerItemModelList, questionId);
    }

    @UiThread
    public void setAnswersLayout(boolean fromRealm) {
        loadHeaderContent();

/*        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvAnswers.setLayoutManager(layoutManager);
        rvAnswers.setHasFixedSize(true);
        recycleAdapter = new RecycleAnswersAdapter(context, adjustContentList(fromRealm));
        rvAnswers.setAdapter(recycleAdapter);*/
//------------------------------

        LayoutInflater inflater;
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout llContent = (LinearLayout) findViewById(R.id.llContent);
        List<AnswerItemModel> list = adjustContentList(fromRealm);

        int numAnswers = list.size();
        String totalAnswers = String.valueOf(numAnswers) + " " + "Answers";
        tvNumberAnswers.setText(totalAnswers);

        for (AnswerItemModel answer : list) {
            View view = inflater.inflate(R.layout.item_answer_list, null);
            TextView tvAnswer = (TextView) view.findViewById(R.id.tvAnswer);
            TextView tvUser = (TextView) view.findViewById(R.id.tvUser);

            ImageView ivUser = (ImageView) view.findViewById(R.id.ivUser);
            tvAnswer.setText(answer.getBody());
            tvUser.setText(answer.getOwner().getDisplay_name());

            Glide.with(ivUser.getContext())
                    .load(answer.getOwner().getProfile_image())
                    .into(ivUser);

            int acceptedAnswer = Color.parseColor("#4CAF50");
            if (answer.is_accepted())
                tvAnswer.setTextColor(acceptedAnswer);
            llContent.addView(view);
        }

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
