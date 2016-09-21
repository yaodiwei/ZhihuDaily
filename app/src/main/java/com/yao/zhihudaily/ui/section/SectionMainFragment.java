package com.yao.zhihudaily.ui.section;

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
import com.yao.zhihudaily.model.Section;
import com.yao.zhihudaily.model.SectionsJson;
import com.yao.zhihudaily.net.OkHttpSync;
import com.yao.zhihudaily.net.UrlConstants;
import com.yao.zhihudaily.net.ZhihuHttp;
import com.yao.zhihudaily.tool.DividerItemDecoration;
import com.yao.zhihudaily.ui.MainFragment;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/7/22.
 */
public class SectionMainFragment extends MainFragment {

    private static final String TAG = "SectionMainFragment";

    private RecyclerView rvSections;

    private SectionAdapter sectionAdapter;

    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_section, container, false);

        rvSections = (RecyclerView) view.findViewById(R.id.rvSections);

        rvSections.setLayoutManager(linearLayoutManager = new LinearLayoutManager(getActivity()));
        rvSections.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        rvSections.setAdapter(sectionAdapter = new SectionAdapter(this));

        getSections();

        return view;
    }

    private void getSections() {
        Subscriber subscriber = new Subscriber<SectionsJson>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.toString());
            }

            @Override
            public void onNext(SectionsJson sectionsJson) {
                ArrayList<Section> sections = sectionsJson.getSections();
                sectionAdapter.addList(sections);
                sectionAdapter.notifyDataSetChanged();
            }
        };

        ZhihuHttp.getZhihuHttp().getSections(subscriber);
    }


    private void getSectionsOld() {
        Observable.create(new Observable.OnSubscribe<SectionsJson>() {

            @Override
            public void call(Subscriber<? super SectionsJson> subscriber) {
                try {
                    Response response = OkHttpSync.get(UrlConstants.SECTIONS);
                    if (response.isSuccessful()) {
                        Type listType = new TypeToken<ArrayList<Section>>(){}.getType();
                        String responseString = response.body().string();
                        responseString = responseString.substring(8, responseString.length()-1);
                        ArrayList<Section> sections = new Gson().fromJson(responseString, listType);
                        sectionAdapter.addList(sections);
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
                .subscribe(new Subscriber<SectionsJson>() {

                    @Override
                    public void onCompleted() {
                        sectionAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onNext(SectionsJson sectionsJson) {
                    }
                });
    }
}