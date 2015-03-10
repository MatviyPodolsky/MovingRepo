package com.sdex.webteb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.ProfilePageAdapter;
import com.sdex.webteb.database.DatabaseHelper;
import com.sdex.webteb.database.model.DbUser;
import com.sdex.webteb.model.Child;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.request.BabyProfileRequest;
import com.sdex.webteb.rest.response.BabyProfileResponse;
import com.sdex.webteb.rest.response.UserInfoResponse;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.InjectView;
import retrofit.client.Response;

/**
 * Created by MPODOLSKY on 02.02.2015.
 */
public class SetupProfileActivity extends BaseActivity implements PageIndicator {

    public static final String FAMILY_RELATION = "FAMILY_RELATION";
    public static final String DATE_TYPE = "DATE_TYPE";
    public static final String DATE = "DATE";
    public static final String CHILDREN = "CHILDREN";

    private ProfilePageAdapter mAdapter;
    @InjectView(R.id.pager)
    ViewPager mPager;
    @InjectView(R.id.indicator)
    CirclePageIndicator mIndicator;
    @InjectView(R.id.profile_card)
    View profileCard;
    private String mEmail;
    private BabyProfileRequest request = new BabyProfileRequest();
    private RestCallback<BabyProfileResponse> getBabyProfileCallback;

    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = DatabaseHelper.getInstance(this);

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String dt = outFormat.format(date);
        request.setDateType(1);
        request.setDate(dt);
        profileCard.findViewById(R.id.photo_container).setVisibility(View.GONE);

        getBabyProfileCallback = new RestCallback<BabyProfileResponse>() {
            @Override
            public void failure(RestError restError) {
                mAdapter = new ProfilePageAdapter(getSupportFragmentManager(), null);
                mPager.setAdapter(mAdapter);
                mIndicator.setViewPager(mPager);
                mPager.setOnPageChangeListener(SetupProfileActivity.this);
                mPager.setOffscreenPageLimit(3);
            }

            @Override
            public void success(BabyProfileResponse babyProfileResponse, Response response) {
                mAdapter = new ProfilePageAdapter(getSupportFragmentManager(), babyProfileResponse);
                mPager.setAdapter(mAdapter);
                mIndicator.setViewPager(mPager);
                mPager.setOnPageChangeListener(SetupProfileActivity.this);
                mPager.setOffscreenPageLimit(3);
            }
        };
        RestClient.getApiService().getBabyProfile(getBabyProfileCallback);

        RestClient.getApiService().getUserInfo(new RestCallback<UserInfoResponse>() {
            @Override
            public void failure(RestError restError) {
            }

            @Override
            public void success(UserInfoResponse userInfoResponse, Response response) {
                ((TextView)profileCard.findViewById(R.id.username)).setText(userInfoResponse.getEmail());
                mEmail = userInfoResponse.getEmail();
            }
        });
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

    public void setFamilyRelation(int relation){
        request.setFamilyRelation(relation);
    }

    public void setBirthDate(String date, int dateType){
        request.setDate(date);
        request.setDateType(dateType);
    }

    public void setChildren(List<Child> children){
        request.setChildren(children);
    }

    public void sendRequest(){
        RestClient.getApiService().setBabyProfile(request, new RestCallback<String>() {
            @Override
            public void failure(RestError restError) {
                Toast.makeText(SetupProfileActivity.this, "Error, check if u correct input data", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void success(String s, Response response) {
                DbUser user = databaseHelper.getUser(mEmail);
                if (user == null) {
                    DbUser newUser = new DbUser();
                    newUser.setEmail(mEmail);
                    newUser.setCompletedProfile(true);
                    String children = "";
                    for (Child child : request.getChildren()){
                        if(children.equals("")) {
                            children = children + child.getName();
                        } else {
                            children = children + "/" + child.getName();
                        }
                    }
                    newUser.setChildren(children);
                    databaseHelper.addUser(newUser);
                } else {
                    user.setCompletedProfile(true);
                    String children = "";
                    for (Child child : request.getChildren()){
                        if(children.equals("")) {
                            children = children + child.getName();
                        } else {
                            children = children + "/" + child.getName();
                        }
                    }
                    user.setChildren(children);
                    databaseHelper.updateUser(user);
                }
                Intent intent = new Intent(SetupProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
