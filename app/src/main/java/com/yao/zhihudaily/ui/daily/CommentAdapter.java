package com.yao.zhihudaily.ui.daily;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.Comment;
import com.yao.zhihudaily.tool.GlideCircleTransform;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/30.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ShortCommentViewHolder> {

    private static final int COMMENT = 0;
    private static final int COMMENT_WITH_REPLY = 1;


    private ArrayList<Comment> comments = new ArrayList<>();
    private LayoutInflater inflater;
    private Context ctx;
    private GlideCircleTransform glideCircleTransform;

    public CommentAdapter(Context ctx) {
        this.ctx = ctx;
        inflater = LayoutInflater.from(ctx);
        glideCircleTransform = new GlideCircleTransform(ctx);
    }

    public void addList(ArrayList<Comment> comments) {
        this.comments.addAll(comments);
    }


    @Override
    public CommentAdapter.ShortCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == COMMENT) {
            return new ShortCommentViewHolder(inflater.inflate(R.layout.item_short_comment, null), COMMENT);
        } else {
            return new ShortCommentViewHolder(inflater.inflate(R.layout.item_short_comment_with_reply, null), COMMENT_WITH_REPLY);
        }
    }

    @Override
    public void onBindViewHolder(final CommentAdapter.ShortCommentViewHolder holder, int position) {
        Comment c = comments.get(position);
        BitmapImageViewTarget target = new BitmapImageViewTarget(holder.ivAvatar){
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(ctx.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.ivAvatar.setImageDrawable(circularBitmapDrawable);
            }
        };
        Glide.with(ctx).load(c.getAvatar()).asBitmap().centerCrop().into(target);
        Glide.with(ctx).load(c.getAvatar()).transform(glideCircleTransform).into(holder.ivAvatar);
        holder.tvAuthor.setText(c.getAuthor());
        holder.tvContent.setText(c.getContent());
        holder.tvTime.setText(c.getTimeStr());
        holder.tvLikes.setText(String.valueOf(c.getLikes()));
        if (c.getReplyTo() != null) {
            String replyAuthor = "@" + c.getReplyTo().getAuthor()+": ";
            holder.tvReplyAuthor.setText(replyAuthor);
            holder.tvReplyContent.setText(replyAuthor + c.getReplyTo().getContent());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (comments.get(position).getReplyTo() == null) {
            return COMMENT;
        } else {
            return COMMENT_WITH_REPLY;
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ShortCommentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivAvatar)
        ImageView ivAvatar;
        @BindView(R.id.tvAuthor)
        TextView tvAuthor;
        @BindView(R.id.tvContent)
        TextView tvContent;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvLikes)
        TextView tvLikes;
        @Nullable
        @BindView(R.id.tvReplyAuthor)
        TextView tvReplyAuthor;
        @Nullable
        @BindView(R.id.tvReplyContent)
        TextView tvReplyContent;

        public ShortCommentViewHolder(View itemView, int type) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
