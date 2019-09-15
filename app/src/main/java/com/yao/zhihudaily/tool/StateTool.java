package com.yao.zhihudaily.tool;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yao.zhihudaily.R;


/**
 *
 * @author Yao
 * @date 2016/9/28
 */
public class StateTool {

    /**
     * 一大堆静态变量,根据项目需要可以随意修改.比如项目全部用同一张错误页面的图片.
     * 在一个项目中如果用好几张不同的错误页面图片,可以考虑改成成员变量.
     */
    //内容字体的大小,单位SP
    private static int CONTENT_TEXT_SIZE = 20;
    //提示字体的大小,单位SP
    private static int TIP_TEXT_SIZE = 12;

    //空页面图片,默认用的安卓sdk里面的图片,严重建议替换成一个256px左右的图片 默认使用android.R.drawable.ic_menu_close_clear_cancel
    private static  int sEmptyImageResId = R.mipmap.empty;
    //错误页面图片,默认用的安卓sdk里面的图片,严重建议替换成一个256px左右的图片 默认使用android.R.drawable.ic_menu_search
    private static  int sErrorImageResId = R.mipmap.error;

    //空页面文字
    private static String sEmptyText = "空页面";
    //错误页面文字
    private static String sErrorText = "错误页面";
    //加载页面文字
    private static String sLoadingText = "加载中...";
    //重载动作的文字提示
    private static String sReloadText = "点击重载";

    //图片的宽和高 可以用ViewGroup.LayoutParams.WRAP_CONTENT
    private static int sImageSidesLength = 128;
    //等待,错误,空页面提示的向上偏移
    private static int sOffset = 0;

    //使用淡入淡出动画
    private static boolean sUseAlphaAnimator = false;

    private ViewGroup sViewGroupRoot;
    private Context mContext;
    private View mContentView;
    private RelativeLayout mEmptyView;
    private RelativeLayout mErrorView;
    private RelativeLayout mProgressView;
    private View mCurrentView;

    private LinearLayout.LayoutParams mParamsChildrenWrapContent;
    private LinearLayout.LayoutParams mParamsChildrenImage;
    private LinearLayout.LayoutParams mParamsChildrenMarginBottom50;

    /**
     * 如果有多个孩子,调用此方法
     * @param root ViewGroup sViewGroupRoot
     */
    public StateTool(ViewGroup root) {
        this.sViewGroupRoot = root;
        if (root.getChildCount() > 1) {
            throw new RuntimeException("sViewGroupRoot view's children more than 1");
        }
        mContentView = root.getChildAt(0);

        init();
    }

    /**
     * 如果有多个孩子,调用此方法
     * @param root ViewGroup sViewGroupRoot
     * @param index 传孩子的位置
     */
    public StateTool(ViewGroup root, int index) {
        this.sViewGroupRoot = root;
        if (root.getChildCount() < index + 1) {
            throw new RuntimeException("Invalid index " + index +", size is " + root.getChildCount());
        }
        mContentView = root.getChildAt(index);

        init();
    }

    private void init() {
        mContext = sViewGroupRoot.getContext();
        mCurrentView = mContentView;

        mParamsChildrenWrapContent =  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mParamsChildrenImage =  new LinearLayout.LayoutParams(sImageSidesLength, sImageSidesLength);
        mParamsChildrenMarginBottom50 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mParamsChildrenMarginBottom50.setMargins(0, 0, 0, sOffset);//不margin不居中

        initEmptyView();
        initErrorView();
        initProgressView();
    }

    public void setEmptyAndErrorImageResId(int emptyImageResId, int errorImageResId) {
        sEmptyImageResId = emptyImageResId;
        sErrorImageResId = errorImageResId;
    }

    public void setEmptyAndErrorTextResId(String emptyText, String errorText, String reloadText, String loadingText) {
        sEmptyText = emptyText;
        sErrorText = errorText;
        sReloadText = reloadText;
        sLoadingText = loadingText;
    }

    public void showEmptyView(){
        if (sUseAlphaAnimator) {
            alphaHide(mCurrentView);
            alphaShow(mEmptyView);
        } else {
            mCurrentView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
        mCurrentView = mEmptyView;
    }

    public void showErrorView(){
        mCurrentView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
        mCurrentView = mErrorView;
    }

    public void showProgressView(){
        mCurrentView.setVisibility(View.GONE);
        mProgressView.setVisibility(View.VISIBLE);
        mCurrentView = mProgressView;
    }

    public void showContentView(){
        mCurrentView.setVisibility(View.GONE);
        mContentView.setVisibility(View.VISIBLE);
        mCurrentView = mContentView;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mEmptyView.setOnClickListener(onClickListener);
        mErrorView.setOnClickListener(onClickListener);
    }

    private void alphaShow(final View v){
        ObjectAnimator oa = ObjectAnimator.ofFloat(v, "alpha", 0, 1);
        oa.setDuration(500);
        oa.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                v.setVisibility(View.VISIBLE);
            }
        });
        oa.start();
    }

    private void alphaHide(final View v){
        ObjectAnimator oa = ObjectAnimator.ofFloat(v, "alpha", 1, 0);
        oa.setDuration(500);
        oa.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                v.setVisibility(View.GONE);
            }
        });
        oa.start();
    }

    private void initEmptyView() {
        mEmptyView = new RelativeLayout(mContext);

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        ImageView iv = new ImageView(mContext);
        iv.setImageResource(sEmptyImageResId);
        linearLayout.addView(iv, mParamsChildrenImage);

        TextView tvContent = new TextView(mContext);
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, CONTENT_TEXT_SIZE);
        tvContent.setText(sEmptyText);
        linearLayout.addView(tvContent, mParamsChildrenWrapContent);

        TextView tvTip = new TextView(mContext);
        tvTip.setTextSize(TypedValue.COMPLEX_UNIT_SP, TIP_TEXT_SIZE);
        tvTip.setText(sReloadText);
        linearLayout.addView(tvTip, mParamsChildrenMarginBottom50);

        RelativeLayout.LayoutParams paramsLinearLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsLinearLayout.addRule(RelativeLayout.CENTER_IN_PARENT);//这个是RelativeLayout的layout_centerInParent属性
        linearLayout.setGravity(Gravity.CENTER);//这个是LinearLayout的gravity属性
        mEmptyView.addView(linearLayout, paramsLinearLayout);

        sViewGroupRoot.addView(mEmptyView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mEmptyView.setVisibility(View.GONE);
    }

    private void initErrorView() {
        mErrorView = new RelativeLayout(mContext);

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        ImageView iv = new ImageView(mContext);
        iv.setImageResource(sErrorImageResId);
        linearLayout.addView(iv, mParamsChildrenImage);

        TextView tvContent = new TextView(mContext);
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, CONTENT_TEXT_SIZE);
        tvContent.setText(sErrorText);
        linearLayout.addView(tvContent, mParamsChildrenWrapContent);

        TextView tvTip = new TextView(mContext);
        tvTip.setTextSize(TypedValue.COMPLEX_UNIT_SP, TIP_TEXT_SIZE);
        tvTip.setText(sReloadText);
        linearLayout.addView(tvTip, mParamsChildrenMarginBottom50);

        RelativeLayout.LayoutParams paramsLinearLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsLinearLayout.addRule(RelativeLayout.CENTER_IN_PARENT);//这个是RelativeLayout的layout_centerInParent属性
        linearLayout.setGravity(Gravity.CENTER);//这个是LinearLayout的gravity属性
        mErrorView.addView(linearLayout, paramsLinearLayout);

        sViewGroupRoot.addView(mErrorView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mErrorView.setVisibility(View.GONE);
    }

    private void initProgressView() {
        mProgressView = new RelativeLayout(mContext);

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        ProgressBar pb = new ProgressBar(mContext);
        linearLayout.addView(pb, mParamsChildrenWrapContent);

        TextView tvContent = new TextView(mContext);
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, CONTENT_TEXT_SIZE);
        tvContent.setText(sLoadingText);
        linearLayout.addView(tvContent, mParamsChildrenMarginBottom50);

        RelativeLayout.LayoutParams paramsLinearLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsLinearLayout.addRule(RelativeLayout.CENTER_IN_PARENT);//这个是RelativeLayout的layout_centerInParent属性
        linearLayout.setGravity(Gravity.CENTER);//这个是LinearLayout的gravity属性
        mProgressView.addView(linearLayout, paramsLinearLayout);

        sViewGroupRoot.addView(mProgressView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

}