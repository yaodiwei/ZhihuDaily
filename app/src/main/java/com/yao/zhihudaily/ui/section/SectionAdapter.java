package com.yao.zhihudaily.ui.section;

import android.content.Intent;
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

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/24.
 */
public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionHolder> {

    private Fragment fragment;
    private ArrayList<Section> sections = new ArrayList<>();
    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(int pos) {
            Section section = sections.get(pos);
            Intent intent = new Intent(fragment.getActivity(), SectionActivity.class);
            intent.putExtra("id", section.getId());
            intent.putExtra("name", section.getName());
            fragment.getActivity().startActivity(intent);
        }
    };

    public SectionAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public SectionAdapter(ArrayList<Section> sections, Fragment fragment) {
        this.sections = sections;
        this.fragment = fragment;
    }

    public void addList(ArrayList<Section> sections) {
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

        @BindView(R.id.iv)
        ImageView iv;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvDescription)
        TextView tvDescription;

        public SectionHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }
    }


}
