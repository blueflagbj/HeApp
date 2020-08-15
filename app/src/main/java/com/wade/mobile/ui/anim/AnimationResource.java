package com.wade.mobile.ui.anim;

import android.content.Context;
import android.view.animation.Animation;
import android.widget.ViewFlipper;

import com.heclient.heapp.R;


/* renamed from: com.wade.mobile.ui.anim.AnimationResource */
public class AnimationResource {
    public static int HYPERSPACE = 1;
    public static int PUSHLEFT = 2;
    public static int PUSHRIGHT = 3;
    public static int SCALEACTION = 4;
    public static int SHUTOPEN = 5;
    public static int[] hyperspace = {R.anim.hyperspace_in, R.anim.hyperspace_out};
    public static int[] pushLeft = {R.anim.push_left_in, R.anim.push_left_out};
    public static int[] pushRight = {R.anim.push_right_in, R.anim.push_right_out};
    public static int[] scaleAction = {R.anim.scale_action, R.anim.scale_action};
    public static int[] shutOpen = {R.anim.shut_open_in, R.anim.shut_open_out};

    public static void setAnimation(Context context, ViewFlipper flipper, int kind) {
        switch (kind) {
            case 1:
                setHyperspace(context, flipper);
                return;
            case 2:
                setLeft(context, flipper);
                return;
            case 3:
                setRight(context, flipper);
                return;
            case 4:
                setScaleAction(context, flipper);
                return;
            case 5:
                setShutOpen(context, flipper);
                return;
            default:
                return;
        }
    }

    public static void setLeft(Context context, ViewFlipper flipper) {
        setAnimation(context, flipper, pushLeft[0], pushLeft[1]);
    }

    public static void setRight(Context context, ViewFlipper flipper) {
        setAnimation(context, flipper, pushRight[0], pushRight[1]);
    }

    public static void setHyperspace(Context context, ViewFlipper flipper) {
        setAnimation(context, flipper, hyperspace[0], hyperspace[1]);
    }

    public static void setScaleAction(Context context, ViewFlipper flipper) {
        setAnimation(context, flipper, scaleAction[0], scaleAction[1]);
    }

    public static void setShutOpen(Context context, ViewFlipper flipper) {
        setAnimation(context, flipper, shutOpen[0], shutOpen[1]);
    }

    public static void setAnimation(Context context, ViewFlipper flipper, int in, int out) {
        if (in > 0) {
            flipper.setInAnimation(context.getApplicationContext(), in);
        }
        if (out > 0) {
            flipper.setOutAnimation(context.getApplicationContext(), out);
        }
    }

    public static void clearAnimation(ViewFlipper flipper) {
        flipper.setOutAnimation((Animation) null);
        flipper.setInAnimation((Animation) null);
    }

    public static boolean isAnimation(ViewFlipper flipper) {
        return (flipper.getInAnimation() == null && flipper.getOutAnimation() == null) ? false : true;
    }

    public static int[] getAnimation(int kind) {
        switch (kind) {
            case 1:
                return hyperspace;
            case 2:
                return pushLeft;
            case 3:
                return pushRight;
            case 4:
                return scaleAction;
            case 5:
                return shutOpen;
            default:
                return null;
        }
    }
}