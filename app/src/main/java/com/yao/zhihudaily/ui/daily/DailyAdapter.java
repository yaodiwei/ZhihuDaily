package com.yao.zhihudaily.ui.daily;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.Daily;
import com.yao.zhihudaily.tool.OnItemClickListener;
import com.yao.zhihudaily.ui.BaseFragment;
import com.yao.zhihudaily.ui.NewsDetailActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 *
 * @author Yao
 * @date 2016/7/24
 */
public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.StoryHolder> {

    private BaseFragment fragment;
    private ArrayList<Daily> stories = new ArrayList<>();
    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(int pos) {
            Daily daily = stories.get(pos);
            Intent intent = new Intent(fragment.getActivity(), NewsDetailActivity.class);
            intent.putExtra("id", daily.getId());
            fragment.getFragmentActivity().startActivity(intent);
        }
    };

    public DailyAdapter(BaseFragment fragment) {
        this.fragment = fragment;
    }

    public DailyAdapter(ArrayList<Daily> stories, BaseFragment fragment) {
        this.stories = stories;
        this.fragment = fragment;
    }

    public void addList(ArrayList<Daily> stories) {
        this.stories.addAll(stories);
    }

    public void addListToHeader(ArrayList<Daily> stories) {
        this.stories.addAll(0, stories);
    }

    @NonNull
    @Override
    public StoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily, parent, false));
    }

    @Override
    public void onBindViewHolder(StoryHolder holder, int position) {
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

    class StoryHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tvTitle;

        public StoryHolder(View view) {
            super(view);
            iv = view.findViewById(R.id.iv_splash);
            tvTitle = view.findViewById(R.id.tv_title);
        }
    }


}
