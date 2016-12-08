package com.mobile.persson.mobleeapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mobile.persson.mobleeapp.R;
import com.mobile.persson.mobleeapp.database.models.AnswerItemModel;
import com.mobile.persson.mobleeapp.database.models.SearchItemModel;

import java.util.List;

/**
 * Created by persson on 08/12/16.
 */

public class RecycleAnswersAdapter extends RecyclerView.Adapter<RecycleAnswersAdapter.RecycleItemViewHolder> {
    private List<AnswerItemModel> answerList;
    private Context context;
    private AdapterView.OnItemClickListener itemClickListener;

    public RecycleAnswersAdapter(Context c, List<AnswerItemModel> content) {
        answerList = content;
        context = c;
    }

    @Override
    public RecycleItemViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_answer_list, parent, false);
        return new RecycleItemViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(RecycleItemViewHolder holder, int position) {
        String htmlAsString = answerList.get(position).getBody();
        Spanned htmlAsSpanned = Html.fromHtml(htmlAsString);
        holder.tvAnswer.setText(htmlAsSpanned);
        holder.tvUser.setText(String.valueOf(answerList.get(position).getOwner().getDisplay_name()));

        Glide.with(holder.ivUser.getContext())
                .load(answerList.get(position).getOwner().getProfile_image())
                .into(holder.ivUser);
    }

    @Override
    public int getItemCount() {
        return answerList.size();
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
        public RecycleAnswersAdapter recycleAdapter;
        public TextView tvAnswer;
        public TextView tvUser;
        public ImageView ivUser;

        public RecycleItemViewHolder(View itemView, RecycleAnswersAdapter adapter) {
            super(itemView);
            recycleAdapter = adapter;
            tvAnswer = (TextView) itemView.findViewById(R.id.tvAnswer);
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