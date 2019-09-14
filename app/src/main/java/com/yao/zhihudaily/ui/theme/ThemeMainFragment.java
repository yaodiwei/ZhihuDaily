package com.yao.zhihudaily.ui.theme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.ThemesJson;
import com.yao.zhihudaily.net.ZhihuHttp;
import com.yao.zhihudaily.tool.GridItemDecoration;
import com.yao.zhihudaily.ui.BaseFragment;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * @author Yao
 * @date 2016/7/22
 */
public class ThemeMainFragment extends BaseFragment {

    private static final String TAG = "ThemeMainFragment";
    @BindView(R.id.rv_themes)
    RecyclerView mRvThemes;

    private ThemeAdapter mThemeAdapter;

    private Disposable mDisposable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme, container, false);
        ButterKnife.bind(this, view);


        GridLayoutManager gridLayoutManager;
        mRvThemes.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRvThemes.addItemDecoration(new GridItemDecoration(10, 3));
        mRvThemes.setAdapter(mThemeAdapter = new ThemeAdapter(this));

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

        ZhihuHttp.getZhihuHttp().getThemes().subscribe(new Observer<ThemesJson>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(ThemesJson themesJson) {
                mThemeAdapter.addList(themesJson.getOthers());
                mThemeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "Subscriber onError()");
            }
        });
    }
}
