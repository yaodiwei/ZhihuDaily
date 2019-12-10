package com.yao.zhihudaily.ui.section

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.orhanobut.logger.Logger
import com.yao.zhihudaily.R
import com.yao.zhihudaily.model.SectionJson
import com.yao.zhihudaily.net.ZhihuHttp
import com.yao.zhihudaily.tool.Constant
import com.yao.zhihudaily.tool.DividerItemDecoration
import com.yao.zhihudaily.tool.RecyclerViewOnLoadMoreListener
import com.yao.zhihudaily.ui.BaseActivity
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_section.*


/**
 *
 * @author Yao
 * @date 2016/9/13
 */
class SectionActivity : BaseActivity() {

    private var mSectionJson: SectionJson? = null
    private var mId: Int = 0
    private var mSectionStoryAdapter: SectionStoryAdapter? = null

    private var listener: RecyclerViewOnLoadMoreListener? = null
    private var mDisposable: Disposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section)

        mId = intent.getIntExtra(Constant.ID, 0)
        val name = intent.getStringExtra(Constant.NAME)

        toolbar!!.setNavigationIcon(R.mipmap.back)//设置导航栏图标
        toolbar!!.title = name//设置主标题
        toolbar!!.setNavigationOnClickListener { v -> finish() }

        mSectionStoryAdapter = SectionStoryAdapter(this)
        rv_stories!!.adapter = mSectionStoryAdapter
        rv_stories!!.layoutManager = LinearLayoutManager(this)
        rv_stories!!.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST))
        listener = object : RecyclerViewOnLoadMoreListener() {
            override fun onLoadMore() {
                getSectionStories(mSectionJson!!.timestamp)
            }
        }
        rv_stories!!.addOnScrollListener(listener as RecyclerViewOnLoadMoreListener)

        getSectionStories(-1)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mDisposable != null) {
            mDisposable!!.dispose()
        }
    }

    private fun getSectionStories(timestamp: Long) {
        val subscriber = object : Observer<SectionJson> {

            override fun onSubscribe(@NonNull d: Disposable) {
                mDisposable = d
            }

            override fun onNext(sectionJson: SectionJson) {
                this@SectionActivity.mSectionJson = sectionJson
                if (sectionJson.stories != null) {
                    mSectionStoryAdapter!!.addList(sectionJson.stories!!)
                    mSectionStoryAdapter!!.notifyDataSetChanged()
                    if (timestamp > 0) {
                        listener!!.loading = false
                    }
                }
            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                Logger.e(e, "Subscriber onError()")
            }
        }
        if (timestamp == -1L) {
            ZhihuHttp.zhihuHttp.getSection(mId.toString()).subscribe(subscriber)
        } else if (timestamp == 0L) {
            //nothing to do
        } else {
            ZhihuHttp.zhihuHttp.getSectionBefore(mId.toString(), timestamp.toString()).subscribe(subscriber)
        }

    }

    companion object {

        private val TAG = "SectionActivity"
    }
}
