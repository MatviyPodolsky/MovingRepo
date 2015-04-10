package com.sdex.webteb.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.sdex.webteb.R;
import com.sdex.webteb.model.ContentLink;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Author: Yuriy Mysochenko
 * Date: 10.04.2015
 */
public abstract class FacebookShareFragment extends PhotoFragment {

    private static final String PERMISSION = "publish_actions";

    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

    private boolean canPresentShareDialog;
    private boolean canPresentShareDialogWithPhotos;
    private PendingAction pendingAction = PendingAction.NONE;

    private final String PENDING_ACTION_BUNDLE_KEY = "PendingAction";

    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            hideProgress();
            Log.d("Facebook", "Canceled");
        }

        @Override
        public void onError(FacebookException error) {
            hideProgress();
            Log.d("Facebook", String.format("Error: %s", error.toString()));
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            hideProgress();
            Log.d("Facebook", "Success!");
        }

    };
    private String photoPath;
    private ContentLink article;

    private ProgressDialog progressDialog;

    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_LINK
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handlePendingAction();
                    }

                    @Override
                    public void onCancel() {
                        if (pendingAction != PendingAction.NONE) {
                            pendingAction = PendingAction.NONE;
                        }
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        if (pendingAction != PendingAction.NONE
                                && exception instanceof FacebookAuthorizationException) {
                            pendingAction = PendingAction.NONE;
                        }
                    }

                });

        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(
                callbackManager,
                shareCallback);

        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }

        // Can we present the share dialog for regular links?
        canPresentShareDialog = ShareDialog.canShow(ShareLinkContent.class);
        // Can we present the share dialog for photos?
        canPresentShareDialogWithPhotos = ShareDialog.canShow(SharePhotoContent.class);
    }

    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but we assume they
        // will succeed.
        pendingAction = PendingAction.NONE;

        switch (previouslyPendingAction) {
            case NONE:
                break;
            case POST_PHOTO:
                postPhoto();
                break;
            case POST_LINK:
                postLink();
                break;
        }
    }

    private void showProgress() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.loading), true, false);
        }
    }

    private void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void postLink() {
        Profile profile = Profile.getCurrentProfile();
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle(article.getTitle())
                .setContentDescription(article.getDescription())
                .setContentUrl(Uri.parse(article.getUrl()))
                .build();
        if (canPresentShareDialog) {
            shareDialog.show(linkContent);
        } else if (profile != null && hasPublishPermission()) {
            ShareApi.share(linkContent, shareCallback);
            showProgress();
        } else {
            pendingAction = PendingAction.POST_LINK;
        }
    }

    private void postPhoto() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap image = BitmapFactory.decodeFile(photoPath, options);
        SharePhoto sharePhoto = new SharePhoto.Builder().setBitmap(image).build();
        ArrayList<SharePhoto> photos = new ArrayList<>();
        photos.add(sharePhoto);

        SharePhotoContent sharePhotoContent =
                new SharePhotoContent.Builder().setPhotos(photos).build();
        if (canPresentShareDialogWithPhotos) {
            shareDialog.show(sharePhotoContent);
        } else if (hasPublishPermission()) {
            ShareApi.share(sharePhotoContent, shareCallback);
            showProgress();
        } else {
            pendingAction = PendingAction.POST_PHOTO;
        }
    }

    private boolean hasPublishPermission() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && accessToken.getPermissions().contains("publish_actions");
    }

    protected void performPublishPhoto(String photoPath) {
        this.photoPath = photoPath;
        performPublish(PendingAction.POST_PHOTO, canPresentShareDialogWithPhotos);
    }

    protected void performPublishLink(ContentLink article) {
        this.article = article;
        performPublish(PendingAction.POST_LINK, canPresentShareDialog);
    }

    private void performPublish(PendingAction action, boolean allowNoToken) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            pendingAction = action;
            if (hasPublishPermission()) {
                // We can do the action right away.
                handlePendingAction();
                return;
            } else {
                logInWithPublishPermissions();

                return;
            }
        }

        if (allowNoToken) {
            pendingAction = action;
            handlePendingAction();
        } else {
            logInWithPublishPermissions();
        }
    }

    private void logInWithPublishPermissions() {
        // We need to get new permissions, then complete the action when we get called back.
        LoginManager.getInstance().logInWithPublishPermissions(
                this,
                Arrays.asList(PERMISSION));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
