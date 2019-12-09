package com.yao.zhihudaily.ui

import android.app.Activity
import android.content.Intent
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
import com.yao.zhihudaily.model.Recommender
import com.yao.zhihudaily.tool.Constant
import com.yao.zhihudaily.tool.GlideCircleTransform
import com.yao.zhihudaily.tool.OnItemClickListener
import java.util.*

/**
 *
 * @author Yao
 * @date 2016/9/16
 */
class RecommenderAdapter(private val mActivity: Activity) : RecyclerView.Adapter<RecommenderAdapter.RecommenderHolder>() {
    private val mRecommenderList = ArrayList<Recommender>()

    private val mOnItemClickListener = object : OnItemClickListener() {
        override fun onItemClick(pos: Int) {
            val recommender = mRecommenderList[pos]
            val intent = Intent(mActivity, ProfilePageActivity::class.java)
            intent.putExtra(Constant.ID, recommender.id)
            intent.putExtra(Constant.NAME, recommender.name)
            mActivity.startActivity(intent)
        }
    }

    fun addList(recommenders: ArrayList<Recommender>) {
        this.mRecommenderList.addAll(recommenders)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommenderHolder {
        return RecommenderHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recommender, parent, false))
    }

    override fun onBindViewHolder(holder: RecommenderHolder, position: Int) {
        val recommender = mRecommenderList[position]
        holder.tvName!!.text = recommender.name
        holder.tvBio!!.text = recommender.bio
        if (recommender.avatar != null) {
            Glide.with(mActivity).load(recommender.avatar).transform(GlideCircleTransform()).into(holder.iv!!)
        }
        holder.itemView.tag = position
        holder.itemView.setOnClickListener(mOnItemClickListener)

    }

    override fun getItemCount(): Int {
        return mRecommenderList.size
    }

    class RecommenderHolder(view: View) : RecyclerView.ViewHolder(view) {

        @JvmField
        @BindView(R.id.iv_splash)
        var iv: ImageView? = null
        @JvmField
        @BindView(R.id.tvName)
        var tvName: TextView? = null
        @JvmField
        @BindView(R.id.tvBio)
        var tvBio: TextView? = null

        init {
            ButterKnife.bind(this, view)
        }
    }


}
