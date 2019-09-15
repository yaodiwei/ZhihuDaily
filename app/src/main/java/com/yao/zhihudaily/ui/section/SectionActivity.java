package com.yao.zhihudaily.ui.section;

import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.SectionJson;
import com.yao.zhihudaily.net.ZhihuHttp;
import com.yao.zhihudaily.tool.DividerItemDecoration;
import com.yao.zhihudaily.tool.RecyclerViewOnLoadMoreListener;
import com.yao.zhihudaily.ui.BaseActivity;

import androidx.appcompat.widget.Toolbar;
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
 * @date 2016/9/13
 */
public class SectionActivity extends BaseActivity {

    private static final String TAG = "SectionActivity";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_stories)
    RecyclerView mRvStories;

    private SectionJson mSectionJson;
    private int mId;
    private SectionStoryAdapter mSectionStoryAdapter;

    private RecyclerViewOnLoadMoreListener listener;
    private Disposable mDisposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        ButterKnife.bind(this);

        mId = getIntent().getIntExtra("mId", 0);
        String name = getIntent().getStringExtra("mName");

        mToolbar.setNavigationIcon(R.mipmap.back);//设置导航栏图标
        mToolbar.setTitle(name);//设置主标题
        mToolbar.setNavigationOnClickListener(v -> finish());

        mSectionStoryAdapter = new SectionStoryAdapter(this);
        mRvStories.setAdapter(mSectionStoryAdapter);
        LinearLayoutManager linearLayoutManager;
        mRvStories.setLayoutManager(new LinearLayoutManager(this));
        mRvStories.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRvStories.addOnScrollListener(listener = new RecyclerViewOnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getSectionStories(mSectionJson.getTimestamp());
            }
        });

        getSectionStories(-1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    private void getSectionStories(final long timestamp) {
        Observer<SectionJson> subscriber = new Observer<SectionJson>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(SectionJson sectionJson) {
                SectionActivity.this.mSectionJson = sectionJson;
                mSectionStoryAdapter.addList(sectionJson.getStories());
                mSectionStoryAdapter.notifyDataSetChanged();
                if (timestamp > 0) {
                    listener.setLoading(false);
                }
            }
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "Subscriber onError()");
            }
        };
        if (timestamp == -1) {
            ZhihuHttp.getZhihuHttp().getSection(String.valueOf(mId)).subscribe(subscriber);
        } else if (timestamp == 0) {
            //nothing to do
        } else {
            ZhihuHttp.getZhihuHttp().getSectionBefore(String.valueOf(mId), String.valueOf(timestamp)).subscribe(subscriber);
        }

    }
}
