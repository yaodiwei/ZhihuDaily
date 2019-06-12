package com.yao.zhihudaily.tool;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Administrator on 2016/9/17.
 */
public abstract class RecyclerViewOnLoadMoreListener extends RecyclerView.OnScrollListener {

    private int lastVisibleItemPosition;
    private int visibleItemCount;
    private int totalItemCount;
    private boolean isLoading;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        visibleItemCount = layoutManager.getChildCount();
        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        totalItemCount = layoutManager.getItemCount();
        //当
        //1.有数据
        //2.滚动是闲置状态
        //3.滚到最后一个Item
        //4.不处于加载更多的状态
        if (visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE
                && lastVisibleItemPosition >= totalItemCount - 1 && !isLoading) {
            onLoadMore();
            isLoading = true;
        }
    }

    public abstract void onLoadMore();

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public boolean getLoading() {
        return isLoading;
    }

}
