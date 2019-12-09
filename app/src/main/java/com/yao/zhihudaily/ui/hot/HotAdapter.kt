package com.yao.zhihudaily.ui.hot

import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.yao.zhihudaily.R
import com.yao.zhihudaily.model.Hot
import com.yao.zhihudaily.tool.Constant
import com.yao.zhihudaily.tool.OnItemClickListener
import com.yao.zhihudaily.ui.BaseFragment
import com.yao.zhihudaily.ui.NewsDetailActivity
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
        return StoryHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_hot, parent, false))
    }

    override fun onBindViewHolder(holder: StoryHolder, position: Int) {
        val hot = mHots[position]
        holder.tvTitle!!.text = hot.title
        if (!TextUtils.isEmpty(hot.thumbnail)) {
            Glide.with(mFragment).load(hot.thumbnail).into(holder.iv!!)
        }
        holder.itemView.tag = position
        holder.itemView.setOnClickListener(mOnItemClickListener)
    }

    override fun getItemCount(): Int {
        return mHots.size
    }

    inner class StoryHolder(view: View) : RecyclerView.ViewHolder(view) {

        @JvmField
        @BindView(R.id.iv_splash)
        var iv: ImageView? = null
        @JvmField
        @BindView(R.id.tv_title)
        var tvTitle: TextView? = null

        init {
            ButterKnife.bind(this, itemView)
        }
    }


}
