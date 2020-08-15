package com.ai.cmcchina.crm.view.pulltorefresh;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ListView;
import com.ai.cmcchina.crm.view.pulltorefresh.ILoadingLayout;

/* renamed from: com.ai.cmcchina.crm.view.pulltorefresh.PullToRefreshListView */
public class PullToRefreshListView extends PullToRefreshBase<ListView> implements AbsListView.OnScrollListener {
    private ListView mListView;
    private LoadingLayout mLoadMoreFooterLayout;
    private AbsListView.OnScrollListener mScrollListener;

    public PullToRefreshListView(Context context) {
        this(context, (AttributeSet) null);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setPullLoadEnabled(false);
    }

    /* access modifiers changed from: protected */
    public ListView createRefreshableView(Context context, AttributeSet attrs) {
        ListView listView = new ListView(context);
        this.mListView = listView;
        listView.setOnScrollListener(this);
        return listView;
    }

    public void setHasMoreData(boolean hasMoreData) {
        if (!hasMoreData) {
            if (this.mLoadMoreFooterLayout != null) {
                this.mLoadMoreFooterLayout.setState(ILoadingLayout.State.NO_MORE_DATA);
            }
            LoadingLayout footerLoadingLayout = getFooterLoadingLayout();
            if (footerLoadingLayout != null) {
                footerLoadingLayout.setState(ILoadingLayout.State.NO_MORE_DATA);
            }
        }
    }

    public void setOnScrollListener(AbsListView.OnScrollListener l) {
        this.mScrollListener = l;
    }

    /* access modifiers changed from: protected */
    public boolean isReadyForPullUp() {
        return isLastItemVisible();
    }

    /* access modifiers changed from: protected */
    public boolean isReadyForPullDown() {
        return isFirstItemVisible();
    }

    /* access modifiers changed from: protected */
    public void startLoading() {
        super.startLoading();
        if (this.mLoadMoreFooterLayout != null) {
            this.mLoadMoreFooterLayout.setState(ILoadingLayout.State.REFRESHING);
        }
    }

    public void onPullUpRefreshComplete() {
        super.onPullUpRefreshComplete();
        if (this.mLoadMoreFooterLayout != null) {
            this.mLoadMoreFooterLayout.setState(ILoadingLayout.State.RESET);
        }
    }

    public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
        super.setScrollLoadEnabled(scrollLoadEnabled);
        if (scrollLoadEnabled) {
            if (this.mLoadMoreFooterLayout == null) {
                this.mLoadMoreFooterLayout = new FooterLoadingLayout(getContext());
            }
            if (this.mLoadMoreFooterLayout.getParent() == null) {
                this.mListView.addFooterView(this.mLoadMoreFooterLayout, (Object) null, false);
            }
            this.mLoadMoreFooterLayout.show(true);
        } else if (this.mLoadMoreFooterLayout != null) {
            this.mLoadMoreFooterLayout.show(false);
        }
    }

    public LoadingLayout getFooterLoadingLayout() {
        if (isScrollLoadEnabled()) {
            return this.mLoadMoreFooterLayout;
        }
        return super.getFooterLoadingLayout();
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isScrollLoadEnabled() && hasMoreData() && ((scrollState == 0 || scrollState == 2) && isReadyForPullUp())) {
            startLoading();
        }
        if (this.mScrollListener != null) {
            this.mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (this.mScrollListener != null) {
            this.mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    private boolean hasMoreData() {
        if (this.mLoadMoreFooterLayout == null || this.mLoadMoreFooterLayout.getState() != ILoadingLayout.State.NO_MORE_DATA) {
            return true;
        }
        return false;
    }

    private boolean isFirstItemVisible() {
        int mostTop;
        Adapter adapter = this.mListView.getAdapter();
        if (adapter == null || adapter.isEmpty()) {
            return true;
        }
        if (this.mListView.getChildCount() > 0) {
            mostTop = this.mListView.getChildAt(0).getTop();
        } else {
            mostTop = 0;
        }
        if (mostTop < 0) {
            return false;
        }
        return true;
    }

    private boolean isLastItemVisible() {
        View lastVisibleChild;
        Adapter adapter = this.mListView.getAdapter();
        if (adapter == null || adapter.isEmpty()) {
            return true;
        }
        int lastItemPosition = adapter.getCount() - 1;
        int lastVisiblePosition = this.mListView.getLastVisiblePosition();
        if (lastVisiblePosition < lastItemPosition - 1 || (lastVisibleChild = this.mListView.getChildAt(Math.min(lastVisiblePosition - this.mListView.getFirstVisiblePosition(), this.mListView.getChildCount() - 1))) == null) {
            return false;
        }
        if (lastVisibleChild.getBottom() > this.mListView.getBottom()) {
            return false;
        }
        return true;
    }
}