package com.yao.zhihudaily.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.orhanobut.logger.Logger
import com.yao.zhihudaily.R
import com.yao.zhihudaily.model.DailyJson
import com.yao.zhihudaily.model.StoryExtra
import com.yao.zhihudaily.net.UrlConstants
import com.yao.zhihudaily.net.ZhihuHttp
import com.yao.zhihudaily.tool.Constant
import com.yao.zhihudaily.ui.daily.CommentsActivity
import com.yao.zhihudaily.util.HtmlUtil
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_news_detail.*

/**
 * @author Yao
 * @date 2016/7/28
 */
class NewsDetailActivity : BaseActivity() {

    private var mStoryExtra: StoryExtra? = null
    private val mCompositeDisposable = CompositeDisposable()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        val id = intent.getIntExtra(Constant.ID, 0)

        //也可以在xml中设置
        collapsing_toolbar_layout.setExpandedTitleTextAppearance(R.style.ExpandedDisappearAppBar)
        collapsing_toolbar_layout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)

        toolbar.setNavigationIcon(R.mipmap.back)//设置导航栏图标
        toolbar.inflateMenu(R.menu.new_detail_menu)//设置右上角的填充菜单
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.itemShare -> {
                    Toast.makeText(this@NewsDetailActivity, "点击分享", Toast.LENGTH_SHORT).show()
                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "发现一篇好文章分享给你，地址:" + String.format(UrlConstants.STORY_SHARE, id))
                    sendIntent.type = "text/plain"
                    startActivity(sendIntent)
                }
                R.id.itemComment -> {
                    val intent = Intent(this@NewsDetailActivity, CommentsActivity::class.java)
                    intent.putExtra(Constant.ID, id)
                    intent.putExtra(Constant.STORY_EXTRA, mStoryExtra)
                    startActivity(intent)
                }
                else -> {
                }
            }
            true
        }

        getNews(id)
        getStoryExtra(id)
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.clear()
    }

    private fun getNews(id: Int) {
        ZhihuHttp.mZhihuHttp.getNews(id.toString()).subscribe(object : Observer<DailyJson> {

            override fun onSubscribe(@NonNull d: Disposable) {
                mCompositeDisposable.add(d)
            }

            override fun onNext(dailyJson: DailyJson) {
                web_view.loadData(HtmlUtil.createHtmlData(dailyJson), HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING)
                tv_title.text = dailyJson.title
                tv_source.text = dailyJson.imageSource
                if (dailyJson.recommenders == null) {
                    collapsing_toolbar_layout.title = "并没有推荐者"
                } else {
                    collapsing_toolbar_layout.title = dailyJson.recommenders!!.size.toString() + "个推荐者"
                    toolbar.setOnClickListener { view ->
                        val intent = Intent(this@NewsDetailActivity, RecommendersActivity::class.java)
                        intent.putExtra(Constant.ID, id)
                        startActivity(intent)
                    }
                }
                Glide.with(this@NewsDetailActivity).load(dailyJson.image).placeholder(R.mipmap.liukanshan).into(iv_news)
            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                Logger.e(e, "Subscriber onError()")
            }
        })
    }

    private fun getStoryExtra(id: Int) {
        ZhihuHttp.mZhihuHttp.getStoryExtra(id.toString()).subscribe(object : Observer<StoryExtra> {

            override fun onSubscribe(@NonNull d: Disposable) {
                mCompositeDisposable.add(d)
            }

            override fun onNext(storyExtra: StoryExtra) {
                this@NewsDetailActivity.mStoryExtra = storyExtra
            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                Logger.e(e, "Subscriber onError()")
            }
        })
    }

    companion object {

        private val TAG = "NewsDetailActivity"
    }
}
