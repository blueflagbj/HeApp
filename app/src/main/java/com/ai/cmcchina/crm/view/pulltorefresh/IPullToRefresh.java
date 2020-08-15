package com.ai.cmcchina.crm.view.pulltorefresh;

import android.view.View;
import com.ai.cmcchina.crm.view.pulltorefresh.PullToRefreshBase;

/* renamed from: com.ai.cmcchina.crm.view.pulltorefresh.IPullToRefresh */
public interface IPullToRefresh<T extends View> {
    LoadingLayout getFooterLoadingLayout();

    LoadingLayout getHeaderLoadingLayout();

    T getRefreshableView();

    boolean isPullLoadEnabled();

    boolean isPullRefreshEnabled();

    boolean isScrollLoadEnabled();

    void onPullDownRefreshComplete();

    void onPullUpRefreshComplete();

    void setLastUpdatedLabel(CharSequence charSequence);

    void setOnRefreshListener(PullToRefreshBase.OnRefreshListener<T> onRefreshListener);

    void setPullLoadEnabled(boolean z);

    void setPullRefreshEnabled(boolean z);

    void setScrollLoadEnabled(boolean z);
}