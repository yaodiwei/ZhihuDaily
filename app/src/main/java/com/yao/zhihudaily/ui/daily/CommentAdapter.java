package com.yao.zhihudaily.ui.daily;

import android.content.Context;
import android.graphics.Bitmap;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * @author Administrator
 * @date 2016/8/30
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ShortCommentViewHolder> {

    private static final int COMMENT = 0;
    private static final int COMMENT_WITH_REPLY = 1;


    private ArrayList<Comment> mComments = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private Context ctx;
    private GlideCircleTransform mGlideCircleTransform;

    public CommentAdapter(Context ctx) {
        this.ctx = ctx;
        mLayoutInflater = LayoutInflater.from(ctx);
        mGlideCircleTransform = new GlideCircleTransform();
    }

    public void addList(ArrayList<Comment> comments) {
        this.mComments.addAll(comments);
    }


    @NonNull
    @Override
    public CommentAdapter.ShortCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == COMMENT) {
            return new ShortCommentViewHolder(mLayoutInflater.inflate(R.layout.item_short_comment, null), COMMENT);
        } else {
            return new ShortCommentViewHolder(mLayoutInflater.inflate(R.layout.item_short_comment_with_reply, null), COMMENT_WITH_REPLY);
        }
    }

    @Override
    public void onBindViewHolder(final CommentAdapter.ShortCommentViewHolder holder, int position) {
        Comment c = mComments.get(position);
        BitmapImageViewTarget target = new BitmapImageViewTarget(holder.ivAvatar){
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(ctx.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.ivAvatar.setImageDrawable(circularBitmapDrawable);
            }
        };
        Glide.with(ctx).asBitmap().load(c.getAvatar()).centerCrop().into(target);
        Glide.with(ctx).load(c.getAvatar()).transform(mGlideCircleTransform).into(holder.ivAvatar);
        holder.tvAuthor.setText(c.getAuthor());
        holder.tvContent.setText(c.getContent());
        holder.tvTime.setText(c.getTimeStr());
        holder.tvLikes.setText(String.valueOf(c.getLikes()));
        if (c.getReplyTo() != null) {
            String replyAuthor = "@" + c.getReplyTo().getAuthor()+": ";
            if (holder.tvReplyAuthor != null) {
                holder.tvReplyAuthor.setText(replyAuthor);
            }
            if (holder.tvReplyContent != null) {
                holder.tvReplyContent.setText(replyAuthor + c.getReplyTo().getContent());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mComments.get(position).getReplyTo() == null) {
            return COMMENT;
        } else {
            return COMMENT_WITH_REPLY;
        }
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    class ShortCommentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_author)
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
