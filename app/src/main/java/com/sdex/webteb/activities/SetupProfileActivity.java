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
import com.sdex.webteb.dialogs.ConfirmDialog;
import com.sdex.webteb.dialogs.DialogCallback;
import com.sdex.webteb.dialogs.PhotoDialog;
import com.sdex.webteb.fragments.PhotoFragment;
import com.sdex.webteb.fragments.main.SettingsFragment;
import com.sdex.webteb.model.Child;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.request.BabyProfileRequest;
import com.sdex.webteb.rest.response.BabyProfileResponse;
import com.sdex.webteb.utils.DateUtil;
import com.sdex.webteb.utils.PreferencesManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.client.Response;

/**
 * Created by MPODOLSKY on 02.02.2015.
 */
public class SetupProfileActivity extends BaseActivity {

    public static final String FAMILY_RELATION = "FAMILY_RELATION";
    public static final String DATE_TYPE = "DATE_TYPE";
    public static final String DATE = "DATE";
    public static final String CHILDREN = "CHILDREN";

    private ProfilePageAdapter mAdapter;
    @InjectView(R.id.pager)
    ViewPager mPager;
    @InjectView(R.id.profile_card)
    View profileCard;
    @InjectView(R.id.avatar)
    ImageView mProfilePhoto;
    private String username;
    private DatabaseHelper databaseHelper;
    private boolean isInEditMode;

    private BabyProfileRequest request = new BabyProfileRequest();
    private RestCallback<BabyProfileResponse> getBabyProfileCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isInEditMode = getIntent().getBooleanExtra(SettingsFragment.EDIT_PROFILE, false);

        databaseHelper = DatabaseHelper.getInstance(this);
        profileCard.findViewById(R.id.photo_container).setVisibility(View.GONE);

        username = PreferencesManager.getInstance().getEmail();
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
                initViewPager();
            }

            @Override
            public void success(BabyProfileResponse babyProfileResponse, Response response) {
                mAdapter = new ProfilePageAdapter(getSupportFragmentManager(), babyProfileResponse);
                initViewPager();
            }
        };

        if (isInEditMode) {
            RestClient.getApiService().getBabyProfile(getBabyProfileCallback);
        } else {
            mAdapter = new ProfilePageAdapter(getSupportFragmentManager(), null);
            initViewPager();
        }


        PreferencesManager preferencesManager = PreferencesManager.getInstance();
        String userName = preferencesManager.getUsername();
        ((TextView) profileCard.findViewById(R.id.username)).setText(userName);
        if (preferencesManager.getCurrentWeek() != null) {
            String currentWeek = preferencesManager.getCurrentWeek();
            ((TextView) profileCard.findViewById(R.id.textView5)).setText(currentWeek);
        }
    }

    private void initViewPager() {
        mPager.setAdapter(mAdapter);
        mPager.setOffscreenPageLimit(3);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_setup_profile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            final String username = PreferencesManager.getInstance().getEmail();
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

    public void scrollToNextPage() {
        if (mPager.getCurrentItem() < mPager.getChildCount() - 1) {
            mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
        }
    }

    public void launchMainActivity() {
        if (!isInEditMode) {
            Intent intent = new Intent(SetupProfileActivity.this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }

    public void saveChanges() {
        if (isInEditMode) {
            ConfirmDialog dialog = ConfirmDialog.newInstance(R.string.dialog_edit_profile_title,
                    R.string.dialog_edit_profile_message, R.string.dialog_edit_profile_confirm,
                    R.string.dialog_edit_profile_cancel);
            dialog.setCallback(new DialogCallback() {
                @Override
                public void confirm() {
                    save();
                }

                @Override
                public void cancel() {
                    finish();
                }
            });
            dialog.show(getSupportFragmentManager(), "dialog");
        } else {
            save();
        }
    }

    private void save() {
        sendRequest();
    }

    public void setFamilyRelation(int relation) {
        request.setFamilyRelation(relation);
    }

    public void setBirthDate(String date) {
        request.setDate(date);
    }

    public void setDateType(int dateType) {
        request.setDateType(dateType);
    }

    public void setChildren(List<Child> children) {
        request.setChildren(children);
    }

    public void sendRequest() {
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
                for (Child child : request.getChildren()) {
                    if (children.isEmpty()) {
                        children = children + child.getName();
                    } else {
                        children = children + "/" + child.getName();
                    }
                }
                user.setChildren(children);
                databaseHelper.updateUser(user);
                MainActivity.launch(SetupProfileActivity.this);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        int currentItem = mPager.getCurrentItem();
        if (currentItem > 0) {
            mPager.setCurrentItem(--currentItem, true);
        } else {
            if (isInEditMode) {
                super.onBackPressed();
            }
        }
    }

    @OnClick(R.id.avatar)
    public void takeProfilePhoto() {
        DialogFragment dialog = PhotoDialog.newInstance(PhotoFragment.PHOTO_TAKEN_PROFILE,
                PhotoFragment.PHOTO_SELECTED_PROFILE);
        dialog.show(getSupportFragmentManager(), null);
    }

    public boolean isValidData(){
        if (request.getFamilyRelation() == -1) {
            Toast.makeText(this, getString(R.string.please_select_relation), Toast.LENGTH_SHORT).show();
            return false;
        }

        //check date
        int dateType = request.getDateType();
        String dateStr = request.getDate();
        if(dateStr != null && dateStr.isEmpty()){
            Toast.makeText(this, getString(R.string.please_select_date), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(dateType == BabyProfileResponse.DATE_TYPE_NOT_SET){
            Toast.makeText(this, getString(R.string.please_select_date), Toast.LENGTH_SHORT).show();
            return false;
        }
        Date currentDate = Calendar.getInstance().getTime();
        Date requestDate = DateUtil.parseDate(dateStr);
        if(dateType == BabyProfileResponse.DATE_TYPE_BIRTH_DATE
                && requestDate.after(currentDate)){
            Toast.makeText(this, getString(R.string.birth_date_cant_be_in_future), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(dateType == BabyProfileResponse.DATE_TYPE_LAST_PERIOD
                && requestDate.after(currentDate)){
            Toast.makeText(this, getString(R.string.last_period_cant_be_in_future), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(dateType == BabyProfileResponse.DATE_TYPE_DUE_TO
                && requestDate.before(currentDate)){
            Toast.makeText(this, getString(R.string.expected_birth_date_cant_be_in_past), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
