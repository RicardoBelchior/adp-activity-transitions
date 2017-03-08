package com.list_detail_transition;

import android.os.Build;

import com.mirror.news.ui.view.list_detail_transition.support.DetailFragmentTransitionPresenterCompat;
import com.mirror.news.ui.view.list_detail_transition.support.DetailPagerTransitionPresenterCompat;
import com.mirror.news.ui.view.list_detail_transition.support.ListTransitionPresenterCompat;

/**
 * Created by ricardobelchior on 31/01/2017.
 */

public class TransitionPresentersFactory {

    public static DetailPagerTransitionPresenter create(DetailPagerTransitionPresenter.DetailPagerTransitionView view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new DetailPagerTransitionPresenterLollipop(view);
        } else {
            return new DetailPagerTransitionPresenterCompat();
        }
    }

    public static DetailFragmentTransitionPresenter create(DetailFragmentTransitionPresenter.DetailFragmentTransitionView view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new DetailFragmentTransitionPresenterLollipop(view);
        } else {
            return new DetailFragmentTransitionPresenterCompat();
        }
    }

    public static ListTransitionPresenter create(ListTransitionPresenter.ListTransitionView view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new ListTransitionPresenterLollipop(view);
        } else {
            return new ListTransitionPresenterCompat(view);
        }
    }

}
