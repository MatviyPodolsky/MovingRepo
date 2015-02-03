package com.sdex.webteb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.ProfilePageAdapter;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import butterknife.InjectView;

/**
 * Created by MPODOLSKY on 02.02.2015.
 */
public class SetupProfileActivity extends BaseActivity implements PageIndicator {

    private ProfilePageAdapter mAdapter;
    @InjectView(R.id.pager)
    ViewPager mPager;
    @InjectView(R.id.indicator)
    CirclePageIndicator mIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ProfilePageAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mPager);
        mPager.setOnPageChangeListener(this);
        mPager.setOffscreenPageLimit(3);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_setup_profile;
    }

    @Override
    public void setViewPager(ViewPager viewPager) {

    }

    @Override
    public void setViewPager(ViewPager viewPager, int i) {

    }

    @Override
    public void setCurrentItem(int i) {

    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {

    }

    @Override
    public void notifyDataSetChanged() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mIndicator.setCurrentItem(position);
        mIndicator.notifyDataSetChanged();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void scrollToNextPage(){
        if(mPager.getCurrentItem()<mPager.getChildCount()-1){
            mPager.setCurrentItem(mPager.getCurrentItem()+1, true);
        }
    }

    public void launchMainActivity() {
        Intent intent = new Intent(SetupProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
