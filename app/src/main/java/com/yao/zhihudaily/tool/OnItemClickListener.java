package com.yao.zhihudaily.tool;

import android.view.View;

/**
 *
 * @author Yao
 * @date 2016/7/28
 */
public abstract class OnItemClickListener implements View.OnClickListener, View.OnLongClickListener{

    public void onItemClick(int pos) {

    }

    public void onItemLongClick(int pos) {

    }

    @Override
    public void onClick(View view) {
        int pos = (int) view.getTag();
        onItemClick(pos);
    }

    @Override
    public boolean onLongClick(View view) {
        int pos = (int) view.getTag();
        onItemLongClick(pos);
        return false;
    }
}
