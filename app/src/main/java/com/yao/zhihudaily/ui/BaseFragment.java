package com.yao.zhihudaily.ui;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.umeng.analytics.MobclickAgent;
import com.yao.zhihudaily.R;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Yao
 * @date 2016/9/4
 */
public abstract class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";

    private FrameLayout mFragmentContainer;
    private RecyclerView mRecyclerView;

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
        Log.i(TAG, "BaseFragment.java - onPause() ---------- Fragment:" + this.getClass().getName());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
        Log.i(TAG, "BaseFragment.java - onResume() ---------- Fragment:" + this.getClass().getName());
    }

    @NonNull
    public FragmentActivity getFragmentActivity(){
        return Objects.requireNonNull(getActivity());
    }

    /**
     * Refresh
     * 如果是当前页面=点击页面, 则平滑移动到头部
     */
    public void refresh() {
        if (mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    /**
     * Called when a fragment will be displayed
     * 将会显示的淡入动画
     */
    public void willBeDisplayed() {
        // Do what you want here, for example animate the content
        if (mFragmentContainer != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            mFragmentContainer.startAnimation(fadeIn);
        }
    }

    /**
     * Called when a fragment will be hidden
     * 将会隐藏的淡出动画
     */
    public void willBeHidden() {
        if (mFragmentContainer != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            mFragmentContainer.startAnimation(fadeOut);
        }
    }

}
