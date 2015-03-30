package com.sdex.webteb.utils;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.sdex.webteb.activities.MainActivity;
import com.sdex.webteb.model.ContentLink;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MPODOLSKY on 17.03.2015.
 */
public class FacebookUtil {

    public static final int PUBLISH_ARTICLE = 0;
    public static final int PUBLISH_PHOTO = 1;

    public static final String PERMISSION = "publish_actions";
    public static boolean isPostingPhoto;
    private static ContentLink lastArticle;
    private static String lastPhoto;
    private static int lastContent;

    public static void publishArticle(Activity activity, ContentLink article) {
        lastArticle = article;
        lastContent = PUBLISH_ARTICLE;
        if (isFacebookInstalled(activity) && FacebookDialog.canPresentShareDialog(activity.getApplicationContext(),
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            publishFeedFromApp(activity, null, article.getTitle(), article.getDescription(),
                    article.getUrl(), article.getImageUrl());
        } else {
            publishFeedFromWeb(activity, null, article.getTitle(), article.getDescription(),
                    article.getUrl(), article.getImageUrl());
        }
    }

    public static void publishPhoto(Activity activity, String path) {
        lastContent = PUBLISH_PHOTO;
        if (isFacebookInstalled(activity)
                && FacebookDialog.canPresentShareDialog(activity.getApplicationContext(),
                FacebookDialog.ShareDialogFeature.PHOTOS)) {
            publishPhotoFromApp(activity, path);
        } else {
            publishPhotoFromWeb(activity, path);
        }
    }

    public static void publishLastContent(Activity activity) {
        if (lastContent == PUBLISH_ARTICLE) {
            if (lastArticle != null) {
                publishArticle(activity, lastArticle);
            }
        }
        if (lastContent == PUBLISH_PHOTO) {
            if (lastPhoto != null && !lastPhoto.isEmpty()) {
                publishPhoto(activity, lastPhoto);
            }
        }
    }

    private static void publishFeedFromApp(Activity activity, String appName, String caption, String description, String link,
                                           String picture) {

        FacebookDialog.ShareDialogBuilder builder = new FacebookDialog.ShareDialogBuilder(activity);
        if (appName != null && !appName.isEmpty()) {
            builder.setName(appName);
        }
        if (caption != null && !caption.isEmpty()) {
            builder.setCaption(caption);
        }
        if (description != null && !description.isEmpty()) {
            builder.setDescription(description);
        }
        if (link != null && !link.isEmpty()) {
            builder.setLink(link);
        }
        if (picture != null && !picture.isEmpty()) {
            builder.setPicture(picture);
        }

        FacebookDialog shareDialog = builder.build();
        ((MainActivity) activity).getUiHelper().trackPendingDialogCall(shareDialog.present());
    }

    private static void publishFeedFromWeb(Activity activity, String appName, String caption, String description, String link,
                                           String picture) {
        Session currentSession = Session.getActiveSession();
        if (currentSession == null || currentSession.getState().isClosed()) {
            Session session = new Session.Builder(activity).build();
            Session.setActiveSession(session);
            currentSession = session;
        }

        if (currentSession.isOpened()) {
//            if (hasPublishPermission()) {
                publishFeedDialog(activity, appName, caption, description, link, picture);
//            } else {
//                currentSession.requestNewPublishPermissions(new Session.NewPermissionsRequest(activity, PERMISSION));
//            }
        } else if (!currentSession.isOpened()) {
            // Ask for username and password
            Session.OpenRequest op = new Session.OpenRequest(activity);

            op.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
            op.setCallback(null);

            List<String> permissions = new ArrayList<String>();
            permissions.add("publish_stream");
            op.setPermissions(permissions);

            Session session = new Session.Builder(activity).build();
            Session.setActiveSession(session);
            session.openForPublish(op);
        }
    }

    private static void publishPhotoFromApp(Activity activity, String path) {

//        if (FacebookDialog.canPresentShareDialog (activity.getApplicationContext(),
//                FacebookDialog.ShareDialogFeature.PHOTOS)) {
        FacebookDialog.PhotoShareDialogBuilder builder = new FacebookDialog.PhotoShareDialogBuilder(activity);
        File photoFile = new File(path);
        List<File> photos = new ArrayList<>();
        photos.add(photoFile);
        builder.addPhotoFiles(photos);
        FacebookDialog photoDialog = builder.build();
        ((MainActivity) activity).getUiHelper().trackPendingDialogCall(photoDialog.present());
//        }
    }

    private static void publishFeedDialog(final Activity activity, String appName, String caption, String description, String link, String picture) {
        Bundle params = new Bundle();
        if (appName != null && !appName.isEmpty()) {
            params.putString("name", appName);
        }
        if (caption != null && !caption.isEmpty()) {
            params.putString("caption", caption);
        }
        if (description != null && !description.isEmpty()) {
            params.putString("description", description);
        }
        if (link != null && !link.isEmpty()) {
            params.putString("link", link);
        }
        if (picture != null && !picture.isEmpty()) {
            params.putString("picture", picture);
        }

        WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(activity, Session.getActiveSession(), params))
                .setOnCompleteListener(new WebDialog.OnCompleteListener() {

                    @Override
                    public void onComplete(Bundle values, FacebookException error) {
                        if (error == null) {
                            // When the story is posted, echo the success
                            // and the post Id.
                            final String postId = values.getString("post_id");
                            if (postId != null) {
                                Toast.makeText(activity, "Posted story, id: " + postId,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // User clicked the Cancel button
                                Toast.makeText(activity.getApplicationContext(), "Publish cancelled",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else if (error instanceof FacebookOperationCanceledException) {
                            // User clicked the "x" button
                            Toast.makeText(activity.getApplicationContext(), "Publish cancelled",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Generic, ex: network error
                            Toast.makeText(activity.getApplicationContext(), "Error posting story",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .build();
        feedDialog.show();
    }

    private static void publishPhotoDialog(final Activity activity, String path) {
        if (!isPostingPhoto) {
            lastPhoto = path;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap image = BitmapFactory.decodeFile(path, options);

            Request request = Request.newUploadPhotoRequest(Session.getActiveSession(), image, new Request.Callback() {
                @Override
                public void onCompleted(Response response) {
                    if (response.getError() == null) {
                        Toast.makeText(activity, "Photo posted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "Error posting photo", Toast.LENGTH_SHORT).show();
                    }
                    isPostingPhoto = false;
                }
            });
            request.executeAsync();
            Toast.makeText(activity, "Posting...", Toast.LENGTH_SHORT).show();
            isPostingPhoto = true;
        } else {
            Toast.makeText(activity, "Please, wait while previous photo will post on Facebook", Toast.LENGTH_SHORT).show();
        }
    }

    private static void publishPhotoFromWeb(Activity activity, String path) {
        Session currentSession = Session.getActiveSession();
        if (currentSession == null || currentSession.getState().isClosed()) {
            Session session = new Session.Builder(activity).build();
            Session.setActiveSession(session);
            currentSession = session;
        }

        if (currentSession.isOpened()) {
            if (hasPublishPermission()) {
                publishPhotoDialog(activity, path);
            } else {
                currentSession.requestNewPublishPermissions(new Session.NewPermissionsRequest(activity, PERMISSION));
            }
        } else if (!currentSession.isOpened()) {
            // Ask for username and password
            Session.OpenRequest op = new Session.OpenRequest(activity);

            op.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
            op.setCallback(null);

            List<String> permissions = new ArrayList<String>();
            permissions.add("publish_action");
            permissions.add("email");
            op.setPermissions(permissions);

            Session session = new Session.Builder(activity).build();
            Session.setActiveSession(session);
            session.openForPublish(op);
        }
    }

    public static boolean isFacebookInstalled(Activity activity) {
        try {
            ApplicationInfo info = activity.getPackageManager().
                    getApplicationInfo("com.facebook.katana", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private static boolean hasPublishPermission() {
        Session session = Session.getActiveSession();
        return session != null && session.getPermissions().contains("publish_actions");
    }

    private static String getLastPhoto() {
        return lastPhoto;
    }

    private static ContentLink getLastArticle() {
        return lastArticle;
    }
}
