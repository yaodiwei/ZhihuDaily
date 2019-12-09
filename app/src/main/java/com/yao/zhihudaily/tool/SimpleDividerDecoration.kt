package com.yao.zhihudaily.tool

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.yao.zhihudaily.R

/**
 *
 * @author Yao
 * @date 2017/3/29
 */
class SimpleDividerDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val dividerHeight: Int
    private val dividerPaint: Paint

    init {
        dividerPaint = Paint()
        dividerPaint.color = context.resources.getColor(R.color.item_decoration)
        dividerHeight = context.resources.getDimensionPixelSize(R.dimen.divider_height)
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = dividerHeight
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val left = parent.paddingStart
        val right = parent.width - parent.paddingEnd

        for (i in 0 until childCount - 1) {
            val view = parent.getChildAt(i)
            val top = view.bottom.toFloat()
            val bottom = (view.bottom + dividerHeight).toFloat()
            canvas.drawRect(left.toFloat(), top, right.toFloat(), bottom, dividerPaint)
        }
    }
}
