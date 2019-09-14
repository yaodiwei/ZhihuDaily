package com.yao.zhihudaily.ui.daily;

import android.os.Bundle;
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
import com.yao.zhihudaily.ui.BaseFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * @author Yao
 * @date 2016/7/22
 */
public class DailyMainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "DailyMainFragment";

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.rv_dailies)
    RecyclerView mRvDailies;
    @BindView(R.id.linear_layout)
    LinearLayout mLinearLayout;

    private DailyAdapter mDailyAdapter;

    private String mEndDate;//当前App里有的最新日报的时间

    private String mStartDate;//当前App下拉加载到的最早时间

    private RecyclerViewOnLoadMoreListener mRecyclerViewOnLoadMoreListener;

    private StateTool mStateTool;

    private Disposable mDisposable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);
        ButterKnife.bind(this, view);

        mStateTool = new StateTool(mLinearLayout);
        mStateTool.setOnClickListener(v -> {
            mEndDate = null;
            mStateTool.showProgressView();
            getDailies(null);
        });

        mSwipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager;
        mRvDailies.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRvDailies.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRvDailies.addItemDecoration(new SimpleDividerDecoration(getFragmentActivity()));
        mRvDailies.setAdapter(mDailyAdapter = new DailyAdapter(this));
        mRvDailies.addOnScrollListener(mRecyclerViewOnLoadMoreListener = new RecyclerViewOnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getDailies(mStartDate);
            }
        });

        mStateTool.showProgressView();
        getDailies(null);

        return view;
    }

    private void getDailies(final String targetDate) {
        //如果是首次加载这个界面
        //表示下拉刷新
        //App的最晚时间 等于 下拉新获取的时间
        ////App的最晚时间 不等于 下拉新获取的时间
        //表示上拉加载
        Observer<DailiesJson> observer = new Observer<DailiesJson>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(DailiesJson dailiesJson) {
                if (dailiesJson.getStories().size() == 0) {
                    mStateTool.showEmptyView();
                    return;
                }
                if (TextUtils.isEmpty(mEndDate)) { //如果是首次加载这个界面
                    mStartDate = dailiesJson.getDate();
                    mEndDate = dailiesJson.getDate();
                    mDailyAdapter.addList(dailiesJson.getStories());
                    mDailyAdapter.notifyDataSetChanged();
                    mStateTool.showContentView();
                } else if (targetDate == null) { //表示下拉刷新
                    if (mEndDate.equals(dailiesJson.getDate())) { //App的最晚时间 等于 下拉新获取的时间
                        mSwipeRefreshLayout.setRefreshing(false);
                    } else { ////App的最晚时间 不等于 下拉新获取的时间
                        mEndDate = dailiesJson.getDate();
                        mDailyAdapter.addListToHeader(dailiesJson.getStories());
                        mDailyAdapter.notifyDataSetChanged();
                        mStateTool.showContentView();
                    }
                } else { //表示上拉加载
                    mStartDate = dailiesJson.getDate();
                    mDailyAdapter.addList(dailiesJson.getStories());
                    mDailyAdapter.notifyDataSetChanged();
                    mRecyclerViewOnLoadMoreListener.setLoading(false);
                    mStateTool.showContentView();
                }
            }

            @Override
            public void onComplete() {
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "Subscriber onError()");
                if (targetDate == null) {
                    mStateTool.showErrorView();
                }
            }
        };
        if (targetDate == null) {
            ZhihuHttp.getZhihuHttp().getDailies().subscribe(observer);
        } else {
            ZhihuHttp.getZhihuHttp().getDailiesBefore(targetDate).subscribe(observer);
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
        mEndDate = null;
        mDisposable.dispose();
    }
}