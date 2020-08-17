package com.yao.zhihudaily.ui.theme

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
import com.yao.zhihudaily.model.Theme
import com.yao.zhihudaily.tool.Constant
import com.yao.zhihudaily.tool.OnItemClickListener
import com.yao.zhihudaily.ui.BaseFragment
import kotlinx.android.synthetic.main.item_section.view.iv_pic
import kotlinx.android.synthetic.main.item_section.view.tv_description
import kotlinx.android.synthetic.main.item_theme.view.*
import java.util.*

/**
 *
 * @author Yao
 * @date 2016/9/9
 */
class ThemeAdapter(private val fragment: BaseFragment) : RecyclerView.Adapter<ThemeAdapter.ThemeHolder>() {
    private val themes = ArrayList<Theme>()

    private val mOnItemClickListener = object : OnItemClickListener() {
        override fun onItemClick(pos: Int) {
            val theme = themes[pos]
            val intent = Intent(fragment.activity, ThemeActivity::class.java)
            intent.putExtra(Constant.ID, theme.id)
            fragment.fragmentActivity.startActivity(intent)
        }
    }

    fun addList(themes: ArrayList<Theme>) {
        this.themes.addAll(themes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_theme, parent, false)
        val holder = ThemeHolder(itemView)
        itemView.setOnClickListener(View.OnClickListener {
            mOnItemClickListener.onItemClick(holder.layoutPosition)
        })
        return holder
    }

    override fun onBindViewHolder(holder: ThemeHolder, position: Int) {
        val theme = themes[position]
        holder.tvName.text = theme.name
        holder.tvDescription.text = theme.description
        if (!TextUtils.isEmpty(theme.thumbnail)) {
            Glide.with(fragment).load(theme.thumbnail).into(holder.ivPic)
        }
    }

    override fun getItemCount(): Int {
        return themes.size
    }

    inner class ThemeHolder(view: View) : RecyclerView.ViewHolder(view) {

        val ivPic: ImageView = view.iv_pic
        val tvName: TextView = view.tv_name
        val tvDescription: TextView = view.tv_description
    }


}
