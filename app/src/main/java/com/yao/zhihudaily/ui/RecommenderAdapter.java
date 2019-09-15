package com.yao.zhihudaily.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.Recommender;
import com.yao.zhihudaily.tool.GlideCircleTransform;
import com.yao.zhihudaily.tool.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * @author Yao
 * @date 2016/9/16
 */
public class RecommenderAdapter extends RecyclerView.Adapter<RecommenderAdapter.RecommenderHolder> {

    private Activity mActivity;
    private List<Recommender> mRecommenderList = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int pos) {
            Recommender recommender = mRecommenderList.get(pos);
            Intent intent = new Intent(mActivity, ProfilePageActivity.class);
            intent.putExtra("id", recommender.getId());
            intent.putExtra("name", recommender.getName());
            mActivity.startActivity(intent);
        }
    };

    public RecommenderAdapter(Activity activity) {
        this.mActivity = activity;
    }

    public void addList(ArrayList<Recommender> recommenders) {
        this.mRecommenderList.addAll(recommenders);
    }

    @NonNull
    @Override
    public RecommenderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecommenderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommender, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecommenderHolder holder, int position) {
        Recommender recommender = mRecommenderList.get(position);
        holder.tvName.setText(recommender.getName());
        holder.tvBio.setText(recommender.getBio());
        if (recommender.getAvatar() != null) {
            Glide.with(mActivity).load(recommender.getAvatar()).transform(new GlideCircleTransform()).into(holder.iv);
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(mOnItemClickListener);

    }

    @Override
    public int getItemCount() {
        return mRecommenderList.size();
    }

    class RecommenderHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_splash)
        ImageView iv;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvBio)
        TextView tvBio;

        public RecommenderHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
