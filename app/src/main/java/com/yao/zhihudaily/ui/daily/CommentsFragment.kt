package com.yao.zhihudaily.ui.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.annotations.SerializedName
import com.orhanobut.logger.Logger
import com.yao.zhihudaily.R
import com.yao.zhihudaily.model.Comment
import com.yao.zhihudaily.model.CommentJson
import com.yao.zhihudaily.net.ZhihuHttp
import com.yao.zhihudaily.tool.Constant
import com.yao.zhihudaily.tool.DividerItemDecoration
import com.yao.zhihudaily.ui.BaseFragment
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_short_comments.*
import java.util.*

/**
 *
 * @author Yao
 * @date 2016/9/4
 */
class CommentsFragment : BaseFragment() {

    private var comments: ArrayList<Comment>? = null
    private var commentAdapter: CommentAdapter? = null

    @SerializedName("id")
    private var commentId: Int = 0
    private var url: String? = null
    private var count: Int = 0
    private var mDisposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_short_comments, null)
        val bundle = arguments
        if (bundle != null) {
            commentId = bundle.getInt(Constant.ID, 0)
            url = bundle.getString(Constant.URL)
            count = bundle.getInt(Constant.COUNT)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_comments!!.layoutManager = LinearLayoutManager(activity)
        rv_comments!!.addItemDecoration(DividerItemDecoration(fragmentActivity, DividerItemDecoration.VERTICAL_LIST))
        commentAdapter = CommentAdapter(activity!!)
        rv_comments!!.adapter = commentAdapter

        if (count > 20) {
            rv_comments!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                private var lastVisibleItemPosition: Int = 0
                private var visibleItemCount: Int = 0
                private var totalItemCount: Int = 0

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager
                    if (layoutManager != null) {
                        visibleItemCount = layoutManager.childCount
                        lastVisibleItemPosition = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                        totalItemCount = layoutManager.itemCount
                    }
                    if (visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE
                            && lastVisibleItemPosition >= totalItemCount - 1) {
                        //加载更多
                        val snackbar = Snackbar.make(rv_comments!!, "如想查看更多评论\n    请下载正版知乎日报.", Snackbar.LENGTH_SHORT)
                        snackbar.setAction("关闭") { view1 -> snackbar.dismiss() }
                        snackbar.show()
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        }

        getComments(url!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mDisposable != null) {
            mDisposable!!.dispose()
        }
    }

    private fun getComments(url: String) {
        val subscriber = object : Observer<CommentJson> {

            override fun onSubscribe(d: Disposable) {
                mDisposable = d
            }

            override fun onNext(commentJson: CommentJson) {
                comments = commentJson.comments
                if (comments != null) {
                    commentAdapter?.addList(comments!!)
                    commentAdapter?.notifyDataSetChanged()
                    //mToolbar.setTitle(mToolbar.getTitle() + "(以下展示" + comments.size() + "条)");
                }
            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                Logger.e(e, "Subscriber onError()")
            }
        }

        if (url.endsWith("short-comments")) {
            ZhihuHttp.mZhihuHttp.getShortComments(commentId.toString()).subscribe(subscriber)
        } else {
            ZhihuHttp.mZhihuHttp.getLongComments(commentId.toString()).subscribe(subscriber)
        }
    }

    companion object {

        private val TAG = "CommentsFragment"
    }
}
