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

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/16.
 */
public class RecommenderAdapter extends RecyclerView.Adapter<RecommenderAdapter.RecommenderHolder> {

    private Activity aty;
    private ArrayList<Recommender> recommenders = new ArrayList<>();

    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(int pos) {
            Recommender recommender = recommenders.get(pos);
            Intent intent = new Intent(aty, ProfilePageActivity.class);
            intent.putExtra("id", recommender.getId());
            intent.putExtra("name", recommender.getName());
            aty.startActivity(intent);
        }
    };

    public RecommenderAdapter(Activity aty) {
        this.aty = aty;
    }

    public void addList(ArrayList<Recommender> recommenders) {
        this.recommenders.addAll(recommenders);
    }

    @Override
    public RecommenderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecommenderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommender, parent, false));
    }

    @Override
    public void onBindViewHolder(RecommenderHolder holder, int position) {
        Recommender recommender = recommenders.get(position);
        holder.tvName.setText(recommender.getName());
        holder.tvBio.setText(recommender.getBio());
        if (recommender.getAvatar() != null) {
            Glide.with(aty).load(recommender.getAvatar()).transform(new GlideCircleTransform()).into(holder.iv);
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return recommenders.size();
    }

    class RecommenderHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv)
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
