package ru.vetatto.investing.investing;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PortfolioFragmentPagerAdapter extends FragmentStatePagerAdapter {
    static final int PAGE_COUNT = 1;
    public PortfolioFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
