package com.yao.zhihudaily.ui.section;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.Section;
import com.yao.zhihudaily.model.SectionsJson;
import com.yao.zhihudaily.net.ZhihuHttp;
import com.yao.zhihudaily.tool.SimpleDividerDecoration;
import com.yao.zhihudaily.ui.MainFragment;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2016/7/22.
 */
public class SectionMainFragment extends MainFragment {

    private static final String TAG = "SectionMainFragment";

    private RecyclerView rvSections;

    private SectionAdapter sectionAdapter;

    private LinearLayoutManager linearLayoutManager;
    private Disposable mDisposable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_section, container, false);

        rvSections = (RecyclerView) view.findViewById(R.id.rvSections);

        rvSections.setLayoutManager(linearLayoutManager = new LinearLayoutManager(getActivity()));
//        rvSections.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        rvSections.addItemDecoration(new SimpleDividerDecoration(getActivity()));
        rvSections.setAdapter(sectionAdapter = new SectionAdapter(this));

        getSections();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    private void getSections() {
        Observer subscriber = new Observer<SectionsJson>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(SectionsJson sectionsJson) {
                ArrayList<Section> sections = sectionsJson.getSections();
                sectionAdapter.addList(sections);
                sectionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "Subscriber onError()");
            }
        };

        ZhihuHttp.getZhihuHttp().getSections(subscriber);
    }
}