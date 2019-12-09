package com.yao.zhihudaily.ui.daily

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.orhanobut.logger.Logger
import com.yao.zhihudaily.R
import com.yao.zhihudaily.model.DailiesJson
import com.yao.zhihudaily.net.ZhihuHttp
import com.yao.zhihudaily.tool.RecyclerViewOnLoadMoreListener
import com.yao.zhihudaily.tool.SimpleDividerDecoration
import com.yao.zhihudaily.tool.StateTool
import com.yao.zhihudaily.ui.BaseFragment
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable

/**
 * @author Yao
 * @date 2016/7/22
 */
class DailyMainFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    @JvmField
    @BindView(R.id.swipeRefreshLayout)
    internal var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    @JvmField
    @BindView(R.id.rv_dailies)
    internal var mRvDailies: RecyclerView? = null
    @JvmField
    @BindView(R.id.linear_layout)
    internal var mLinearLayout: LinearLayout? = null

    private var mDailyAdapter: DailyAdapter? = null

    private var mEndDate: String? = null//当前App里有的最新日报的时间

    private var mStartDate: String? = null//当前App下拉加载到的最早时间

    private lateinit var mRecyclerViewOnLoadMoreListener: RecyclerViewOnLoadMoreListener

    private var mStateTool: StateTool? = null

    private var mDisposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_daily, container, false)
        ButterKnife.bind(this, view)

        mStateTool = StateTool(mLinearLayout!!)
        mStateTool!!.setOnClickListener(View.OnClickListener{
            mEndDate = null
            mStateTool!!.showProgressView()
            getDailies(null)
        })

        mSwipeRefreshLayout!!.setOnRefreshListener(this)
        val linearLayoutManager: LinearLayoutManager
        mRvDailies!!.layoutManager = LinearLayoutManager(activity)
        //mRvDailies.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRvDailies!!.addItemDecoration(SimpleDividerDecoration(fragmentActivity))
        mDailyAdapter = DailyAdapter(this)
        mRvDailies!!.setAdapter(mDailyAdapter)
        mRecyclerViewOnLoadMoreListener = object : RecyclerViewOnLoadMoreListener() {
            override fun onLoadMore() {
                getDailies(mStartDate)
            }
        }
        mRvDailies!!.addOnScrollListener(mRecyclerViewOnLoadMoreListener)

        mStateTool!!.showProgressView()
        getDailies(null)

        return view
    }

    private fun getDailies(targetDate: String?) {
        //如果是首次加载这个界面
        //表示下拉刷新
        //App的最晚时间 等于 下拉新获取的时间
        ////App的最晚时间 不等于 下拉新获取的时间
        //表示上拉加载
        val observer = object : Observer<DailiesJson> {

            override fun onSubscribe(@NonNull d: Disposable) {
                mDisposable = d
            }

            override fun onNext(dailiesJson: DailiesJson) {
                if (dailiesJson.allStories.size == 0) {
                    mStateTool!!.showEmptyView()
                    return
                }
                if (TextUtils.isEmpty(mEndDate)) { //如果是首次加载这个界面
                    mStartDate = dailiesJson.date
                    mEndDate = dailiesJson.date
                    mDailyAdapter!!.addList(dailiesJson.allStories)
                    mDailyAdapter!!.notifyDataSetChanged()
                    mStateTool!!.showContentView()
                } else if (targetDate == null) { //表示下拉刷新
                    if (mEndDate == dailiesJson.date) { //App的最晚时间 等于 下拉新获取的时间
                        mSwipeRefreshLayout!!.isRefreshing = false
                    } else { ////App的最晚时间 不等于 下拉新获取的时间
                        mEndDate = dailiesJson.date
                        mDailyAdapter!!.addListToHeader(dailiesJson.allStories)
                        mDailyAdapter!!.notifyDataSetChanged()
                        mStateTool!!.showContentView()
                    }
                } else { //表示上拉加载
                    mStartDate = dailiesJson.date
                    mDailyAdapter!!.addList(dailiesJson.allStories)
                    mDailyAdapter!!.notifyDataSetChanged()
                    mRecyclerViewOnLoadMoreListener!!.loading = false
                    mStateTool!!.showContentView()
                }
            }

            override fun onComplete() {}

            override fun onError(e: Throwable) {
                Logger.e(e, "Subscriber onError()")
                if (targetDate == null) {
                    mStateTool!!.showErrorView()
                }
            }
        }
        if (targetDate == null) {
            ZhihuHttp.zhihuHttp.dailies.subscribe(observer)
        } else {
            ZhihuHttp.zhihuHttp.getDailiesBefore(targetDate).subscribe(observer)
        }

    }


    override fun onRefresh() {
        getDailies(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //如果没有这个置空,当没有设置fragment缓存时,会执行destroyView方法.但是成员变量并不会摧毁,依然有值
        // 下次再进来时,会得出endDate不是空的情况,从而跳过"首次加载这个界面"这个逻辑
        mEndDate = null
        mDisposable!!.dispose()
    }

    companion object {

        private val TAG = "DailyMainFragment"
    }
}