package com.yao.zhihudaily.ui.theme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.ThemesJson;
import com.yao.zhihudaily.net.ZhihuHttp;
import com.yao.zhihudaily.tool.GridItemDecoration;
import com.yao.zhihudaily.ui.MainFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2016/7/22.
 */
public class ThemeMainFragment extends MainFragment {

    private static final String TAG = "ThemeMainFragment";
    @BindView(R.id.rvThemes)
    RecyclerView rvThemes;

    private GridLayoutManager gridLayoutManager;
    private ThemeAdapter themeAdapter;
    private Observer mSubscriber;

    private Disposable mDisposable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme, container, false);
        ButterKnife.bind(this, view);


        rvThemes.setLayoutManager(gridLayoutManager = new GridLayoutManager(getActivity(), 3));
        rvThemes.addItemDecoration(new GridItemDecoration(10, 3));
        rvThemes.setAdapter(themeAdapter = new ThemeAdapter(this));

        getThemes();


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    private void getThemes() {
        Observer mSubscriber = new Observer<ThemesJson>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(ThemesJson themesJson) {
                themeAdapter.addList(themesJson.getOthers());
                themeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "Subscriber onError()");
            }
        };

        ZhihuHttp.getZhihuHttp().getThemes(mSubscriber);
    }
}
