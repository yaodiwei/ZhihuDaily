package com.yao.zhihudaily.ui.hot;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.Hot;
import com.yao.zhihudaily.tool.OnItemClickListener;
import com.yao.zhihudaily.ui.NewsDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/24.
 */
public class HotAdapter extends RecyclerView.Adapter<HotAdapter.StroyHolder> {

    private Fragment fragment;
    private List<Hot> stories = new ArrayList<>();
    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(int pos) {
            Hot hot = stories.get(pos);
            Intent intent = new Intent(fragment.getActivity(), NewsDetailActivity.class);
            intent.putExtra("id", hot.getNewsId());
            fragment.getActivity().startActivity(intent);
        }
    };

    public HotAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public HotAdapter(List<Hot> stories, Fragment fragment) {
        this.stories = stories;
        this.fragment = fragment;
    }

    public void addList(List<Hot> stories) {
        this.stories.addAll(stories);
    }


    @Override
    public StroyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StroyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot, parent, false));
    }

    @Override
    public void onBindViewHolder(StroyHolder holder, int position) {
        Hot hot = stories.get(position);
        holder.tvTitle.setText(hot.getTitle());
        if (!TextUtils.isEmpty(hot.getThumbnail())) {
            Glide.with(fragment).load(hot.getThumbnail()).into(holder.iv);
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


}
