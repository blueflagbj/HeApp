package com.ai.cmcchina.crm.view;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;


/* renamed from: com.ai.cmcchina.crm.view.PinnedExpandableListView */
public class PinnedExpandableListView extends ExpandableListView implements AbsListView.OnScrollListener {
    private static final float FINGER_WIDTH = 20.0f;
    private static final int MAX_ALPHA = 255;
    private PinnedExpandableListViewAdapter mAdapter;
    private float mDownX;
    private float mDownY;
    private View mHeaderView;
    private int mHeaderViewHeight;
    private int mHeaderViewWidth;
    private boolean mHeaderVisible;
    private int mOldState = -1;
    private OnClickListener mPinnedHeaderClickLisenter;

    /* renamed from: com.ai.cmcchina.crm.view.PinnedExpandableListView$PinnedExpandableListViewAdapter */
    public interface PinnedExpandableListViewAdapter {
        public static final int PINNED_HEADER_GONE = 0;
        public static final int PINNED_HEADER_PUSHED_UP = 2;
        public static final int PINNED_HEADER_VISIBLE = 1;

        void configurePinnedHeader(View view, int i, int i2, int i3);

        int getPinnedHeaderState(int i, int i2);
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        long flatPos = getExpandableListPosition(firstVisibleItem);
        configureHeaderView(ExpandableListView.getPackedPositionGroup(flatPos), ExpandableListView.getPackedPositionChild(flatPos));
    }

    public void setOnPinnedHeaderClickLisenter(OnClickListener listener) {
        this.mPinnedHeaderClickLisenter = listener;
    }

    public PinnedExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnScrollListener(this);
    }

    public void setPinnedHeaderView(View view) {
        this.mHeaderView = view;
        if (this.mHeaderView != null) {
            this.mHeaderView.setLayoutParams(new LayoutParams(-1, -2));
            setOnPinnedHeaderClickLisenter(new OnClickListener() {
                public void onClick(View v) {
                    PinnedExpandableListView.this.collapseGroup(ExpandableListView.getPackedPositionGroup(PinnedExpandableListView.this.getExpandableListPosition(PinnedExpandableListView.this.getFirstVisiblePosition())));
                }
            });
            setFadingEdgeLength(0);
        }
        requestLayout();
    }

    public void setAdapter(ExpandableListAdapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof PinnedExpandableListViewAdapter) {
            this.mAdapter = (PinnedExpandableListViewAdapter) adapter;
            return;
        }
        throw new RuntimeException("the adapter must implements PinnedExpandableListViewAdapter");
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mHeaderView != null) {
            measureChild(this.mHeaderView, widthMeasureSpec, heightMeasureSpec);
            this.mHeaderViewWidth = this.mHeaderView.getMeasuredWidth();
            this.mHeaderViewHeight = this.mHeaderView.getMeasuredHeight();
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        long flatPostion = getExpandableListPosition(getFirstVisiblePosition());
        int groupPos = ExpandableListView.getPackedPositionGroup(flatPostion);
        int childPos = ExpandableListView.getPackedPositionChild(flatPostion);
        int state = this.mAdapter.getPinnedHeaderState(groupPos, childPos);
        if (!(this.mHeaderView == null || this.mAdapter == null || state == this.mOldState)) {
            this.mOldState = state;
            this.mHeaderView.layout(0, 0, this.mHeaderViewWidth, this.mHeaderViewHeight);
        }
        configureHeaderView(groupPos, childPos);
    }

    public void configureHeaderView(int groupPosition, int childPosition) {
        int y;
        int alpha;
        if (this.mHeaderView != null && this.mAdapter != null) {
            switch (this.mAdapter.getPinnedHeaderState(groupPosition, childPosition)) {
                case 0:
                    this.mHeaderVisible = false;
                    return;
                case 1:
                    this.mAdapter.configurePinnedHeader(this.mHeaderView, groupPosition, childPosition, 255);
                    if (this.mHeaderView.getTop() != 0) {
                        this.mHeaderView.layout(0, 0, this.mHeaderViewWidth, this.mHeaderViewHeight);
                    }
                    this.mHeaderVisible = true;
                    return;
                case 2:
                    View firstView = getChildAt(0);
                    if (firstView != null) {
                        int bottom = firstView.getBottom();
                        int headerHeight = this.mHeaderView.getHeight();
                        if (bottom < headerHeight) {
                            y = bottom - headerHeight;
                            alpha = ((headerHeight + y) * 255) / headerHeight;
                        } else {
                            y = 0;
                            alpha = 255;
                        }
                        this.mAdapter.configurePinnedHeader(this.mHeaderView, groupPosition, childPosition, alpha);
                        if (this.mHeaderView.getTop() != y) {
                            this.mHeaderView.layout(0, y, this.mHeaderViewWidth, this.mHeaderViewHeight + y);
                        }
                        this.mHeaderVisible = true;
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        } catch (IndexOutOfBoundsException e) {
           e.printStackTrace();
        }
        if (this.mHeaderVisible) {
            drawChild(canvas, this.mHeaderView, getDrawingTime());
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mHeaderVisible) {
            switch (ev.getAction()) {
                case 0:
                    this.mDownX = ev.getX();
                    this.mDownY = ev.getY();
                    if (this.mDownX <= ((float) this.mHeaderViewWidth) && this.mDownY <= ((float) this.mHeaderViewHeight)) {
                        return true;
                    }
                case 1:
                    float x = ev.getX();
                    float y = ev.getY();
                    float offsetX = Math.abs(x - this.mDownX);
                    float offsetY = Math.abs(y - this.mDownY);
                    if (x <= ((float) this.mHeaderViewWidth) && y <= ((float) this.mHeaderViewHeight) && offsetX <= FINGER_WIDTH && offsetY <= FINGER_WIDTH) {
                        if (this.mPinnedHeaderClickLisenter == null) {
                            return true;
                        }
                        this.mPinnedHeaderClickLisenter.onClick(this.mHeaderView);
                        return true;
                    }
            }
        }
        return super.onTouchEvent(ev);
    }
}