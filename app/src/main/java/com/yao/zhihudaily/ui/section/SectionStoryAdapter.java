package com.yao.zhihudaily.ui.section;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.SectionStory;
import com.yao.zhihudaily.tool.OnItemClickListener;
import com.yao.zhihudaily.ui.NewsDetailActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/10.
 */
public class SectionStoryAdapter extends RecyclerView.Adapter<SectionStoryAdapter.StoryHolder> {

    private Activity aty;
    private ArrayList<SectionStory> stories = new ArrayList<>();

    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(int pos) {
            SectionStory sectionStory = stories.get(pos);
            Intent intent = new Intent(aty, NewsDetailActivity.class);
            intent.putExtra("id", sectionStory.getId());
            aty.startActivity(intent);
        }
    };

    public SectionStoryAdapter(Activity aty) {
        this.aty = aty;
    }

    public void addList(ArrayList<SectionStory> stories) {
        this.stories.addAll(stories);
    }

    @Override
    public StoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section_story_with_image, parent, false));
    }

    @Override
    public void onBindViewHolder(StoryHolder holder, int position) {
        SectionStory sectionStory = stories.get(position);
        holder.tvTitle.setText(sectionStory.getTitle());
        holder.tvDescription.setText(sectionStory.getDate());
        if (sectionStory.getImages() != null) {
            Glide.with(aty).load(sectionStory.getImages().get(0)).placeholder(R.mipmap.ic_launcher).into(holder.iv);
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    class StoryHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv)
        ImageView iv;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvDescription)
        TextView tvDescription;

        public StoryHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }
    }


}
