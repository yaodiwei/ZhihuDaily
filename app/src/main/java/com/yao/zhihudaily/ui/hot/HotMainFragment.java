package com.yao.zhihudaily.ui.hot;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.Hot;
import com.yao.zhihudaily.model.HotJson;
import com.yao.zhihudaily.net.ZhihuHttp;
import com.yao.zhihudaily.tool.SimpleDividerDecoration;
import com.yao.zhihudaily.ui.MainFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2016/7/22.
 */
public class HotMainFragment extends MainFragment {

    private static final String TAG = "HotMainFragment";
    @BindView(R.id.rvHots)
    RecyclerView rvHots;


    private HotAdapter hotAdapter;

    private LinearLayoutManager linearLayoutManager;
    private Disposable mDisposable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        ButterKnife.bind(this, view);

        rvHots.setLayoutManager(linearLayoutManager = new LinearLayoutManager(getActivity()));
//        rvHots.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        rvHots.addItemDecoration(new SimpleDividerDecoration(getActivity()));
        rvHots.setAdapter(hotAdapter = new HotAdapter(this));

        getHot();


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    private void getHot() {
        Observer subscriber = new Observer<HotJson>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(HotJson hotJson) {
                ArrayList<Hot> hots = hotJson.getHots();
                hotAdapter.addList(hots);
                hotAdapter.notifyDataSetChanged();
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "Subscriber onError()");
            }
        };

        ZhihuHttp.getZhihuHttp().getHot(subscriber);
    }

}