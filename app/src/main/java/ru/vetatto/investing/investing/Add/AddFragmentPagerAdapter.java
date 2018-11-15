package ru.vetatto.investing.investing.Add;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class AddFragmentPagerAdapter extends FragmentStatePagerAdapter {
    static final int PAGE_COUNT = 1;
    public AddFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }
    @Override
    public Fragment getItem(int position) {
        return AddPageFragment.newInstance(position);
    }
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
