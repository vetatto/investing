package ru.vetatto.investing.investing.Login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ru.vetatto.investing.investing.PageFragment;

public class LoginFragmentPagerAdapter extends FragmentStatePagerAdapter {
    static final int PAGE_COUNT = 2;
    public LoginFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        return LoginPageFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
