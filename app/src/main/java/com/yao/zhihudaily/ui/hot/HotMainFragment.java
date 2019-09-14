package com.yao.zhihudaily.ui.hot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.Hot;
import com.yao.zhihudaily.model.HotJson;
import com.yao.zhihudaily.net.ZhihuHttp;
import com.yao.zhihudaily.tool.SimpleDividerDecoration;
import com.yao.zhihudaily.ui.BaseFragment;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
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
public class HotMainFragment extends BaseFragment {

    private static final String TAG = "HotMainFragment";

    @BindView(R.id.rv_hots)
    RecyclerView mRvHots;

    private HotAdapter hotAdapter;

    private Disposable mDisposable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager linearLayoutManager;
        mRvHots.setLayoutManager(new LinearLayoutManager(getFragmentActivity()));
        //mRvHots.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRvHots.addItemDecoration(new SimpleDividerDecoration(getFragmentActivity()));
        mRvHots.setAdapter(hotAdapter = new HotAdapter(this));

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
        ZhihuHttp.getZhihuHttp().getHot().subscribe(new Observer<HotJson>() {

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
        });
    }

}