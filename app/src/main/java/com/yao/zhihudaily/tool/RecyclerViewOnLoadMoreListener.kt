package com.yao.zhihudaily.tool


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * @author Yao
 * @date 2016/9/17
 */
abstract class RecyclerViewOnLoadMoreListener : RecyclerView.OnScrollListener() {

    private var lastVisibleItemPosition: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    var loading: Boolean = false

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager != null) {
            visibleItemCount = layoutManager.childCount
            lastVisibleItemPosition = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            totalItemCount = layoutManager.itemCount
        }
        //当
        //1.有数据
        //2.滚动是闲置状态
        //3.滚到最后一个Item
        //4.不处于加载更多的状态
        if (visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE
                && lastVisibleItemPosition >= totalItemCount - 1 && !loading) {
            onLoadMore()
            loading = true
        }
    }

    abstract fun onLoadMore()

}
