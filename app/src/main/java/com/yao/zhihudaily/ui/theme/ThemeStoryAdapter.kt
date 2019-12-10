package com.yao.zhihudaily.ui.theme

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
import com.yao.zhihudaily.model.ThemeStory
import com.yao.zhihudaily.tool.Constant
import com.yao.zhihudaily.tool.OnItemClickListener
import com.yao.zhihudaily.ui.NewsDetailActivity
import kotlinx.android.synthetic.main.item_theme_story_with_image.view.*
import java.util.*

/**
 *
 * @author Yao
 * @date 2016/9/10
 */
class ThemeStoryAdapter(private val aty: Activity) : RecyclerView.Adapter<ThemeStoryAdapter.StoryHolder>() {
    private val stories = ArrayList<ThemeStory>()
    private val listener = object : OnItemClickListener() {
        override fun onItemClick(pos: Int) {
            val themeStory = stories[pos]
            val intent = Intent(aty, NewsDetailActivity::class.java)
            intent.putExtra(Constant.ID, themeStory.id)
            intent.putExtra(Constant.ID, themeStory.id)
            aty.startActivity(intent)
        }
    }

    fun addList(stories: ArrayList<ThemeStory>) {
        this.stories.addAll(stories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryHolder {
        return if (viewType == STORY) {
            StoryHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_theme_story, parent, false), STORY)
        } else {
            StoryHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_theme_story_with_image, parent, false), STORY_WITH_IMAGE)
        }
    }

    override fun onBindViewHolder(holder: StoryHolder, position: Int) {
        val themeStory = stories[position]
        holder.tvTitle.text = themeStory.title
        if (themeStory.images != null) {
            Glide.with(aty).load(themeStory.images!![0]).placeholder(R.mipmap.ic_launcher).into(holder.ivPic)
        }
        holder.itemView.tag = position
        holder.itemView.setOnClickListener(listener)

    }

    override fun getItemViewType(position: Int): Int {
        val themeStory = stories[position]
        return if (themeStory.images == null) {
            STORY
        } else {
            STORY_WITH_IMAGE
        }
    }

    override fun getItemCount(): Int {
        return stories.size
    }

    inner class StoryHolder(view: View, type: Int) : RecyclerView.ViewHolder(view) {

        val ivPic: ImageView = view.iv_pic
        val tvTitle: TextView = view.tv_title
    }

    companion object {

        private val STORY = 0
        private val STORY_WITH_IMAGE = 1
    }


}
