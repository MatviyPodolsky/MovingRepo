package com.sdex.webteb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.TutorialPageAdapter;
import com.sdex.webteb.dialogs.TermsOfServiceDialog;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by MPODOLSKY on 02.02.2015.
 */
public class WelcomeActivity extends BaseActivity implements PageIndicator{

    private TutorialPageAdapter mAdapter;
    @InjectView(R.id.pager) ViewPager mPager;
    @InjectView(R.id.indicator) CirclePageIndicator mIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new TutorialPageAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mPager);
        mPager.setOnPageChangeListener(this);
        mPager.setOffscreenPageLimit(3);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_welcom;
    }

    @OnClick(R.id.login_facebook)
    public void loginWithFB() {
        Intent intent = new Intent(WelcomeActivity.this, SetupProfileActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.login)
    public void login(final View v){
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.register)
    public void register(final View v){
        Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.terms_of_usage)
    public void showTOC(final View v){
        DialogFragment newFragment = TermsOfServiceDialog.newInstance();
        newFragment.show(getSupportFragmentManager(), null);
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
}
