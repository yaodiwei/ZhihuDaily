package com.yao.zhihudaily.ui.theme

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.orhanobut.logger.Logger
import com.yao.zhihudaily.R
import com.yao.zhihudaily.model.ThemeJson
import com.yao.zhihudaily.net.ZhihuHttp
import com.yao.zhihudaily.tool.DividerItemDecoration
import com.yao.zhihudaily.ui.BaseActivity
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_theme.*


/**
 *
 * @author Yao
 * @date 2016/9/10
 */
class ThemeActivity : BaseActivity() {

    private val mThemeJson: ThemeJson? = null
    private var mId: Int = 0
    private var mThemeStoryAdapter: ThemeStoryAdapter? = null
    private var mDisposable: Disposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme)

        mId = intent.getIntExtra("mId", 0)
        //已经在xml中设置
        //collapsing_toolbar_layout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        //collapsing_toolbar_layout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.mipmap.back)//设置导航栏图标
        //toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setNavigationOnClickListener { view -> finish() }


        mThemeStoryAdapter = ThemeStoryAdapter(this)
        rv_stories!!.adapter = mThemeStoryAdapter
        rv_stories!!.layoutManager = LinearLayoutManager(this)
        rv_stories!!.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST))

        getThemeData()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mDisposable != null) {
            mDisposable!!.dispose()
        }
    }

    private fun getThemeData() {
        ZhihuHttp.zhihuHttp.getTheme(mId.toString()).subscribe(object : Observer<ThemeJson> {

            override fun onSubscribe(@NonNull d: Disposable) {
                mDisposable = d
            }

            override fun onNext(themeJson: ThemeJson) {
                if (themeJson.stories != null) {
                    mThemeStoryAdapter!!.addList(themeJson.stories!!)
                    mThemeStoryAdapter!!.notifyDataSetChanged()
                    Glide.with(this@ThemeActivity).load(themeJson.background).into(iv_background!!)
                    collapsing_toolbar_layout!!.title = themeJson.name
                    tv_description!!.text = "        " + themeJson.description!!
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

        private val TAG = "ThemeActivity"
    }
}
