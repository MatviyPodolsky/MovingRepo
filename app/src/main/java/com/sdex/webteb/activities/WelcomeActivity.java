package com.sdex.webteb.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.RequestBatch;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.sdex.webteb.R;
import com.sdex.webteb.adapters.TutorialPageAdapter;
import com.sdex.webteb.dialogs.TermsOfServiceDialog;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import java.util.Arrays;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by MPODOLSKY on 02.02.2015.
 */
public class WelcomeActivity extends BaseActivity implements PageIndicator {

    private static final String TAG = "WelcomActivity";

    private TutorialPageAdapter mAdapter;
    private UiLifecycleHelper uiHelper;
    @InjectView(R.id.pager)
    ViewPager mPager;
    @InjectView(R.id.indicator)
    CirclePageIndicator mIndicator;
    @InjectView(R.id.authButton)
    LoginButton loginButton;
    @InjectView(R.id.info)
    TextView userInfoTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        mAdapter = new TutorialPageAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mPager);
        mPager.setOnPageChangeListener(this);
        mPager.setOffscreenPageLimit(3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_welcome;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
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

    @OnClick(R.id.terms_of_usage)
    public void showTOC(final View v) {
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

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
            RequestBatch requestBatch = new RequestBatch();
            requestBatch.add(new Request(Session.getActiveSession(),
                    "me", null, null, new Request.Callback() {
                public void onCompleted(Response response) {
                    GraphObject graphObject = response.getGraphObject();
                    String s = "";
                    if (graphObject != null) {
                        if (graphObject.getProperty("id") != null) {
                            s = String.format("%s: %s",
                                    graphObject.getProperty("id"),
                                    graphObject.getProperty("name"));
                        }
                    }
                    userInfoTextView.setText(s);
                }
            }));
            requestBatch.executeAsync();
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
            userInfoTextView.setText("");
        }
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private void openFacebookSession(){

        Session.openActiveSession(this, true,
                Arrays.asList("user_likes", "user_status", "user_publish"),
                new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                if (exception != null) {
                    Log.d("Facebook", exception.getMessage());
                }
                Log.d("Facebook", "Session State: " + session.getState());
                // you can make request to the /me API or do other stuff like post, etc. here
            }
        });
    }

    private static Session openActiveSession(Activity activity, boolean allowLoginUI, List permissions, Session.StatusCallback callback) {
        Session.OpenRequest openRequest = new Session.OpenRequest(activity).setPermissions(permissions).setCallback(callback);
        Session session = new Session.Builder(activity).build();
        if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) || allowLoginUI) {
            Session.setActiveSession(session);
            session.openForRead(openRequest);
            return session;
        }
        return null;
    }

    private String buildUserInfoDisplay(GraphUser user) {
        StringBuilder userInfo = new StringBuilder("");

        // Example: typed access (name)
        // - no special permissions required
        userInfo.append(String.format("Name: %s\n\n",
                user.getName()));

        // Example: typed access (birthday)
        // - requires user_birthday permission
        userInfo.append(String.format("Birthday: %s\n\n",
                user.getBirthday()));

        // Example: partially typed access, to location field,
        // name key (location)
        // - requires user_location permission
//        userInfo.append(String.format("Location: %s\n\n",
//                user.getLocation().getProperty("name")));

        // Example: access via property name (locale)
        // - no special permissions required
        userInfo.append(String.format("Locale: %s\n\n",
                user.getProperty("locale")));

        return userInfo.toString();
    }
}
