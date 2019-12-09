package com.yao.zhihudaily.ui.daily

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.yao.zhihudaily.R
import com.yao.zhihudaily.model.Comment
import com.yao.zhihudaily.tool.GlideCircleTransform
import java.util.*

/**
 *
 * @author Yao
 * @date 2016/8/30
 */
class CommentAdapter(private val ctx: Context) : RecyclerView.Adapter<CommentAdapter.ShortCommentViewHolder>() {


    private val mComments = ArrayList<Comment>()
    private val mLayoutInflater: LayoutInflater
    private val mGlideCircleTransform: GlideCircleTransform

    init {
        mLayoutInflater = LayoutInflater.from(ctx)
        mGlideCircleTransform = GlideCircleTransform()
    }

    fun addList(comments: ArrayList<Comment>) {
        this.mComments.addAll(comments)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.ShortCommentViewHolder {
        return if (viewType == COMMENT) {
            ShortCommentViewHolder(mLayoutInflater.inflate(R.layout.item_short_comment, null), COMMENT)
        } else {
            ShortCommentViewHolder(mLayoutInflater.inflate(R.layout.item_short_comment_with_reply, null), COMMENT_WITH_REPLY)
        }
    }

    override fun onBindViewHolder(holder: CommentAdapter.ShortCommentViewHolder, position: Int) {
        val c = mComments[position]
        val target = object : BitmapImageViewTarget(holder.ivAvatar!!) {
            override fun setResource(resource: Bitmap?) {
                val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(ctx.resources, resource)
                circularBitmapDrawable.isCircular = true
                holder.ivAvatar!!.setImageDrawable(circularBitmapDrawable)
            }
        }
        Glide.with(ctx).asBitmap().load(c.avatar).centerCrop().into<BitmapImageViewTarget>(target)
        Glide.with(ctx).load(c.avatar).transform(mGlideCircleTransform).into(holder.ivAvatar!!)
        holder.tvAuthor!!.text = c.author
        holder.tvContent!!.text = c.content
        holder.tvTime!!.text = c.timeStr
        holder.tvLikes!!.text = c.likes.toString()
        if (c.replyTo != null) {
            val replyAuthor = "@" + c.replyTo!!.author + ": "
            if (holder.tvReplyAuthor != null) {
                holder.tvReplyAuthor!!.text = replyAuthor
            }
            if (holder.tvReplyContent != null) {
                holder.tvReplyContent!!.text = replyAuthor + c.replyTo!!.content
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mComments[position].replyTo == null) {
            COMMENT
        } else {
            COMMENT_WITH_REPLY
        }
    }

    override fun getItemCount(): Int {
        return mComments.size
    }

    inner class ShortCommentViewHolder(itemView: View, type: Int) : RecyclerView.ViewHolder(itemView) {

        @JvmField
        @BindView(R.id.iv_avatar)
        var ivAvatar: ImageView? = null
        @JvmField
        @BindView(R.id.tv_author)
        var tvAuthor: TextView? = null
        @JvmField
        @BindView(R.id.tvContent)
        var tvContent: TextView? = null
        @JvmField
        @BindView(R.id.tvTime)
        var tvTime: TextView? = null
        @JvmField
        @BindView(R.id.tvLikes)
        var tvLikes: TextView? = null
        @JvmField
        @BindView(R.id.tvReplyAuthor)
        var tvReplyAuthor: TextView? = null
        @JvmField
        @BindView(R.id.tvReplyContent)
        var tvReplyContent: TextView? = null

        init {
            ButterKnife.bind(this, itemView)
        }
    }

    companion object {

        private val COMMENT = 0
        private val COMMENT_WITH_REPLY = 1
    }
}
