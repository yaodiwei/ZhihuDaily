package com.yao.zhihudaily.ui;

import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.RecommendsJson;
import com.yao.zhihudaily.net.ZhihuHttp;
import com.yao.zhihudaily.tool.DividerItemDecoration;
import com.yao.zhihudaily.tool.StateTool;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 *
 * @author Yao
 * @date 2016/9/16
 */
public class RecommendersActivity extends BaseActivity {

    private static final String TAG = "RecommendersActivity";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_recommenders)
    RecyclerView mRvRecommenders;
    @BindView(R.id.rootView)
    CoordinatorLayout mRootView;

    private RecommendsJson mRecommendsJson;
    private RecommenderAdapter mRecommenderAdapter;

    private StateTool mStateTool;
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommenders);
        ButterKnife.bind(this);

        int id = getIntent().getIntExtra("id", 0);

        mToolbar.setNavigationOnClickListener(v -> finish());

        mStateTool = new StateTool(mRootView, 1);

        mRecommenderAdapter = new RecommenderAdapter(this);
        mRvRecommenders.setAdapter(mRecommenderAdapter);
        mRvRecommenders.setLayoutManager(new LinearLayoutManager(this));
        mRvRecommenders.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));


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
        mStateTool.showProgressView();
        ZhihuHttp.getZhihuHttp().getRecommends(id).subscribe(new Observer<RecommendsJson>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(RecommendsJson recommendsJson) {
                if (recommendsJson.getItems() == null) {
                    mStateTool.showEmptyView();
                    return;
                }
                if (recommendsJson.getItems() != null && recommendsJson.getItems().size() != 0) {
                    mRecommenderAdapter.addList(recommendsJson.getItems().get(0).getRecommenders());
                    mRecommenderAdapter.notifyDataSetChanged();
                    mStateTool.showContentView();
                }
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                mStateTool.showErrorView();
                Logger.e(e, "Subscriber onError()");
            }
        });
    }
}
