package com.yao.zhihudaily.ui.daily

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yao.zhihudaily.R
import com.yao.zhihudaily.model.Daily
import com.yao.zhihudaily.tool.Constant
import com.yao.zhihudaily.tool.OnItemClickListener
import com.yao.zhihudaily.ui.BaseFragment
import com.yao.zhihudaily.ui.NewsDetailActivity
import java.util.*

/**
 * @author Yao
 * @date 2016/7/24
 */
class DailyAdapter : RecyclerView.Adapter<DailyAdapter.StoryHolder> {

    private var fragment: BaseFragment? = null
    private var stories = ArrayList<Daily>()
    private val listener = object : OnItemClickListener() {
        override fun onItemClick(pos: Int) {
            val daily = stories[pos]
            val intent = Intent(fragment!!.activity, NewsDetailActivity::class.java)
            intent.putExtra(Constant.ID, daily.id)
            fragment!!.fragmentActivity.startActivity(intent)
        }
    }

    constructor(fragment: BaseFragment) {
        this.fragment = fragment
    }

    constructor(stories: ArrayList<Daily>, fragment: BaseFragment) {
        this.stories = stories
        this.fragment = fragment
    }

    fun addList(stories: ArrayList<Daily>) {
        this.stories.addAll(stories)
    }

    fun addListToHeader(stories: ArrayList<Daily>) {
        this.stories.addAll(0, stories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryHolder {
        return StoryHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_daily, parent, false))
    }

    override fun onBindViewHolder(holder: StoryHolder, position: Int) {
        val daily = stories[position]
        holder.tvTitle.text = daily.title
        if (daily.images != null && daily.images!!.size != 0) {
            Glide.with(fragment!!).load(daily.images!![0]).into(holder.iv)
        } else {
            Glide.with(fragment!!).load(daily.image).into(holder.iv)
        }
        holder.itemView.tag = position
        holder.itemView.setOnClickListener(listener)

    }

    override fun getItemCount(): Int {
        return stories.size
    }

    inner class StoryHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv: ImageView
        var tvTitle: TextView

        init {
            iv = view.findViewById(R.id.iv_splash)
            tvTitle = view.findViewById(R.id.tv_title)
        }
    }


}
