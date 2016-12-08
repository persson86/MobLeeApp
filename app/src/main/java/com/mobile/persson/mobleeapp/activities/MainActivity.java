package com.mobile.persson.mobleeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.mobile.persson.mobleeapp.R;
import com.mobile.persson.mobleeapp.adapters.RecycleTagsAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    Context context;
    private RecycleTagsAdapter recycleAdapter;
    private LinearLayoutManager layoutManager;

    private List<String> tagsList;

    @ViewById
    RecyclerView rvTags;
    @ViewById
    TextView tvTag;

    @AfterViews
    void initialize() {

        tagsList = new ArrayList<>();
        tagsList.add("Android");
        tagsList.add("Java");
        tagsList.add("Android Studio");
        tagsList.add("Marshmallow");
        tagsList.add("Nexus");

        context = getApplicationContext();
        setRecycleViewConfig();
    }

    @UiThread
    public void setRecycleViewConfig() {

            layoutManager = new GridLayoutManager(MainActivity.this, 2);
            rvTags.setLayoutManager(layoutManager);
            rvTags.setHasFixedSize(true);
            recycleAdapter = new RecycleTagsAdapter(context, tagsList);
            rvTags.setAdapter(recycleAdapter);

        onClickListener();

    }

    private void onClickListener() {
        recycleAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuestionsActivity_.intent(context)
                        .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .extra("tag", tagsList.get(position).toString())
                        .start();

                //overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

    }
}
