package com.rbelchior.list_detail_transition;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

/**
 * {@link FragmentStatePagerAdapter} where you can access the currently visible fragment instance.
 */
public abstract class CurrentFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private Fragment currentFragment;

    public CurrentFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        currentFragment = (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }
}
