package com.mobile.persson.mobleeapp.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.mobile.persson.mobleeapp.R;

import java.util.List;

/**
 * Created by persson on 07/12/16.
 */

public class RecycleTagsAdapter extends RecyclerView.Adapter<RecycleTagsAdapter.GridItemViewHolder> {
    private List<String> tagsList;
    private Context context;
    private AdapterView.OnItemClickListener itemClickListener;

    public RecycleTagsAdapter(Context c, List<String> content) {
        tagsList = content;
        context = c;
    }

    @Override
    public GridItemViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_tag_list, parent, false);
        return new GridItemViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(GridItemViewHolder holder, int position) {
        holder.tvTag.setText(tagsList.get(position));

        if (holder.tvTag.getText() == "android" ||
                holder.tvTag.getText() == "java" ||
                holder.tvTag.getText() == "android-studio" ||
                holder.tvTag.getText() == "marshmallow" ||
                holder.tvTag.getText() == "nexus") {
            holder.tvTag.setTypeface(Typeface.DEFAULT_BOLD);
            holder.tvTag.setTextSize(18);
        }
    }

    @Override
    public int getItemCount() {
        return tagsList.size();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.itemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(GridItemViewHolder itemHolder) {
        if (itemClickListener != null) {
            itemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    public class GridItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvTag;
        public RecycleTagsAdapter recycleAdapter;

        public GridItemViewHolder(View itemView, RecycleTagsAdapter adapter) {
            super(itemView);
            recycleAdapter = adapter;
            tvTag = (TextView) itemView.findViewById(R.id.tvTag);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recycleAdapter.onItemHolderClick(this);
        }
    }
}