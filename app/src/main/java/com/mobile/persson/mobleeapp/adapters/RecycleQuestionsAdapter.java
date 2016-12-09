package com.mobile.persson.mobleeapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mobile.persson.mobleeapp.R;
import com.mobile.persson.mobleeapp.database.models.QuestionItemModel;

import java.util.List;

/**
 * Created by persson on 07/12/16.
 */

public class RecycleQuestionsAdapter extends RecyclerView.Adapter<RecycleQuestionsAdapter.RecycleItemViewHolder> {
    private List<QuestionItemModel> questionsList;
    private Context context;
    private AdapterView.OnItemClickListener itemClickListener;

    public RecycleQuestionsAdapter(Context c, List<QuestionItemModel> content) {
        questionsList = content;
        context = c;
    }

    @Override
    public RecycleItemViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_question_list, parent, false);
        return new RecycleItemViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(RecycleItemViewHolder holder, int position) {
        holder.tvTitle.setText(questionsList.get(position).getTitle());
        holder.tvScore.setText(String.valueOf(questionsList.get(position).getScore()));
        holder.tvUser.setText(questionsList.get(position).getOwner().getDisplay_name());

        Glide.with(holder.ivUser.getContext())
                .load(questionsList.get(position).getOwner().getProfile_image())
                .into(holder.ivUser);
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.itemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(RecycleItemViewHolder itemHolder) {
        if (itemClickListener != null) {
            itemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    public class RecycleItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RecycleQuestionsAdapter recycleAdapter;
        public TextView tvTitle;
        public TextView tvScore;
        public TextView tvUser;
        public ImageView ivUser;

        public RecycleItemViewHolder(View itemView, RecycleQuestionsAdapter adapter) {
            super(itemView);
            recycleAdapter = adapter;
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvScore = (TextView) itemView.findViewById(R.id.tvScore);
            tvUser = (TextView) itemView.findViewById(R.id.tvUser);
            ivUser = (ImageView) itemView.findViewById(R.id.ivUser);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recycleAdapter.onItemHolderClick(this);
        }
    }
}