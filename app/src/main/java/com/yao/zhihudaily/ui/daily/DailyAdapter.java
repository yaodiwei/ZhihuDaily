package com.yao.zhihudaily.ui.daily;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.Daily;
import com.yao.zhihudaily.tool.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/24.
 */
public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.StroyHolder> {

    private Fragment fragment;
    private List<Daily> stories = new ArrayList<>();

    public DailyAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public DailyAdapter(List<Daily> stories, Fragment fragment) {
        this.stories = stories;
        this.fragment = fragment;
    }

    public void addList(List<Daily> stories) {
        this.stories.addAll(stories);
    }

    public void addListToHeader(List<Daily> stories) {
        this.stories.addAll(0, stories);
    }

    @Override
    public StroyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StroyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_story, parent, false));
    }

    @Override
    public void onBindViewHolder(StroyHolder holder, int position) {
        Daily daily = stories.get(position);
        holder.tvTitle.setText(daily.getTitle());
        if (daily.getImages().size() != 0) {
            Glide.with(fragment).load(daily.getImages().get(0)).into(holder.iv);
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    class StroyHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tvTitle;

        public StroyHolder(View view) {
            super(view);
            iv = (ImageView) view.findViewById(R.id.iv);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        }
    }

    private OnItemClickListener listener = new OnItemClickListener(){
        @Override
        public void onItemClick(int pos) {
            Daily daily = stories.get(pos);
            Intent intent = new Intent(fragment.getActivity(), DailyDetailActivity.class);
            intent.putExtra("daily", daily);
            fragment.getActivity().startActivity(intent);
        }
    };


}
