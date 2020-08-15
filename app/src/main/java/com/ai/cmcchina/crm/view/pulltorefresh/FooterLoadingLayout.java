package com.ai.cmcchina.crm.view.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ai.cmcchina.crm.view.pulltorefresh.ILoadingLayout;
import com.heclient.heapp.R;

/* renamed from: com.ai.cmcchina.crm.view.pulltorefresh.FooterLoadingLayout */
public class FooterLoadingLayout extends LoadingLayout {
    private TextView mHintView;
    private ProgressBar mProgressBar;

    public FooterLoadingLayout(Context context) {
        super(context);
        init(context);
    }

    public FooterLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mProgressBar = (ProgressBar) findViewById(R.id.pull_to_load_footer_progressbar);
        this.mHintView = (TextView) findViewById(R.id.pull_to_load_footer_hint_textview);
        setState(State.RESET);
    }

    /* access modifiers changed from: protected */
    public View createLoadingView(Context context, AttributeSet attrs) {
        return LayoutInflater.from(context).inflate(R.layout.pull_to_load_footer, (ViewGroup) null);
    }

    public void setLastUpdatedLabel(CharSequence label) {
    }

    public int getContentSize() {
        View view = findViewById(R.id.pull_to_load_footer_content);
        if (view != null) {
            return view.getHeight();
        }
        return (int) (getResources().getDisplayMetrics().density * 40.0f);
    }

    /* access modifiers changed from: protected */
    public void onStateChanged(State curState, State oldState) {
        this.mProgressBar.setVisibility(View.GONE);
        this.mHintView.setVisibility(View.INVISIBLE);
        super.onStateChanged(curState, oldState);
    }

    /* access modifiers changed from: protected */
    public void onReset() {
        this.mHintView.setText(R.string.pull_to_refresh_header_hint_loading);
    }

    /* access modifiers changed from: protected */
    public void onPullToRefresh() {
        this.mHintView.setVisibility(View.VISIBLE);
        this.mHintView.setText(R.string.pull_to_refresh_header_hint_normal2);
    }

    /* access modifiers changed from: protected */
    public void onReleaseToRefresh() {
        this.mHintView.setVisibility(View.VISIBLE);
        this.mHintView.setText(R.string.pull_to_refresh_header_hint_ready);
    }

    /* access modifiers changed from: protected */
    public void onRefreshing() {
        this.mProgressBar.setVisibility(View.VISIBLE);
        this.mHintView.setVisibility(View.VISIBLE);
        this.mHintView.setText(R.string.pull_to_refresh_header_hint_loading);
    }

    /* access modifiers changed from: protected */
    public void onNoMoreData() {
        this.mHintView.setVisibility(View.VISIBLE);
        this.mHintView.setText(R.string.pushmsg_center_no_more_msg);
    }
}