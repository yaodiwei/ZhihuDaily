package com.yao.zhihudaily.ui.feed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.ShortComment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/30.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ShortCommentViewHolder> {

    private static final int COMMENT = 0;
    private static final int COMMENT_WITH_REPLY = 1;


    private List<ShortComment> comments = new ArrayList<>();
    private LayoutInflater inflater;
    private Context ctx;

    public CommentAdapter(Context ctx) {
        this.ctx = ctx;
        inflater = LayoutInflater.from(ctx);
    }

    public void addList(List<ShortComment> comments) {
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
    public void onBindViewHolder(CommentAdapter.ShortCommentViewHolder holder, int position) {
        ShortComment c = comments.get(position);
        Glide.with(ctx).load(c.getAvatar()).into(holder.ivAvatar);
        holder.tvAuthor.setText(c.getAuthor());
        holder.tvContent.setText(c.getAuthor() + c.getContent());
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

        ImageView ivAvatar;
        TextView tvAuthor;
        TextView tvContent;
        TextView tvTime;
        TextView tvLikes;
        TextView tvReplyAuthor;
        TextView tvReplyContent;

        public ShortCommentViewHolder(View itemView, int type) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.ivAvatar);
            tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvLikes = (TextView) itemView.findViewById(R.id.tvLikes);
            if (type == COMMENT_WITH_REPLY) {
                tvReplyAuthor = (TextView) itemView.findViewById(R.id.tvReplyAuthor);
                tvReplyContent = (TextView) itemView.findViewById(R.id.tvReplyContent);
            }
        }
    }
}
