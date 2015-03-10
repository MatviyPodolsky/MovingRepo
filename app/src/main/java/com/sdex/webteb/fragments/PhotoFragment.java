package com.sdex.webteb.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.sdex.webteb.database.DatabaseHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * Created by Yuriy Mysochenko on 03.03.2015.
 */
public abstract class PhotoFragment extends BaseFragment {

    public static final int PHOTO_TAKEN_ALBUM = 6001;
    public static final int PHOTO_TAKEN_PROFILE = 6002;
    public static final int PHOTO_SELECTED_ALBUM = 7001;
    public static final int PHOTO_SELECTED_PROFILE = 7002;

    public static final String PHOTO_PATH = "PHOTO_PATH";

    private static final String CAMERA_DIR = "/dcim/";

    public static final String JPEG_FILE_PREFIX = "IMG_";
    public static final String JPEG_FILE_SUFFIX = ".jpg";

    public static final String FILE_PREFIX = "file:///";

    private static final String ALBUM_NAME = "WebTeb";

    protected EventBus BUS = EventBus.getDefault();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        BUS.registerSticky(this);
    }

    @Override
    public void onStop() {
        BUS.unregister(this);
        super.onStop();
    }


    public static Uri getGalleryPhotoUri(Activity activity, Uri selectedImage) {
        Cursor cursor = null;
        String filePath = null;
        try {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            cursor = activity.getContentResolver().query(
                    selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return Uri.parse(filePath);
    }

    private static File getAlbumStorageDir() {
        return new File(
                Environment.getExternalStorageDirectory()
                        + CAMERA_DIR
                        + ALBUM_NAME
        );
    }

    public static File getAlbumDir() {
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = getAlbumStorageDir();
            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d("CameraHelper", "failed to create directory");
                        return null;
                    }
                }
            }
        } else {
            Log.v("CameraHelper", "External storage is not mounted READ/WRITE.");
        }
        return storageDir;
    }

    public void addPictureToGallery(String photoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(photoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private static File createAlbumImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private static File createProfileImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "/profile";
        File albumF = getAlbumDir();
        //File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        File imageF = new File(albumF.getAbsolutePath() + imageFileName + JPEG_FILE_SUFFIX);
        imageF.getParentFile().mkdirs();
        imageF.createNewFile();
        return imageF;
    }

    public static void dispatchTakePictureIntent(Activity activity, int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                if (requestCode == PHOTO_TAKEN_ALBUM) {
                    photoFile = createAlbumImageFile();
                } else {
                    photoFile = createProfileImageFile();
                }
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri uri = Uri.fromFile(photoFile);

                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(activity);
                databaseHelper.setTmpPhoto(uri);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                activity.startActivityForResult(takePictureIntent, requestCode);
            }
        }
    }

    public static void dispatchGetGalleryPictureIntent(Activity activity, int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Photo"),
                requestCode);
    }

}
