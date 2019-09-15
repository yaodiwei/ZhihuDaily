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
import com.yao.zhihudaily.tool.Constant;
import com.yao.zhihudaily.tool.OnItemClickListener;
import com.yao.zhihudaily.ui.BaseFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * @author Yao
 * @date 2016/7/24
 */
public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionHolder> {

    private BaseFragment mBaseFragment;
    private ArrayList<Section> mSections = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int pos) {
            Section section = mSections.get(pos);
            Intent intent = new Intent(mBaseFragment.getActivity(), SectionActivity.class);
            intent.putExtra(Constant.ID, section.getId());
            intent.putExtra(Constant.NAME, section.getName());
            mBaseFragment.getFragmentActivity().startActivity(intent);
        }
    };

    public SectionAdapter(BaseFragment baseFragment) {
        this.mBaseFragment = baseFragment;
    }

    public SectionAdapter(ArrayList<Section> sections, BaseFragment baseFragment) {
        this.mSections = sections;
        this.mBaseFragment = baseFragment;
    }

    public void addList(ArrayList<Section> sections) {
        this.mSections.addAll(sections);
    }


    @NonNull
    @Override
    public SectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SectionHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false));
    }

    @Override
    public void onBindViewHolder(SectionHolder holder, int position) {
        Section section = mSections.get(position);
        holder.tvTitle.setText(section.getName());
        holder.tvDescription.setText(section.getDescription());
        if (!TextUtils.isEmpty(section.getThumbnail())) {
            Glide.with(mBaseFragment).load(section.getThumbnail()).into(holder.iv);
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(mOnItemClickListener);

    }

    @Override
    public int getItemCount() {
        return mSections.size();
    }

    class SectionHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_splash)
        ImageView iv;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_description)
        TextView tvDescription;

        public SectionHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }
    }


}
