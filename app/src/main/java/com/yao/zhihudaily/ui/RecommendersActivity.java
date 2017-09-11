package com.yao.zhihudaily.ui;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.RecommendsJson;
import com.yao.zhihudaily.net.ZhihuHttp;
import com.yao.zhihudaily.tool.DividerItemDecoration;
import com.yao.zhihudaily.tool.StateTool;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2016/9/16.
 */
public class RecommendersActivity extends BaseActivity {

    private static final String TAG = "RecommendersActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvRecommenders)
    RecyclerView rvRecommenders;
    @BindView(R.id.rootView)
    CoordinatorLayout rootView;

    private RecommendsJson recommendsJson;
    private int id;
    private RecommenderAdapter recommenderAdapter;
    private LinearLayoutManager linearLayoutManager;

    private StateTool stateTool;
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommenders);
        ButterKnife.bind(this);

        id = getIntent().getIntExtra("id", 0);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        stateTool = new StateTool(rootView, 1);

        recommenderAdapter = new RecommenderAdapter(this);
        rvRecommenders.setAdapter(recommenderAdapter);
        rvRecommenders.setLayoutManager(linearLayoutManager = new LinearLayoutManager(this));
        rvRecommenders.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));


        //获取推荐者
        getRecommenders(String.valueOf(id));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    private void getRecommenders(String id) {
        Observer subscriber = new Observer<RecommendsJson>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(RecommendsJson recommendsJson) {
                if (recommendsJson.getItems() == null) {
                    stateTool.showEmptyView();
                    return;
                }
                if (recommendsJson.getItems() != null && recommendsJson.getItems().size() != 0) {
                    recommenderAdapter.addList(recommendsJson.getItems().get(0).getRecommenders());
                    recommenderAdapter.notifyDataSetChanged();
                    stateTool.showContentView();
                }
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                stateTool.showErrorView();
                Logger.e(e, "Subscriber onError()");
            }
        };

        stateTool.showProgressView();
        ZhihuHttp.getZhihuHttp().getRecommends(subscriber, id);
    }
}
