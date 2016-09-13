package com.yao.zhihudaily.ui.theme;

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
import com.yao.zhihudaily.model.ThemeStory;
import com.yao.zhihudaily.tool.OnItemClickListener;
import com.yao.zhihudaily.ui.NewsDetailActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/10.
 */
public class ThemeStoryAdapter extends RecyclerView.Adapter<ThemeStoryAdapter.StoryHolder> {

    private static final int STORY = 0;
    private static final int STORY_WITH_IMAGE = 1;

    private Activity aty;
    private ArrayList<ThemeStory> stories = new ArrayList<>();
    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(int pos) {
            ThemeStory themeStory = stories.get(pos);
            Intent intent = new Intent(aty, NewsDetailActivity.class);
            intent.putExtra("id", themeStory.getId());
            intent.putExtra("id", themeStory.getId());
            aty.startActivity(intent);
        }
    };

    public ThemeStoryAdapter(Activity aty) {
        this.aty = aty;
    }

    public void addList(ArrayList<ThemeStory> stories) {
        this.stories.addAll(stories);
    }

    @Override
    public StoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == STORY) {
            return new StoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theme_story, parent, false), STORY);
        } else {
            return new StoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theme_story_with_image, parent, false), STORY_WITH_IMAGE);
        }
    }

    @Override
    public void onBindViewHolder(StoryHolder holder, int position) {
        ThemeStory themeStory = stories.get(position);
        holder.tvTitle.setText(themeStory.getTitle());
        if (themeStory.getImages() != null) {
            Glide.with(aty).load(themeStory.getImages().get(0)).placeholder(R.mipmap.ic_launcher).into(holder.iv);
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(listener);

    }

    @Override
    public int getItemViewType(int position) {
        ThemeStory themeStory = stories.get(position);
        if (themeStory.getImages() == null) {
            return STORY;
        } else {
            return STORY_WITH_IMAGE;
        }
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    class StoryHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tvTitle;

        public StoryHolder(View view, int type) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            if (type == 1) {
                iv = (ImageView) view.findViewById(R.id.iv);
            }
        }
    }


}
