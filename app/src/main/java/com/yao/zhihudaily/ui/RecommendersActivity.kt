package com.yao.zhihudaily.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.orhanobut.logger.Logger
import com.yao.zhihudaily.R
import com.yao.zhihudaily.model.RecommendsJson
import com.yao.zhihudaily.net.ZhihuHttp
import com.yao.zhihudaily.tool.Constant
import com.yao.zhihudaily.tool.DividerItemDecoration
import com.yao.zhihudaily.tool.StateTool
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_recommenders.*

/**
 *
 * @author Yao
 * @date 2016/9/16
 */
class RecommendersActivity : BaseActivity() {

    private val mRecommendsJson: RecommendsJson? = null
    private var mRecommenderAdapter: RecommenderAdapter? = null

    private var mStateTool: StateTool? = null
    private var mDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommenders)

        val id = intent.getIntExtra(Constant.ID, 0)

        toolbar!!.setNavigationOnClickListener { v -> finish() }

        mStateTool = StateTool(root_view, 1)

        mRecommenderAdapter = RecommenderAdapter(this)
        rv_recommenders!!.adapter = mRecommenderAdapter
        rv_recommenders!!.layoutManager = LinearLayoutManager(this)
        rv_recommenders!!.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST))


        //获取推荐者
        getRecommenders(id.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mDisposable != null) {
            mDisposable!!.dispose()
        }
    }

    private fun getRecommenders(id: String) {
        mStateTool!!.showProgressView()
        ZhihuHttp.mZhihuHttp.getRecommends(id).subscribe(object : Observer<RecommendsJson> {

            override fun onSubscribe(@NonNull d: Disposable) {
                mDisposable = d
            }

            override fun onNext(recommendsJson: RecommendsJson) {
                if (recommendsJson.items == null) {
                    mStateTool!!.showEmptyView()
                    return
                }
                if (recommendsJson.items != null && recommendsJson.items!!.size != 0) {
                    mRecommenderAdapter!!.addList(recommendsJson.items!![0].recommenders)
                    mRecommenderAdapter!!.notifyDataSetChanged()
                    mStateTool!!.showContentView()
                }
            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mStateTool!!.showErrorView()
                Logger.e(e, "Subscriber onError()")
            }
        })
    }

    companion object {

        private val TAG = "RecommendersActivity"
    }
}
