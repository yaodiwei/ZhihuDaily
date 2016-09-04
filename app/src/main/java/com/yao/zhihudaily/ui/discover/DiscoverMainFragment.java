package com.yao.zhihudaily.ui.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yao.zhihudaily.R;
import com.yao.zhihudaily.ui.MainFragment;

/**
 * Created by Administrator on 2016/7/22.
 */
public class DiscoverMainFragment extends MainFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        return view;
    }
}
