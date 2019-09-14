package com.yao.zhihudaily.ui.hot;

import android.content.Intent;
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
import com.yao.zhihudaily.ui.BaseFragment;
import com.yao.zhihudaily.ui.NewsDetailActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Administrator
 * @date 2016/7/24
 */
public class HotAdapter extends RecyclerView.Adapter<HotAdapter.StoryHolder> {

    @NonNull
    private BaseFragment mFragment;
    private ArrayList<Hot> mHots = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int pos) {
            Hot hot = mHots.get(pos);
            Intent intent = new Intent(mFragment.getActivity(), NewsDetailActivity.class);
            intent.putExtra("id", hot.getNewsId());
            mFragment.getFragmentActivity().startActivity(intent);
        }
    };

    public HotAdapter(@NonNull BaseFragment fragment) {
        this.mFragment = fragment;
    }

    public HotAdapter(ArrayList<Hot> hots, @NonNull BaseFragment fragment) {
        this.mHots = hots;
        this.mFragment = fragment;
    }

    public void addList(ArrayList<Hot> stories) {
        this.mHots.addAll(stories);
    }


    @NonNull
    @Override
    public StoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot, parent, false));
    }

    @Override
    public void onBindViewHolder(StoryHolder holder, int position) {
        Hot hot = mHots.get(position);
        holder.tvTitle.setText(hot.getTitle());
        if (!TextUtils.isEmpty(hot.getThumbnail())) {
            Glide.with(mFragment).load(hot.getThumbnail()).into(holder.iv);
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(mOnItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mHots.size();
    }

    class StoryHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_splash)
        ImageView iv;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        public StoryHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }
    }


}
