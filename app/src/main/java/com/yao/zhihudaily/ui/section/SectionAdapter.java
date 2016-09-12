package com.yao.zhihudaily.ui.section;

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
import com.yao.zhihudaily.model.Section;
import com.yao.zhihudaily.tool.OnItemClickListener;
import com.yao.zhihudaily.ui.NewsDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/24.
 */
public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionHolder> {

    private Fragment fragment;
    private List<Section> sections = new ArrayList<>();
    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(int pos) {
            Section section = sections.get(pos);
            Intent intent = new Intent(fragment.getActivity(), NewsDetailActivity.class);
            intent.putExtra("id", section.getId());
            fragment.getActivity().startActivity(intent);
        }
    };

    public SectionAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public SectionAdapter(List<Section> sections, Fragment fragment) {
        this.sections = sections;
        this.fragment = fragment;
    }

    public void addList(List<Section> sections) {
        this.sections.addAll(sections);
    }


    @Override
    public SectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SectionHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false));
    }

    @Override
    public void onBindViewHolder(SectionHolder holder, int position) {
        Section section = sections.get(position);
        holder.tvTitle.setText(section.getName());
        holder.tvDescription.setText(section.getDescription());
        if (!TextUtils.isEmpty(section.getThumbnail())) {
            Glide.with(fragment).load(section.getThumbnail()).into(holder.iv);
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return sections.size();
    }

    class SectionHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tvTitle;
        TextView tvDescription;

        public SectionHolder(View view) {
            super(view);
            iv = (ImageView) view.findViewById(R.id.iv);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        }
    }


}
