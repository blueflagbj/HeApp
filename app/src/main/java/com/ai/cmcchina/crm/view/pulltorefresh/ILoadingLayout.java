package com.ai.cmcchina.crm.view.pulltorefresh;
/* renamed from: com.ai.cmcchina.crm.view.pulltorefresh.ILoadingLayout */
public interface ILoadingLayout {

    /* renamed from: com.ai.cmcchina.crm.view.pulltorefresh.ILoadingLayout$State */
    public enum State {
        NONE,
        RESET,
        PULL_TO_REFRESH,
        RELEASE_TO_REFRESH,
        REFRESHING,
        LOADING,
        NO_MORE_DATA
    }

    int getContentSize();

    State getState();

    void onPull(float f);

    void setState(State state);
}