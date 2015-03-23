package com.sdex.webteb.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.widget.LoginButton;
import com.sdex.webteb.R;
import com.sdex.webteb.adapters.TutorialPageAdapter;
import com.sdex.webteb.dialogs.TermsOfServiceDialog;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.utils.PreferencesManager;
import com.viewpagerindicator.IconPageIndicator;
import com.viewpagerindicator.PageIndicator;

import java.util.Arrays;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by MPODOLSKY on 02.02.2015.
 */
public class WelcomeActivity extends FacebookAuthActivity implements PageIndicator {

    private TutorialPageAdapter mAdapter;
    @InjectView(R.id.pager)
    ViewPager mPager;
    @InjectView(R.id.indicator)
    IconPageIndicator mIndicator;
    @InjectView(R.id.auth_button)
    LoginButton loginButton;
    @InjectView(R.id.info)
    TextView userInfoTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new TutorialPageAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mPager);
        mPager.setOnPageChangeListener(this);
        mPager.setOffscreenPageLimit(3);
        loginButton.setReadPermissions(Arrays.asList("email"));

        showServerChooseDialog();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_welcome;
    }

    @OnClick(R.id.login)
    public void login(final View v) {
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.register)
    public void register(final View v) {
        Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.link1)
    public void showTOC(final View v) {
        DialogFragment newFragment = TermsOfServiceDialog.newInstance();
        newFragment.show(getSupportFragmentManager(), null);
    }

    @OnClick(R.id.link2)
    public void showSA(final View v) {
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

    private void showServerChooseDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                WelcomeActivity.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Select server address");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                WelcomeActivity.this,
                android.R.layout.select_dialog_item);
        arrayAdapter.add(RestClient.SERVER_URL);
        arrayAdapter.add("http://api.jwebteb.com");
        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String server = arrayAdapter.getItem(which);
                        SharedPreferences preferences = PreferencesManager.getInstance().getPreferences();
                        preferences.edit().putString("server", server).commit();
                        RestClient.ENDPOINT.setUrl(server);
                    }
                });
        AlertDialog alertDialog = builderSingle.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

}
