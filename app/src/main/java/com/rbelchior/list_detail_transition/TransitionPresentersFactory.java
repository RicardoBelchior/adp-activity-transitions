package com.rbelchior.list_detail_transition;

import android.os.Build;

import com.rbelchior.list_detail_transition.support.DetailFragmentTransitionPresenterCompat;
import com.rbelchior.list_detail_transition.support.DetailPagerTransitionPresenterCompat;
import com.rbelchior.list_detail_transition.support.ListTransitionPresenterCompat;


/**
 * Created by ricardobelchior on 31/01/2017.
 */

public class TransitionPresentersFactory {

    public static DetailPagerTransitionPresenter create(DetailPagerTransitionPresenter.DetailPagerTransitionView view) {
        if (isTransitionsSupported()) {
            return new DetailPagerTransitionPresenterLollipop(view);
        } else {
            return new DetailPagerTransitionPresenterCompat();
        }
    }

    public static DetailFragmentTransitionPresenter create(DetailFragmentTransitionPresenter.DetailFragmentTransitionView view) {
        if (isTransitionsSupported()) {
            return new DetailFragmentTransitionPresenterLollipop(view);
        } else {
            return new DetailFragmentTransitionPresenterCompat();
        }
    }

    public static ListTransitionPresenter create(ListTransitionPresenter.ListTransitionView view) {
        if (isTransitionsSupported()) {
            return new ListTransitionPresenterLollipop(view);
        } else {
            return new ListTransitionPresenterCompat(view);
        }
    }

    private static boolean isTransitionsSupported() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

}
