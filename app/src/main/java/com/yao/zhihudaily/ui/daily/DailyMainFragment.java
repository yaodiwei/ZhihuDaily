package com.yao.zhihudaily.ui.daily;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.orhanobut.logger.Logger;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.DailiesJson;
import com.yao.zhihudaily.net.ZhihuHttp;
import com.yao.zhihudaily.tool.RecyclerViewOnLoadMoreListener;
import com.yao.zhihudaily.tool.SimpleDividerDecoration;
import com.yao.zhihudaily.tool.StateTool;
import com.yao.zhihudaily.ui.MainFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2016/7/22.
 */
public class DailyMainFragment extends MainFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "DailyMainFragment";

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvDailies)
    RecyclerView rvDailies;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;

    private Unbinder unbinder;

    private DailyAdapter dailyAdapter;

    private String endDate;//当前App里有的最新日报的时间

    private String startDate;//当前App下拉加载到的最早时间

    private LinearLayoutManager linearLayoutManager;

    private RecyclerViewOnLoadMoreListener listener;

    private Observer<DailiesJson> observer;

    private StateTool stateTool;

    private Disposable mDisposable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);
        unbinder = ButterKnife.bind(this, view);

        stateTool = new StateTool(linearLayout);
        stateTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDate = null;
                stateTool.showProgressView();
                getDailies(null);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);
        rvDailies.setLayoutManager(linearLayoutManager = new LinearLayoutManager(getActivity()));
//        rvDailies.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        rvDailies.addItemDecoration(new SimpleDividerDecoration(getActivity()));
        rvDailies.setAdapter(dailyAdapter = new DailyAdapter(this));
        rvDailies.addOnScrollListener(listener = new RecyclerViewOnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getDailies(startDate);
            }
        });

        stateTool.showProgressView();
        getDailies(null);

        return view;
    }

    private void getDailies(final String targetDate) {
        observer = new Observer<DailiesJson>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(DailiesJson dailiesJson) {
                if (dailiesJson.getStories().size() == 0) {
                    stateTool.showEmptyView();
                    return;
                }
                if (TextUtils.isEmpty(endDate)) { //如果是首次加载这个界面
                    startDate = dailiesJson.getDate();
                    endDate = dailiesJson.getDate();
                    dailyAdapter.addList(dailiesJson.getStories());
                    dailyAdapter.notifyDataSetChanged();
                    stateTool.showContentView();
                } else if (targetDate == null) { //表示下拉刷新
                    if (endDate.equals(dailiesJson.getDate())) { //App的最晚时间 等于 下拉新获取的时间
                        swipeRefreshLayout.setRefreshing(false);
                    } else { ////App的最晚时间 不等于 下拉新获取的时间
                        endDate = dailiesJson.getDate();
                        dailyAdapter.addListToHeader(dailiesJson.getStories());
                        dailyAdapter.notifyDataSetChanged();
                        stateTool.showContentView();
                    }
                } else { //表示上拉加载
                    startDate = dailiesJson.getDate();
                    dailyAdapter.addList(dailiesJson.getStories());
                    dailyAdapter.notifyDataSetChanged();
                    listener.setLoading(false);
                    stateTool.showContentView();
                }
            }

            @Override
            public void onComplete() {}

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "Subscriber onError()");
                if (targetDate == null) {
                    stateTool.showErrorView();
                }
            }
        };
        if (targetDate == null) {
            ZhihuHttp.getZhihuHttp().getDailies(observer);
        } else {
            ZhihuHttp.getZhihuHttp().getDailiesBefore(observer, targetDate);
        }

    }


    @Override
    public void onRefresh() {
        getDailies(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //如果没有这个置空,当没有设置fragment缓存时,会执行destroyView方法.但是成员变量并不会摧毁,依然有值
        // 下次再进来时,会得出endDate不是空的情况,从而跳过"首次加载这个界面"这个逻辑
        endDate = null;
        unbinder.unbind();
        mDisposable.dispose();
    }
}