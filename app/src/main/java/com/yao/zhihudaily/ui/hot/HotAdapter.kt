package com.yao.zhihudaily.ui.hot

import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yao.zhihudaily.R
import com.yao.zhihudaily.model.Hot
import com.yao.zhihudaily.tool.Constant
import com.yao.zhihudaily.tool.OnItemClickListener
import com.yao.zhihudaily.ui.BaseFragment
import com.yao.zhihudaily.ui.NewsDetailActivity
import kotlinx.android.synthetic.main.item_hot.view.*
import java.util.*

/**
 * @author Yao
 * @date 2016/7/24
 */
class HotAdapter : RecyclerView.Adapter<HotAdapter.StoryHolder> {

    private lateinit var mFragment: BaseFragment
    private var mHots = ArrayList<Hot>()
    private val mOnItemClickListener = object : OnItemClickListener() {
        override fun onItemClick(pos: Int) {
            val hot = mHots[pos]
            val intent = Intent(mFragment.activity, NewsDetailActivity::class.java)
            intent.putExtra(Constant.ID, hot.newsId)
            mFragment.fragmentActivity.startActivity(intent)
        }
    }

    constructor(fragment: BaseFragment) {
        this.mFragment = fragment
    }

    constructor(hots: ArrayList<Hot>, fragment: BaseFragment) {
        this.mHots = hots
        this.mFragment = fragment
    }

    fun addList(stories: ArrayList<Hot>) {
        this.mHots.addAll(stories)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_hot, parent, false)
        val holder = StoryHolder(itemView)
        itemView.setOnClickListener(View.OnClickListener {
            mOnItemClickListener.onItemClick(holder.layoutPosition)
        })
        return holder
    }

    override fun onBindViewHolder(holder: StoryHolder, position: Int) {
        val hot = mHots[position]
        holder.tvTitle!!.text = hot.title
        if (!TextUtils.isEmpty(hot.thumbnail)) {
            Glide.with(mFragment).load(hot.thumbnail).into(holder.ivPic!!)
        }
    }

    override fun getItemCount(): Int {
        return mHots.size
    }

    inner class StoryHolder(view: View) : RecyclerView.ViewHolder(view) {

        val ivPic: ImageView = view.iv_pic
        val tvTitle: TextView = view.tv_title
    }


}
