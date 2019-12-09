package com.yao.zhihudaily.ui.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.orhanobut.logger.Logger
import com.yao.zhihudaily.R
import com.yao.zhihudaily.model.ThemesJson
import com.yao.zhihudaily.net.ZhihuHttp
import com.yao.zhihudaily.tool.GridItemDecoration
import com.yao.zhihudaily.ui.BaseFragment
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable

/**
 * @author Yao
 * @date 2016/7/22
 */
class ThemeMainFragment : BaseFragment() {
    @JvmField
    @BindView(R.id.rv_themes)
    internal var mRvThemes: RecyclerView? = null

    private var mThemeAdapter: ThemeAdapter? = null

    private var mDisposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_theme, container, false)
        ButterKnife.bind(this, view)


        val gridLayoutManager: GridLayoutManager
        mRvThemes!!.layoutManager = GridLayoutManager(activity, 3)
        mRvThemes!!.addItemDecoration(GridItemDecoration(10, 3))
        mThemeAdapter = ThemeAdapter(this)
        mRvThemes!!.setAdapter(mThemeAdapter)

        getThemes()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mDisposable != null) {
            mDisposable!!.dispose()
        }
    }

    private fun getThemes() {

        ZhihuHttp.zhihuHttp.themes.subscribe(object : Observer<ThemesJson> {

            override fun onSubscribe(@NonNull d: Disposable) {
                mDisposable = d
            }

            override fun onNext(themesJson: ThemesJson) {
                if (themesJson.others != null) {
                    mThemeAdapter!!.addList(themesJson.others!!)
                    mThemeAdapter!!.notifyDataSetChanged()
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

        private val TAG = "ThemeMainFragment"
    }
}
