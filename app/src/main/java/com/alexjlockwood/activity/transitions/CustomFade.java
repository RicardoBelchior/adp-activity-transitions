package com.alexjlockwood.activity.transitions;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.transition.ChangeBounds;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ricardobelchior on 09/03/2017.
 */
public class CustomFade extends ChangeBounds {


    private int fadeMode;

    public CustomFade(int fadeMode) {
        this.fadeMode = fadeMode;
    }

    public CustomFade(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private float getStartAlpha() {
        return fadeMode == Visibility.MODE_IN ? 0.0f : 1.0f;
    }

    private float getEndAlpha() {
        return fadeMode == Visibility.MODE_IN ? 1.0f : 0.0f;
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        View endView = endValues.view;

        final AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(endView, View.ALPHA, getStartAlpha(), getEndAlpha()),
                super.createAnimator(sceneRoot, startValues, endValues)
        );

        return set;
    }
}
