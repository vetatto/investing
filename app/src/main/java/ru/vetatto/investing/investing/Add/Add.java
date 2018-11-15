package ru.vetatto.investing.investing.Add;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ru.vetatto.investing.investing.R;

public class Add extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ViewPager pager = (ViewPager) findViewById(R.id.addViewPager);
        AddFragmentPagerAdapter pagerAdapter = new AddFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
    }
}
