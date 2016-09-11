package com.yao.zhihudaily.ui.theme;

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
import com.yao.zhihudaily.model.Theme;
import com.yao.zhihudaily.tool.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ThemeHolder> {

    private Fragment fragment;
    private List<Theme> themes = new ArrayList<>();

    public ThemeAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public void addList(List<Theme> themes) {
        this.themes.addAll(themes);
    }

    @Override
    public ThemeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ThemeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theme, parent, false));
    }

    @Override
    public void onBindViewHolder(ThemeHolder holder, int position) {
        Theme theme = themes.get(position);
        holder.tvName.setText(theme.getName());
        holder.tvDescription.setText(theme.getDescription());
        if (!TextUtils.isEmpty(theme.getThumbnail())) {
            Glide.with(fragment).load(theme.getThumbnail()).into(holder.iv);
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return themes.size();
    }

    class ThemeHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tvName;
        TextView tvDescription;

        public ThemeHolder(View view) {
            super(view);
            iv = (ImageView) view.findViewById(R.id.iv);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        }
    }

    private OnItemClickListener listener = new OnItemClickListener(){
        @Override
        public void onItemClick(int pos) {
            Theme theme = themes.get(pos);
            Intent intent = new Intent(fragment.getActivity(), ThemeActivity.class);
            intent.putExtra("id", theme.getId());
            fragment.getActivity().startActivity(intent);
        }
    };


}
