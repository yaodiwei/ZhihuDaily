package com.yao.zhihudaily.ui.section

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.orhanobut.logger.Logger
import com.yao.zhihudaily.R
import com.yao.zhihudaily.model.SectionsJson
import com.yao.zhihudaily.net.ZhihuHttp
import com.yao.zhihudaily.tool.SimpleDividerDecoration
import com.yao.zhihudaily.ui.BaseFragment
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_section.*

/**
 * @author Yao
 * @date 2016/7/22
 */
class SectionMainFragment : BaseFragment() {

    private var mSectionAdapter: SectionAdapter? = null

    private var mDisposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_section, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_sections.layoutManager = LinearLayoutManager(activity)
        //rvSections.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        rv_sections.addItemDecoration(SimpleDividerDecoration(fragmentActivity))
        mSectionAdapter = SectionAdapter(this)
        rv_sections.setAdapter(mSectionAdapter)
        getSections()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mDisposable != null) {
            mDisposable!!.dispose()
        }
    }

    private fun getSections() {
        ZhihuHttp.zhihuHttp.getSections().subscribe(object : Observer<SectionsJson> {

            override fun onSubscribe(@NonNull d: Disposable) {
                mDisposable = d
            }

            override fun onNext(sectionsJson: SectionsJson) {
                val sections = sectionsJson.sections
                if (sections != null) {
                    mSectionAdapter!!.addList(sections)
                    mSectionAdapter!!.notifyDataSetChanged()
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

        private val TAG = "SectionMainFragment"
    }
}