package com.yao.zhihudaily.tool

import android.view.View

/**
 *
 * @author Yao
 * @date 2016/7/28
 */
abstract class OnItemClickListener : View.OnClickListener, View.OnLongClickListener {

    open fun onItemClick(pos: Int) {

    }

    fun onItemLongClick(pos: Int) {

    }

    override fun onClick(view: View) {
        val pos = view.tag as Int
        onItemClick(pos)
    }

    override fun onLongClick(view: View): Boolean {
        val pos = view.tag as Int
        onItemLongClick(pos)
        return false
    }
}
