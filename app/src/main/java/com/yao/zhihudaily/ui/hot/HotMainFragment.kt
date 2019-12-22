package com.yao.zhihudaily.ui.hot

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.orhanobut.logger.Logger
import com.yao.zhihudaily.R
import com.yao.zhihudaily.model.HotJson
import com.yao.zhihudaily.net.ZhihuApiService
import com.yao.zhihudaily.net.ZhihuHttp
import com.yao.zhihudaily.tool.SimpleDividerDecoration
import com.yao.zhihudaily.ui.BaseFragment
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_hot.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Yao
 * @date 2016/7/22
 */
class HotMainFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    private var hotAdapter: HotAdapter? = null

    private var mDisposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_hot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe_refresh_layout.setOnRefreshListener(this)
        rv_hots!!.layoutManager = LinearLayoutManager(fragmentActivity)
        //rv_hots.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        rv_hots!!.addItemDecoration(SimpleDividerDecoration(fragmentActivity))
        hotAdapter = HotAdapter(this)
        rv_hots!!.adapter = hotAdapter
        getHot()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mDisposable != null) {
            mDisposable!!.dispose()
        }
    }

    override fun onRefresh() {
        Log.e("YAO", "HotMainFragment --- onRefresh()")
        getHot()
    }

    private fun getHotOriginal() {
        GlobalScope.launch(Dispatchers.Main) {

            val hotJson = withContext(Dispatchers.IO){
                val dailiesJson = ZhihuApiService.mZhihuApiService.getHotCoroutine()
                dailiesJson
            }

            if (swipe_refresh_layout.isRefreshing) {
                swipe_refresh_layout.isRefreshing = false
            }

            val hots = hotJson.hots
            if (hots != null) {
                hotAdapter!!.addList(hots)
                hotAdapter!!.notifyDataSetChanged()
            }
        }
    }

    private fun getHot() {
        GlobalScope.launch(Dispatchers.Main) {

            try {
                val hotJson = withContext(Dispatchers.IO){
                    val dailiesJson = ZhihuApiService.mZhihuApiService.getHotCoroutine()
                    dailiesJson
                }

                val hots = hotJson.hots
                if (hots != null) {
                    hotAdapter!!.addList(hots)
                    hotAdapter!!.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("YAO", e.toString())
            } finally {
                if (swipe_refresh_layout.isRefreshing) {
                    swipe_refresh_layout.isRefreshing = false
                }
            }
        }
    }

    private fun getHotBak() {
        ZhihuHttp.mZhihuHttp.getHot().subscribe(object : Observer<HotJson> {

            override fun onSubscribe(@NonNull d: Disposable) {
                mDisposable = d
            }

            override fun onNext(hotJson: HotJson) {
                val hots = hotJson.hots
                if (hots != null) {
                    hotAdapter!!.addList(hots)
                    hotAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                Logger.e(e, "Subscriber onError()")
            }
        })
    }

    companion object {

        private val TAG = "HotMainFragment"
    }
}