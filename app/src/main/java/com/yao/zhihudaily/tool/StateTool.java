package com.yao.zhihudaily.tool;

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


/**
 * Created by Administrator on 2016/9/28.
 */

public class StateTool {

    //内容字体的大小,单位SP
    private static int CONTENT_TEXT_SIZE = 20;
    //提示字体的大小,单位SP
    private static int TIP_TEXT_SIZE = 12;

    //空页面图片,默认用的安卓sdk里面的图片,严重建议替换
    private static  int emptyImageResId = android.R.drawable.ic_menu_close_clear_cancel;
    //错误页面图片,默认用的安卓sdk里面的图片,严重建议替换
    private static  int errorImageResId = android.R.drawable.ic_menu_search;

    //空页面文字
    private static String emptyText = "空页面";
    //错误页面文字
    private static String errorText = "错误页面";
    //加载页面文字
    private static String loadingText = "加载中...";
    //重载动作的文字提示
    private static String reloadText = "点击重载";

    //图片的宽和高 可以用ViewGroup.LayoutParams.WRAP_CONTENT
    private static int imageSidesLength = 256;

    private ViewGroup root;
    private Context ctx;
    private View contentView;
    private RelativeLayout emptyView;
    private RelativeLayout errorView;
    private RelativeLayout progressView;
    private View currentView;

    private LinearLayout.LayoutParams paramsChildrenWrapContent;
    private LinearLayout.LayoutParams paramsChildrenImage;
    private LinearLayout.LayoutParams paramsChildrenMarginBottom50;

    public StateTool(ViewGroup root) {
        this.root = root;
        ctx = root.getContext();
        if (root.getChildCount() > 1) {
            throw new RuntimeException("root view's children more than 1");
        }
        contentView = root.getChildAt(0);
        currentView = contentView;

        paramsChildrenWrapContent =  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsChildrenImage =  new LinearLayout.LayoutParams(imageSidesLength, imageSidesLength);
        paramsChildrenMarginBottom50 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsChildrenMarginBottom50.setMargins(0, 0, 0, 500);//不margin不居中

        initEmptyView();
        initErrorView();
        initProgressView();
    }

    public void setEmptyAndErrorImageResId(int emptyImageResId, int errorImageResId) {
        this.emptyImageResId = emptyImageResId;
        this.errorImageResId = errorImageResId;
    }

    public void setEmptyAndErrorTextResId(String emptyText, String errorText, String reloadText, String loadingText) {
        this.emptyText = emptyText;
        this.errorText = errorText;
        this.reloadText = reloadText;
        this.loadingText = loadingText;
    }

    public void showEmptyView(){
        currentView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        currentView = emptyView;
    }

    public void showErrorView(){
        currentView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        currentView = errorView;
    }

    public void showProgressView(){
        currentView.setVisibility(View.GONE);
        progressView.setVisibility(View.VISIBLE);
        currentView = progressView;
    }

    public void showContentView(){
        currentView.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
        currentView = contentView;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        emptyView.setOnClickListener(onClickListener);
        errorView.setOnClickListener(onClickListener);
    }

    private void initEmptyView() {
        emptyView = new RelativeLayout(ctx);

        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        ImageView iv = new ImageView(ctx);
        iv.setImageResource(emptyImageResId);
        linearLayout.addView(iv, paramsChildrenImage);

        TextView tvContent = new TextView(ctx);
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, CONTENT_TEXT_SIZE);
        tvContent.setText(emptyText);
        linearLayout.addView(tvContent, paramsChildrenWrapContent);

        TextView tvTip = new TextView(ctx);
        tvTip.setTextSize(TypedValue.COMPLEX_UNIT_SP, TIP_TEXT_SIZE);
        tvTip.setText(reloadText);
        linearLayout.addView(tvTip, paramsChildrenMarginBottom50);

        RelativeLayout.LayoutParams paramsLinearLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsLinearLayout.addRule(RelativeLayout.CENTER_IN_PARENT);//这个是RelativeLayout的layout_centerInParent属性
        linearLayout.setGravity(Gravity.CENTER);//这个是LinearLayout的gravity属性
        emptyView.addView(linearLayout, paramsLinearLayout);

        root.addView(emptyView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        emptyView.setVisibility(View.GONE);
    }

    private void initErrorView() {
        errorView = new RelativeLayout(ctx);

        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        ImageView iv = new ImageView(ctx);
        iv.setImageResource(errorImageResId);
        linearLayout.addView(iv, paramsChildrenImage);

        TextView tvContent = new TextView(ctx);
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, CONTENT_TEXT_SIZE);
        tvContent.setText(errorText);
        linearLayout.addView(tvContent, paramsChildrenWrapContent);

        TextView tvTip = new TextView(ctx);
        tvTip.setTextSize(TypedValue.COMPLEX_UNIT_SP, TIP_TEXT_SIZE);
        tvTip.setText(reloadText);
        linearLayout.addView(tvTip, paramsChildrenMarginBottom50);

        RelativeLayout.LayoutParams paramsLinearLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsLinearLayout.addRule(RelativeLayout.CENTER_IN_PARENT);//这个是RelativeLayout的layout_centerInParent属性
        linearLayout.setGravity(Gravity.CENTER);//这个是LinearLayout的gravity属性
        errorView.addView(linearLayout, paramsLinearLayout);

        root.addView(errorView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        errorView.setVisibility(View.GONE);
    }

    private void initProgressView() {
        progressView = new RelativeLayout(ctx);

        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        ProgressBar pb = new ProgressBar(ctx);
        linearLayout.addView(pb, paramsChildrenWrapContent);

        TextView tvContent = new TextView(ctx);
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, CONTENT_TEXT_SIZE);
        tvContent.setText(loadingText);
        linearLayout.addView(tvContent, paramsChildrenMarginBottom50);

        RelativeLayout.LayoutParams paramsLinearLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsLinearLayout.addRule(RelativeLayout.CENTER_IN_PARENT);//这个是RelativeLayout的layout_centerInParent属性
        linearLayout.setGravity(Gravity.CENTER);//这个是LinearLayout的gravity属性
        progressView.addView(linearLayout, paramsLinearLayout);

        root.addView(progressView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

}
