package ru.vetatto.investing.investing;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ru.vetatto.investing.investing.IndexViewPager.BillingFragment;
import ru.vetatto.investing.investing.IndexViewPager.PifFragment;

public class PortfolioFragmentPagerAdapter extends FragmentStatePagerAdapter {
    static final int PAGE_COUNT = 2;
    public PortfolioFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0: return BillingFragment.newInstance(0);
            case 1: return PifFragment.newInstance(1);
            default: return  BillingFragment.newInstance(0);
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
