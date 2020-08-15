package com.ai.cmcchina.crm.view.pulltorefresh;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.ai.cmcchina.crm.view.pulltorefresh.ILoadingLayout;

/* renamed from: com.ai.cmcchina.crm.view.pulltorefresh.LoadingLayout */
public abstract class LoadingLayout extends FrameLayout implements ILoadingLayout {
    private View mContainer;
    private ILoadingLayout.State mCurState;
    private ILoadingLayout.State mPreState;

    /* access modifiers changed from: protected */
    public abstract View createLoadingView(Context context, AttributeSet attributeSet);

    public abstract int getContentSize();

    public LoadingLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCurState = ILoadingLayout.State.NONE;
        this.mPreState = ILoadingLayout.State.NONE;
        init(context, attrs);
    }

    /* access modifiers changed from: protected */
    public void init(Context context, AttributeSet attrs) {
        this.mContainer = createLoadingView(context, attrs);
        if (this.mContainer == null) {
            throw new NullPointerException("Loading view can not be null.");
        }
        addView(this.mContainer, new LayoutParams(-1, -2));
    }

    public void show(boolean show) {
        boolean z;
        ViewGroup.LayoutParams params;
        int i = 0;
        if (getVisibility() == 0) {
            z = true;
        } else {
            z = false;
        }
        if (show != z && (params = this.mContainer.getLayoutParams()) != null) {
            if (show) {
                params.height = -2;
            } else {
                params.height = 0;
            }
            if (!show) {
                i = 4;
            }
            setVisibility(i);
        }
    }

    public void setLastUpdatedLabel(CharSequence label) {
    }

    public void setLoadingDrawable(Drawable drawable) {
    }

    public void setPullLabel(CharSequence pullLabel) {
    }

    public void setRefreshingLabel(CharSequence refreshingLabel) {
    }

    public void setReleaseLabel(CharSequence releaseLabel) {
    }

    public void setState(ILoadingLayout.State state) {
        if (this.mCurState != state) {
            this.mPreState = this.mCurState;
            this.mCurState = state;
            onStateChanged(state, this.mPreState);
        }
    }

    public ILoadingLayout.State getState() {
        return this.mCurState;
    }

    public void onPull(float scale) {
    }

    /* access modifiers changed from: protected */
    public ILoadingLayout.State getPreState() {
        return this.mPreState;
    }

    /* access modifiers changed from: protected */
    public void onStateChanged(ILoadingLayout.State curState, ILoadingLayout.State oldState) {
        switch (curState) {
            case RESET:
                onReset();
                return;
            case RELEASE_TO_REFRESH:
                onReleaseToRefresh();
                return;
            case PULL_TO_REFRESH:
                onPullToRefresh();
                return;
            case REFRESHING:
                onRefreshing();
                return;
            case NO_MORE_DATA:
                onNoMoreData();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void onReset() {
    }

    /* access modifiers changed from: protected */
    public void onPullToRefresh() {
    }

    /* access modifiers changed from: protected */
    public void onReleaseToRefresh() {
    }

    /* access modifiers changed from: protected */
    public void onRefreshing() {
    }

    /* access modifiers changed from: protected */
    public void onNoMoreData() {
    }
}