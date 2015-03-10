package com.sdex.webteb.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.ProfilePageAdapter;
import com.sdex.webteb.database.DatabaseHelper;
import com.sdex.webteb.database.model.DbUser;
import com.sdex.webteb.dialogs.PhotoDialog;
import com.sdex.webteb.fragments.PhotoFragment;
import com.sdex.webteb.model.Child;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.request.BabyProfileRequest;
import com.sdex.webteb.rest.response.BabyProfileResponse;
import com.sdex.webteb.rest.response.UserInfoResponse;
import com.sdex.webteb.utils.PreferencesManager;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
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
    @InjectView(R.id.avatar)
    ImageView mProfilePhoto;
    private String username;
    private DatabaseHelper databaseHelper;

    private BabyProfileRequest request = new BabyProfileRequest();
    private RestCallback<BabyProfileResponse> getBabyProfileCallback;

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

        username = PreferencesManager.getInstance().getUsername();
        DbUser user = databaseHelper.getUser(username);
        final String photoPath = user.getPhotoPath();
        if (photoPath != null) {
            Picasso.with(this)
                    .load(PhotoFragment.FILE_PREFIX + photoPath)
                    .placeholder(R.drawable.ic_photo)
                    .fit()
                    .centerCrop()
                    .into(mProfilePhoto);
        }

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
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_setup_profile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            final String username = PreferencesManager.getInstance().getUsername();
            DbUser user = databaseHelper.getUser(username);
            switch (requestCode) {
                case PhotoFragment.PHOTO_TAKEN_PROFILE:
                    File albumDir = PhotoFragment.getAlbumDir();
                    File profileImage = new File(albumDir.getAbsolutePath() + "/profile"
                            + PhotoFragment.JPEG_FILE_SUFFIX);
                    user.setPhotoPath(profileImage.getAbsolutePath());
                    databaseHelper.updateUser(user);
                    Picasso.with(this)
                            .load(PhotoFragment.FILE_PREFIX + profileImage)
                            .placeholder(R.drawable.ic_photo)
                            .fit()
                            .centerCrop()
                            .into(mProfilePhoto);
                    break;
                case PhotoFragment.PHOTO_SELECTED_PROFILE:
                    Uri galleryPhotoUri = PhotoFragment.getGalleryPhotoUri(this, data.getData());
                    user.setPhotoPath(galleryPhotoUri.getPath());
                    databaseHelper.updateUser(user);
                    Picasso.with(this)
                            .load(PhotoFragment.FILE_PREFIX + galleryPhotoUri)
                            .placeholder(R.drawable.ic_photo)
                            .fit()
                            .centerCrop()
                            .into(mProfilePhoto);
                    break;
            }
        }
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
                DbUser user = databaseHelper.getUser(username);
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
                Intent intent = new Intent(SetupProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @OnClick(R.id.avatar)
    public void takeProfilePhoto() {
        DialogFragment dialog = PhotoDialog.newInstance(PhotoFragment.PHOTO_TAKEN_PROFILE,
                PhotoFragment.PHOTO_SELECTED_PROFILE);
        dialog.show(getSupportFragmentManager(), null);
    }

}
