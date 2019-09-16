package com.yao.zhihudaily.ui.daily;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.logger.Logger;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.Comment;
import com.yao.zhihudaily.model.CommentJson;
import com.yao.zhihudaily.net.ZhihuHttp;
import com.yao.zhihudaily.tool.Constant;
import com.yao.zhihudaily.tool.DividerItemDecoration;
import com.yao.zhihudaily.ui.BaseFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 *
 * @author Yao
 * @date 2016/9/4
 */
public class CommentsFragment extends BaseFragment {

    private static final String TAG = "CommentsFragment";
    @BindView(R.id.rvComments)
    RecyclerView rvComments;

    private ArrayList<Comment> comments;
    private CommentAdapter commentAdapter;

    private int id;
    private String url;
    private int count;
    private Disposable mDisposable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_short_comments, null);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getInt(Constant.ID, 0);
            url = bundle.getString(Constant.URL);
            count = bundle.getInt(Constant.COUNT);
        }


        LinearLayoutManager linearLayoutManager;
        rvComments.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvComments.addItemDecoration(new DividerItemDecoration(getFragmentActivity(), DividerItemDecoration.VERTICAL_LIST));
        rvComments.setAdapter(commentAdapter = new CommentAdapter(getActivity()));

        if (count > 20) {
            rvComments.addOnScrollListener(new RecyclerView.OnScrollListener() {
                private int lastVisibleItemPosition;
                private int visibleItemCount;
                private int totalItemCount;

                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager != null) {
                        visibleItemCount = layoutManager.getChildCount();
                        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                        totalItemCount = layoutManager.getItemCount();
                    }
                    if (visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE
                            && lastVisibleItemPosition >= totalItemCount - 1) {
                        //加载更多
                        final Snackbar snackbar = Snackbar.make(rvComments, "如想查看更多评论\n    请下载正版知乎日报.", Snackbar.LENGTH_SHORT);
                        snackbar.setAction("关闭", view1 -> snackbar.dismiss());
                        snackbar.show();
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
        }

        getComments(url);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    private void getComments(String url) {
        Observer<CommentJson> subscriber = new Observer<CommentJson>() {

            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(CommentJson commentJson) {
                comments = commentJson.getComments();
                commentAdapter.addList(comments);
                commentAdapter.notifyDataSetChanged();
                //mToolbar.setTitle(mToolbar.getTitle() + "(以下展示" + comments.size() + "条)");
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "Subscriber onError()");
            }
        };

        if (url.endsWith("short-comments")) {
            ZhihuHttp.getZhihuHttp().getShortComments(String.valueOf(id)).subscribe(subscriber);
        } else {
            ZhihuHttp.getZhihuHttp().getLongComments(String.valueOf(id)).subscribe(subscriber);
        }
    }
}
