package com.yao.zhihudaily.ui.theme

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
import com.yao.zhihudaily.model.Theme
import com.yao.zhihudaily.tool.Constant
import com.yao.zhihudaily.tool.OnItemClickListener
import com.yao.zhihudaily.ui.BaseFragment
import java.util.*

/**
 *
 * @author Yao
 * @date 2016/9/9
 */
class ThemeAdapter(private val fragment: BaseFragment) : RecyclerView.Adapter<ThemeAdapter.ThemeHolder>() {
    private val themes = ArrayList<Theme>()

    private val listener = object : OnItemClickListener() {
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
        return ThemeHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_theme, parent, false))
    }

    override fun onBindViewHolder(holder: ThemeHolder, position: Int) {
        val theme = themes[position]
        holder.tvName!!.text = theme.name
        holder.tvDescription!!.text = theme.description
        if (!TextUtils.isEmpty(theme.thumbnail)) {
            Glide.with(fragment).load(theme.thumbnail).into(holder.iv!!)
        }
        holder.itemView.tag = position
        holder.itemView.setOnClickListener(listener)

    }

    override fun getItemCount(): Int {
        return themes.size
    }

    inner class ThemeHolder(view: View) : RecyclerView.ViewHolder(view) {

        @JvmField
        @BindView(R.id.iv_splash)
        var iv: ImageView? = null
        @JvmField
        @BindView(R.id.tvName)
        var tvName: TextView? = null
        @JvmField
        @BindView(R.id.tv_description)
        var tvDescription: TextView? = null

        init {
            ButterKnife.bind(this, itemView)
        }
    }


}
