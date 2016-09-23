package com.yao.zhihudaily.ui.hot;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.Hot;
import com.yao.zhihudaily.model.HotJson;
import com.yao.zhihudaily.net.OkHttpSync;
import com.yao.zhihudaily.net.UrlConstants;
import com.yao.zhihudaily.net.ZhihuHttp;
import com.yao.zhihudaily.tool.DividerItemDecoration;
import com.yao.zhihudaily.ui.MainFragment;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/7/22.
 */
public class HotMainFragment extends MainFragment {

    private static final String TAG = "HotMainFragment";
    @BindView(R.id.rvHots)
    RecyclerView rvHots;


    private HotAdapter hotAdapter;

    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        ButterKnife.bind(this, view);

        rvHots.setLayoutManager(linearLayoutManager = new LinearLayoutManager(getActivity()));
        rvHots.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        rvHots.setAdapter(hotAdapter = new HotAdapter(this));

        getHot();


        return view;
    }

    private void getHot() {
        Subscriber subscriber = new Subscriber<HotJson>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.toString());
            }

            @Override
            public void onNext(HotJson hotJson) {
                ArrayList<Hot> hots = hotJson.getHots();
                hotAdapter.addList(hots);
                hotAdapter.notifyDataSetChanged();
            }
        };

        ZhihuHttp.getZhihuHttp().getHot(subscriber);
    }

    private void getHotOld() {
        Observable.create(new Observable.OnSubscribe<Response>() {

            @Override
            public void call(Subscriber<? super Response> subscriber) {
                try {
                    Response response = OkHttpSync.get(UrlConstants.HOT);
                    if (response.isSuccessful()) {
                        Type listType = new TypeToken<ArrayList<Hot>>() {
                        }.getType();
                        String responseString = response.body().string();
                        responseString = responseString.substring(10, responseString.length() - 1);
                        ArrayList<Hot> hots = new Gson().fromJson(responseString, listType);
                        Log.e("YAO", "HotMainFragment.java - call() ---------- " + hots);
                        hotAdapter.addList(hots);
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new Exception("error"));
                    }
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response>() {

                    @Override
                    public void onCompleted() {
                        hotAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onNext(Response response) {
                    }
                });
    }

}