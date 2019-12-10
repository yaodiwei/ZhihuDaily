package com.yao.zhihudaily.ui.section

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
import com.yao.zhihudaily.model.Section
import com.yao.zhihudaily.tool.Constant
import com.yao.zhihudaily.tool.OnItemClickListener
import com.yao.zhihudaily.ui.BaseFragment
import kotlinx.android.synthetic.main.item_section.view.*
import java.util.*

/**
 *
 * @author Yao
 * @date 2016/7/24
 */
class SectionAdapter : RecyclerView.Adapter<SectionAdapter.SectionHolder> {

    private var mBaseFragment: BaseFragment? = null
    private var mSections = ArrayList<Section>()
    private val mOnItemClickListener = object : OnItemClickListener() {
        override fun onItemClick(pos: Int) {
            val section = mSections[pos]
            val intent = Intent(mBaseFragment!!.activity, SectionActivity::class.java)
            intent.putExtra(Constant.ID, section.id)
            intent.putExtra(Constant.NAME, section.name)
            mBaseFragment!!.fragmentActivity.startActivity(intent)
        }
    }

    constructor(baseFragment: BaseFragment) {
        this.mBaseFragment = baseFragment
    }

    constructor(sections: ArrayList<Section>, baseFragment: BaseFragment) {
        this.mSections = sections
        this.mBaseFragment = baseFragment
    }

    fun addList(sections: ArrayList<Section>) {
        this.mSections.addAll(sections)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionHolder {
        return SectionHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_section, parent, false))
    }

    override fun onBindViewHolder(holder: SectionHolder, position: Int) {
        val section = mSections[position]
        holder.tvTitle!!.text = section.name
        holder.tvDescription!!.text = section.description
        if (!TextUtils.isEmpty(section.thumbnail)) {
            Glide.with(mBaseFragment!!).load(section.thumbnail).into(holder.ivPic!!)
        }
        holder.itemView.tag = position
        holder.itemView.setOnClickListener(mOnItemClickListener)

    }

    override fun getItemCount(): Int {
        return mSections.size
    }

    inner class SectionHolder(view: View) : RecyclerView.ViewHolder(view) {

        val ivPic: ImageView = view.iv_pic
        val tvTitle: TextView = view.tv_title
        val tvDescription: TextView = view.tv_description
    }


}
