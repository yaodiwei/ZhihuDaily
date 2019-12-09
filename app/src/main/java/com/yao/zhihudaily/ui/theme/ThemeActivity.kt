package com.yao.zhihudaily.ui.theme

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.orhanobut.logger.Logger
import com.yao.zhihudaily.R
import com.yao.zhihudaily.model.ThemeJson
import com.yao.zhihudaily.net.ZhihuHttp
import com.yao.zhihudaily.tool.DividerItemDecoration
import com.yao.zhihudaily.ui.BaseActivity
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable


/**
 *
 * @author Yao
 * @date 2016/9/10
 */
class ThemeActivity : BaseActivity() {
    @JvmField
    @BindView(R.id.iv_background)
    internal var mIvBackground: ImageView? = null
    @JvmField
    @BindView(R.id.tv_description)
    internal var mTvDescription: TextView? = null
    @JvmField
    @BindView(R.id.collapsing_toolbar_layout)
    internal var mCollapsingToolbarLayout: CollapsingToolbarLayout? = null
    @JvmField
    @BindView(R.id.rv_stories)
    internal var mRvStories: RecyclerView? = null

    private val mThemeJson: ThemeJson? = null
    private var mId: Int = 0
    private var mThemeStoryAdapter: ThemeStoryAdapter? = null
    private var mDisposable: Disposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme)
        ButterKnife.bind(this)

        mId = intent.getIntExtra("mId", 0)
        //已经在xml中设置
        //mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        //mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.mipmap.back)//设置导航栏图标
        //toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setNavigationOnClickListener { view -> finish() }


        mThemeStoryAdapter = ThemeStoryAdapter(this)
        mRvStories!!.adapter = mThemeStoryAdapter
        val linearLayoutManager: LinearLayoutManager
        mRvStories!!.layoutManager = LinearLayoutManager(this)
        mRvStories!!.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST))

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
                    Glide.with(this@ThemeActivity).load(themeJson.background).into(mIvBackground!!)
                    mCollapsingToolbarLayout!!.title = themeJson.name
                    mTvDescription!!.text = "        " + themeJson.description!!
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
