package com.alexjlockwood.activity.transitions;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ricardobelchior on 08/03/2017.
 */

public class IconFadeTransition3 extends Transition {

    public static final String PROP_NAME_ALPHA = "alpha";

    private int fadeMode = Visibility.MODE_IN;

    public IconFadeTransition3(int fadeMode) {
        this.fadeMode = fadeMode;
    }

    public IconFadeTransition3(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private float getStartAlpha() {
        return fadeMode == Visibility.MODE_IN ? 0.0f : 1.0f;
    }

    private float getEndAlpha() {
        return fadeMode == Visibility.MODE_IN ? 1.0f : 0.0f;
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        float alpha = getStartAlpha();
        captureValues(transitionValues, alpha);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        float alpha = getEndAlpha();
        captureValues(transitionValues, alpha);
    }


    private void captureValues(TransitionValues transitionValues, float alpha) {
        if (transitionValues == null ||
                transitionValues.view == null ||
                transitionValues.view.getVisibility() != View.VISIBLE) {
            return;
        }

        transitionValues.values.put(PROP_NAME_ALPHA, alpha);
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null || endValues == null) {
            return null;
        }

        Float startValue = (Float) startValues.values.get(PROP_NAME_ALPHA);
        Float endValue = (Float) endValues.values.get(PROP_NAME_ALPHA);
        final View view = endValues.view;
        if (view == null || startValue == null || endValue == null) {
            return null;
        }
        view.setAlpha(startValue);

        addListener(new TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                view.setAlpha(getEndAlpha());
            }
        });


        Log.d("TAG", "start view: " + startValues.view);
        Log.d("TAG", "end view:   " + endValues.view);

        return ObjectAnimator.ofFloat(view, View.ALPHA, startValue, endValue);
    }

}
