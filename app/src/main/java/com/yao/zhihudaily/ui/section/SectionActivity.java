package com.yao.zhihudaily.ui.section;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.SectionJson;
import com.yao.zhihudaily.net.ZhihuHttp;
import com.yao.zhihudaily.tool.DividerItemDecoration;
import com.yao.zhihudaily.tool.RecyclerViewOnLoadMoreListener;
import com.yao.zhihudaily.ui.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * Created by Administrator on 2016/9/13.
 */
public class SectionActivity extends BaseActivity {

    private static final String TAG = "SectionActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvStories)
    RecyclerView rvStories;

    private SectionJson sectionJson;
    private int id;
    private String name;
    private SectionStoryAdapter sectionStoryAdapter;
    private LinearLayoutManager linearLayoutManager;

    private RecyclerViewOnLoadMoreListener listener;
    private Disposable mDispzosable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        ButterKnife.bind(this);

        id = getIntent().getIntExtra("id", 0);
        name = getIntent().getStringExtra("name");

        toolbar.setNavigationIcon(R.mipmap.back);//设置导航栏图标
        toolbar.setTitle(name);//设置主标题
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sectionStoryAdapter = new SectionStoryAdapter(this);
        rvStories.setAdapter(sectionStoryAdapter);
        rvStories.setLayoutManager(linearLayoutManager = new LinearLayoutManager(this));
        rvStories.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        rvStories.addOnScrollListener(listener = new RecyclerViewOnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getSectionStories(sectionJson.getTimestamp());
            }
        });

        getSectionStories(-1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDispzosable != null) {
            mDispzosable.dispose();
        }
    }

    private void getSectionStories(final long timestamp) {
        Observer subscriber = new Observer<SectionJson>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDispzosable = d;
            }

            @Override
            public void onNext(SectionJson sectionJson) {
                SectionActivity.this.sectionJson = sectionJson;
                sectionStoryAdapter.addList(sectionJson.getStories());
                sectionStoryAdapter.notifyDataSetChanged();
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
            ZhihuHttp.getZhihuHttp().getSection(subscriber, String.valueOf(id));
        } else if (timestamp == 0) {
        } else {
            ZhihuHttp.getZhihuHttp().getSectionBefore(subscriber, String.valueOf(id), String.valueOf(timestamp));
        }

    }
}
