package com.yao.zhihudaily.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.RecommendsJson;
import com.yao.zhihudaily.net.OkHttpSync;
import com.yao.zhihudaily.net.UrlConstants;
import com.yao.zhihudaily.tool.DividerItemDecoration;
import com.yao.zhihudaily.ui.theme.RecommenderAdapter;

import java.io.IOException;

import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/9/16.
 */
public class RecommendersActivity extends Activity {

    private static final String TAG = "RecommendersActivity";

    private RecommendsJson recommendsJson;
    private int id;
    private Toolbar toolbar;
    private RecyclerView rvRecommenders;
    private RecommenderAdapter recommenderAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommenders);

        id = getIntent().getIntExtra("id", 0);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        rvRecommenders = (RecyclerView) findViewById(R.id.rvRecommenders);
        recommenderAdapter = new RecommenderAdapter(this);
        rvRecommenders.setAdapter(recommenderAdapter);
        rvRecommenders.setLayoutManager(linearLayoutManager = new LinearLayoutManager(this));
        rvRecommenders.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));


        //获取推荐者
        Observable.create(new Observable.OnSubscribe<RecommendsJson>() {

            @Override
            public void call(Subscriber<? super RecommendsJson> subscriber) {
                try {
                    Response response = OkHttpSync.get(String.format(UrlConstants.RECOMMENDERS, id));
                    if (response.isSuccessful()) {
                        String json = response.body().string();
                        recommendsJson = new Gson().fromJson(json, RecommendsJson.class);
                        recommenderAdapter.addList(recommendsJson.getItems().get(0).getRecommenders());
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
                .subscribe(new Subscriber<RecommendsJson>() {

                    @Override
                    public void onCompleted() {
                        recommenderAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onNext(RecommendsJson recommendsJson) {
                    }
                });
    }
}
