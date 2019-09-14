package com.yao.zhihudaily.ui.section;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.Section;
import com.yao.zhihudaily.model.SectionsJson;
import com.yao.zhihudaily.net.ZhihuHttp;
import com.yao.zhihudaily.tool.SimpleDividerDecoration;
import com.yao.zhihudaily.ui.BaseFragment;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * @author Yao
 * @date 2016/7/22
 */
public class SectionMainFragment extends BaseFragment {

    private static final String TAG = "SectionMainFragment";

    private SectionAdapter mSectionAdapter;

    private Disposable mDisposable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_section, container, false);

        RecyclerView rvSections = view.findViewById(R.id.rv_sections);

        LinearLayoutManager linearLayoutManager;
        rvSections.setLayoutManager(new LinearLayoutManager(getActivity()));
        //rvSections.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        rvSections.addItemDecoration(new SimpleDividerDecoration(getFragmentActivity()));
        rvSections.setAdapter(mSectionAdapter = new SectionAdapter(this));

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
        ZhihuHttp.getZhihuHttp().getSections().subscribe(new Observer<SectionsJson>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(SectionsJson sectionsJson) {
                ArrayList<Section> sections = sectionsJson.getSections();
                mSectionAdapter.addList(sections);
                mSectionAdapter.notifyDataSetChanged();
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