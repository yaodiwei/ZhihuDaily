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

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.DailiesJson;
import com.yao.zhihudaily.net.OkHttpSync;
import com.yao.zhihudaily.net.UrlConstants;
import com.yao.zhihudaily.net.ZhihuHttp;
import com.yao.zhihudaily.tool.DividerItemDecoration;
import com.yao.zhihudaily.tool.RecyclerViewOnLoadMoreListener;
import com.yao.zhihudaily.tool.StateTool;
import com.yao.zhihudaily.ui.MainFragment;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    private Subscriber<DailiesJson> subscriber;

    private StateTool stateTool;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);
        unbinder = ButterKnife.bind(this, view);
        ButterKnife.bind(this, view);

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
        rvDailies.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
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
        subscriber = new Subscriber<DailiesJson>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "Subscriber onError()");
                if (targetDate == null) {
                    stateTool.showErrorView();
                }
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
        };
        if (targetDate == null) {
            ZhihuHttp.getZhihuHttp().getDailies(subscriber);
        } else {
            ZhihuHttp.getZhihuHttp().getDailiesBefore(subscriber, targetDate);
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
    }

    /**
     * @param tartgetDate targetDate为空表示首次刷新或者下拉刷新, 获取最新数据
     *                    不为空表示加载更多, 获取指定日期历史数据
     *                    此为纯RxJava
     */
    @Deprecated
    private void getDailiesOld(final String tartgetDate) {
        Observable.create(new Observable.OnSubscribe<Boolean>() {

            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    Response response = null;
                    if (tartgetDate == null) {
                        response = OkHttpSync.get(UrlConstants.DAILIES);
                    } else {
                        response = OkHttpSync.get(String.format(UrlConstants.DAILIES_BEFORE, tartgetDate));
                    }
                    if (response.isSuccessful()) {
                        DailiesJson dailiesJson = new Gson().fromJson(response.body().string(), DailiesJson.class);
                        if (TextUtils.isEmpty(endDate)) { //如果是首次加载这个界面
                            startDate = dailiesJson.getDate();
                            endDate = dailiesJson.getDate();
                            dailyAdapter.addList(dailiesJson.getStories());
                            subscriber.onCompleted();
                        } else if (tartgetDate == null) { //表示下拉刷新
                            if (endDate.equals(dailiesJson.getDate())) { //App的最晚时间 等于 下拉新获取的时间
                                subscriber.onNext(false);
                            } else { ////App的最晚时间 不等于 下拉新获取的时间
                                endDate = dailiesJson.getDate();
                                dailyAdapter.addListToHeader(dailiesJson.getStories());
                                subscriber.onCompleted();
                            }
                        } else { //表示上拉加载
                            startDate = dailiesJson.getDate();
                            dailyAdapter.addList(dailiesJson.getStories());
                            subscriber.onCompleted();
                        }
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
                        dailyAdapter.notifyDataSetChanged();
                        if (tartgetDate != null) {
                            listener.setLoading(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e, "Subscriber onError()");
                    }

                    @Override
                    public void onNext(Boolean isRefreshing) {
                        swipeRefreshLayout.setRefreshing(isRefreshing);
                    }
                });
    }
}