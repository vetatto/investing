package ru.vetatto.investing.investing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MainFragment extends Fragment{
    View view;
    public String api_token;
    String all_sum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.second_first, container, false);
        view = inflater.inflate(R.layout.title_view, container, false);
        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        PortfolioFragmentPagerAdapter pagerAdapter = new PortfolioFragmentPagerAdapter(getFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(listener);


        return view;
    }
    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        @Override
        public void onPageSelected(int position) {

            if (position == 0) {
                ((MainActivity) getActivity()).showFloatingActionButton();
                //BillingFragment.SetActionBar("test");
            }
            if (position == 1) {
                ((MainActivity) getActivity()).hideFloatingActionButton();
                //PifFragment.SetActionBar("test");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}