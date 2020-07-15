package com.yao.zhihudaily.ui.daily

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.yao.zhihudaily.R
import com.yao.zhihudaily.model.Comment
import kotlinx.android.synthetic.main.item_short_comment_with_reply.view.*
import java.util.*

/**
 *
 * @author Yao
 * @date 2016/8/30
 */
class CommentAdapter(private val ctx: Context) : RecyclerView.Adapter<CommentAdapter.ShortCommentViewHolder>() {


    private val mComments = ArrayList<Comment>()
    private val mLayoutInflater: LayoutInflater
    private val mCircleCrop: CircleCrop

    init {
        mLayoutInflater = LayoutInflater.from(ctx)
        mCircleCrop = CircleCrop()
    }

    fun addList(comments: ArrayList<Comment>) {
        this.mComments.addAll(comments)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortCommentViewHolder {
        return if (viewType == COMMENT) {
            ShortCommentViewHolder(mLayoutInflater.inflate(R.layout.item_short_comment, null), COMMENT)
        } else {
            ShortCommentViewHolder(mLayoutInflater.inflate(R.layout.item_short_comment_with_reply, null), COMMENT_WITH_REPLY)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ShortCommentViewHolder, position: Int) {
        val c = mComments[position]
        val target = object : BitmapImageViewTarget(holder.ivAvatar) {
            override fun setResource(resource: Bitmap?) {
                val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(ctx.resources, resource)
                circularBitmapDrawable.isCircular = true
                holder.ivAvatar.setImageDrawable(circularBitmapDrawable)
            }
        }
        Glide.with(ctx).asBitmap().load(c.avatar).centerCrop().into<BitmapImageViewTarget>(target)
        Glide.with(ctx).load(c.avatar).transform(mCircleCrop).into(holder.ivAvatar)
        holder.tvAuthor.text = c.author
        holder.tvContent.text = c.content
        holder.tvTime.text = c.timeStr
        holder.tvLikes.text = c.likes.toString()
        if (c.replyTo != null) {
            val replyAuthor = "@" + c.replyTo?.author + ": "
            holder.tvReplyAuthor.text = replyAuthor
            holder.tvReplyContent.text = replyAuthor + c.replyTo?.content
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

    inner class ShortCommentViewHolder(view: View, type: Int) : RecyclerView.ViewHolder(view) {

        val ivAvatar: ImageView = view.iv_avatar
        val tvAuthor: TextView = view.tv_author
        val tvContent: TextView = view.tvContent
        val tvTime: TextView = view.tvTime
        val tvLikes: TextView = view.tvLikes
        val tvReplyAuthor: TextView = view.tvReplyAuthor
        val tvReplyContent: TextView = view.tvReplyContent
    }

    companion object {

        private val COMMENT = 0
        private val COMMENT_WITH_REPLY = 1
    }
}
