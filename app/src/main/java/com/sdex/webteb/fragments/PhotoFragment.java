package com.sdex.webteb.fragments;

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

    public static final int REQUEST_DIALOG = 2;

    public static final int PHOTO_TAKEN = 6001;
    public static final int PHOTO_SELECTED = 6002;

    public static final String PHOTO_PATH = "PHOTO_PATH";

    private static final String CAMERA_DIR = "/dcim/";
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    public static final String FILE_PREFIX = "file:///";

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


    protected Uri getGalleryPhotoUri(Uri selectedImage) {
        Cursor cursor = null;
        String filePath = null;
        try {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            cursor = getActivity().getContentResolver().query(
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

    private File getAlbumStorageDir(String albumName) {
        return new File(
                Environment.getExternalStorageDirectory()
                        + CAMERA_DIR
                        + albumName
        );
    }

    private File getAlbumDir(String albumName) {
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = getAlbumStorageDir(albumName);
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

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir("WebTeb");
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri uri = Uri.fromFile(photoFile);

                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity());
                databaseHelper.setTmpPhoto(uri);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                getActivity().startActivityForResult(takePictureIntent, PHOTO_TAKEN);
            }
        }
    }

    public void dispatchGetGalleryPictureIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(Intent.createChooser(intent, "Select Photo"),
                PHOTO_SELECTED);
    }

}