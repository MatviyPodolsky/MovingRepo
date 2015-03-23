package com.sdex.webteb.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.RequestBatch;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.widget.LoginButton;
import com.sdex.webteb.R;
import com.sdex.webteb.adapters.TutorialPageAdapter;
import com.sdex.webteb.database.DatabaseHelper;
import com.sdex.webteb.database.model.DbUser;
import com.sdex.webteb.dialogs.TermsOfServiceDialog;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.request.FacebookLoginRequest;
import com.sdex.webteb.rest.response.UserLoginResponse;
import com.sdex.webteb.utils.PreferencesManager;
import com.viewpagerindicator.IconPageIndicator;
import com.viewpagerindicator.PageIndicator;

import java.util.Arrays;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by MPODOLSKY on 02.02.2015.
 */
public class WelcomeActivity extends BaseActivity implements PageIndicator {

    private static final String TAG = "WelcomeActivity";

    private TutorialPageAdapter mAdapter;
    private UiLifecycleHelper uiHelper;
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

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        mAdapter = new TutorialPageAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mPager);
        mPager.setOnPageChangeListener(this);
        mPager.setOffscreenPageLimit(3);
        loginButton.setPublishPermissions(Arrays.asList("publish_actions", "email"));
//        loginButton.setReadPermissions(Arrays.asList("email"));

        showServerChooseDialog();
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
//            requestBatch.executeAsync();
            FacebookLoginRequest request = new FacebookLoginRequest();
            request.setToken(session.getAccessToken());

            RestClient.getApiService().facebookLogin(request, new RestCallback<UserLoginResponse>() {
                @Override
                public void failure(RestError restError) {
                }

                @Override
                public void success(UserLoginResponse s, retrofit.client.Response response) {
                    //TODO
                    final PreferencesManager preferencesManager = PreferencesManager.getInstance();
                    preferencesManager.setTokenData(s.getAccessToken(), s.getTokenType());
                    preferencesManager.setEmail(s.getUserName());
                    DatabaseHelper databaseHelper = DatabaseHelper.getInstance(WelcomeActivity.this);
                    DbUser user = databaseHelper.getUser(s.getUserName());
                    if (user == null) {
                        DbUser newUser = new DbUser();
                        newUser.setEmail(s.getUserName());
                        databaseHelper.addUser(newUser);
                        launchMainActivity(false);
                    } else {
                        if (user.isCompletedProfile()){
                            launchMainActivity(true);
                        } else {
                            launchMainActivity(false);
                        }
                    }
                }
            });
//            session.getAccessToken();
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

    private void launchMainActivity(boolean completedProfile) {
        Intent intent;
        if(completedProfile) {
            MainActivity.launch(WelcomeActivity.this);
        } else {
            intent = new Intent(WelcomeActivity.this, SetupProfileActivity.class);
            startActivity(intent);
        }
        finish();
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
