package com.yao.zhihudaily.ui.feed;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.ShortComment;
import com.yao.zhihudaily.model.ShortCommentJson;
import com.yao.zhihudaily.model.StoryExtra;
import com.yao.zhihudaily.net.OkHttpSync;
import com.yao.zhihudaily.net.UrlConstants;
import com.yao.zhihudaily.tool.DividerItemDecoration;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/30.
 */
public class CommentsActivity extends Activity {

    private int id;
    private StoryExtra storyExtra;
    private RecyclerView rvComments;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<ShortComment> shortComments;
    private CommentAdapter commentAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        id = getIntent().getIntExtra("id", 0);
        storyExtra = (StoryExtra) getIntent().getSerializableExtra("storyExtra");
        rvComments = (RecyclerView) findViewById(R.id.rvComments);


        rvComments.setLayoutManager(linearLayoutManager = new LinearLayoutManager(this));
        rvComments.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        rvComments.setAdapter(commentAdapter = new CommentAdapter(this));


        Subscription subscription = Observable.create(new Observable.OnSubscribe<Boolean>() {

            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    Response response = OkHttpSync.get(String.format(UrlConstants.SHORT_COMMENTS, String.valueOf(id)));
                    if (response.isSuccessful()) {
                        String json = response.body().string();
                        Log.e("YAO", "CommentsActivity.java - call() ---------- " + json);
                        ShortCommentJson shortCommentJson = new Gson().fromJson(json, ShortCommentJson.class);
                        shortComments = shortCommentJson.getShortComments();
                        Log.e("YAO", "CommentsActivity.java - call() ---------- " + shortComments);
                        commentAdapter.addList(shortComments);
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new Exception("error"));
                    }
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {

                    @Override
                    public void onCompleted() {
                        commentAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("YAO", "onError: " + e.toString());
                    }

                    @Override
                    public void onNext(Boolean isRefreshing) {
                    }
                });
    }
}
