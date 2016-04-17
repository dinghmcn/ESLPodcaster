package com.qingwenwei.eslpodcaster.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingwenwei.eslpodcaster.R;
import com.qingwenwei.eslpodcaster.entity.PodcastEpisode;

import java.util.List;

public class PodcastEpisodeRecyclerViewAdapter extends RecyclerView.Adapter<PodcastEpisodeRecyclerViewAdapter.ViewHolder>{
    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<PodcastEpisode> mValues;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String mBoundString;

        public final View mView;
        public final TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTextView = (TextView) view.findViewById(R.id.titleTextView2);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText();
        }
    }

    public PodcastEpisodeRecyclerViewAdapter(Context context, List<PodcastEpisode> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
    }

    @Override
    public PodcastEpisodeRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.podcast_episode_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PodcastEpisodeRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mBoundString = mValues.get(position).getTitle();
        holder.mTextView.setText(mValues.get(position).getTitle());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Context context = v.getContext();
//                Intent intent = new Intent(context, CheeseDetailActivity.class);
//                intent.putExtra(CheeseDetailActivity.EXTRA_NAME, holder.mBoundString);
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}