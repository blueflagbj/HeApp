package com.wade.mobile.ui.view;


import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import com.wade.mobile.common.MobileLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* renamed from: com.wade.mobile.ui.view.FlipperLayout */
public class FlipperLayout extends FrameLayout {
    private static final String TAG = FlipperLayout.class.getSimpleName();
    private Map<Integer, Animation> animCache;
    private Animation backInAnim;
    private Animation backOutAnim;
    private Context context;
    private int currIndex;
    private Animation inAnim;
    private Animation outAnim;
    private View preCurrView;
    private ArrayList<View> viewCache;

    /* renamed from: com.wade.mobile.ui.view.FlipperLayout$FlipperAnimationType */
    public static class FlipperAnimationType {
        public static int IPUAnimationHyperspace = 3;
        public static int IPUAnimationNone = 0;
        public static int IPUAnimationPushFromRight = 1;
        public static int IPUAnimationScale = 2;
        public static int IPUAnimationShutOpen = 4;
    }

    public FlipperLayout(Context context2) {
        super(context2);
        this.context = context2;
        init();
    }

    private void init() {
        this.currIndex = -1;
        this.viewCache = new ArrayList<>(3);
        this.animCache = new HashMap(4);
    }

    private boolean inRange(int index) {
        return index > -1 && index < this.viewCache.size();
    }

    public View getNextView() {
        int nextIndex = this.currIndex + 1;
        if (inRange(nextIndex)) {
            return this.viewCache.get(nextIndex);
        }
        return null;
    }

    public void addNextView(View view) {
        this.viewCache.add(view);
        view.setId(this.viewCache.size());
    }

    public void setPreCurrView(View preCurrView2) {
        this.preCurrView = preCurrView2;
    }

    public View getCurrView() {
        if (this.preCurrView != null) {
            return this.preCurrView;
        }
        if (inRange(this.currIndex)) {
            return this.viewCache.get(this.currIndex);
        }
        return null;
    }

    public int showNextView() {
        View currView;
        int nextIndex = this.currIndex + 1;
        if (!inRange(nextIndex)) {
            MobileLog.w(TAG, "请添加下一级视图");
            return this.currIndex;
        }
        if (inRange(this.currIndex)) {
            currView = this.viewCache.get(this.currIndex);
        } else {
            currView = null;
        }
        if (currView != null) {
            if (this.outAnim != null) {
                currView.startAnimation(this.outAnim);
            }
            removeView(currView);
        }
        View child = this.viewCache.get(nextIndex);
        addView(child);
        if (this.inAnim != null) {
            child.startAnimation(this.inAnim);
        }
        this.currIndex++;
        this.preCurrView = null;
        return this.currIndex;
    }

    public void back() {
        if (!isCanBack()) {
            MobileLog.w(TAG, "根页面不能返回!");
            return;
        }
        int preIndex = this.currIndex - 1;
        View currView = this.viewCache.get(this.currIndex);
        View preView = this.viewCache.get(preIndex);
        if (currView != null) {
            if (this.backOutAnim != null) {
                currView.startAnimation(this.backOutAnim);
            }
            removeView(currView);
        }
        addView(preView);
        if (this.backInAnim != null) {
            preView.startAnimation(this.backInAnim);
        }
        this.currIndex--;
    }

    public boolean isCanBack() {
        return this.currIndex > 0;
    }

    public void setAnimation(int inAnim2, int outAnim2) {
        if (this.animCache.get(Integer.valueOf(inAnim2)) == null) {
            this.animCache.put(Integer.valueOf(inAnim2), AnimationUtils.loadAnimation(this.context, inAnim2));
        }
        if (this.animCache.get(Integer.valueOf(outAnim2)) == null) {
            this.animCache.put(Integer.valueOf(outAnim2), AnimationUtils.loadAnimation(this.context, outAnim2));
        }
        this.inAnim = this.animCache.get(Integer.valueOf(inAnim2));
        this.outAnim = this.animCache.get(Integer.valueOf(outAnim2));
    }

    public void setBackAnimation(int backInAnim2, int backOutAnim2) {
        if (this.animCache.get(Integer.valueOf(backInAnim2)) == null) {
            this.animCache.put(Integer.valueOf(backInAnim2), AnimationUtils.loadAnimation(this.context, backInAnim2));
        }
        if (this.animCache.get(Integer.valueOf(backOutAnim2)) == null) {
            this.animCache.put(Integer.valueOf(backOutAnim2), AnimationUtils.loadAnimation(this.context, backOutAnim2));
        }
        this.backInAnim = this.animCache.get(Integer.valueOf(backInAnim2));
        this.backOutAnim = this.animCache.get(Integer.valueOf(backOutAnim2));
    }

    public void clearAnimation() {
        this.inAnim = null;
        this.outAnim = null;
        this.backInAnim = null;
        this.backOutAnim = null;
    }

    public void clearBackStack() {
        this.currIndex = -1;
        removeAllViews();
    }
}