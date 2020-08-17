package com.yao.zhihudaily.ui.section

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yao.zhihudaily.R
import com.yao.zhihudaily.model.SectionStory
import com.yao.zhihudaily.tool.Constant
import com.yao.zhihudaily.tool.OnItemClickListener
import com.yao.zhihudaily.ui.NewsDetailActivity
import kotlinx.android.synthetic.main.item_section.view.*
import java.util.*

/**
 *
 * @author Yao
 * @date 2016/9/10
 */
class SectionStoryAdapter(private val mActivity: Activity) : RecyclerView.Adapter<SectionStoryAdapter.StoryHolder>() {
    private val mSectionStories = ArrayList<SectionStory>()

    private val mOnItemClickListener = object : OnItemClickListener() {
        override fun onItemClick(pos: Int) {
            val sectionStory = mSectionStories[pos]
            val intent = Intent(mActivity, NewsDetailActivity::class.java)
            intent.putExtra(Constant.ID, sectionStory.id)
            mActivity.startActivity(intent)
        }
    }

    fun addList(stories: ArrayList<SectionStory>) {
        this.mSectionStories.addAll(stories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_section_story_with_image, parent, false)
        val holder = StoryHolder(itemView)
        itemView.setOnClickListener(View.OnClickListener {
            mOnItemClickListener.onItemClick(holder.layoutPosition)
        })
        return holder
    }

    override fun onBindViewHolder(holder: StoryHolder, position: Int) {
        val sectionStory = mSectionStories[position]
        holder.tvTitle.text = sectionStory.title
        holder.tvDescription.text = sectionStory.date
        if (sectionStory.images != null) {
            Glide.with(mActivity).load(sectionStory.images!![0]).placeholder(R.mipmap.ic_launcher).into(holder.ivPic)
        }
    }

    override fun getItemCount(): Int {
        return mSectionStories.size
    }

    inner class StoryHolder(view: View) : RecyclerView.ViewHolder(view) {

        val ivPic: ImageView = view.iv_pic
        val tvTitle: TextView = view.tv_title
        val tvDescription: TextView = view.tv_description
    }


}
