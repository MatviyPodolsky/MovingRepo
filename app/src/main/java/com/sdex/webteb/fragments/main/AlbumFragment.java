package com.sdex.webteb.fragments.main;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.AlbumAdapter;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class AlbumFragment extends BaseMainFragment {

    public static List<String> cameraImages;

    @InjectView(R.id.grid_view)
    StickyGridHeadersGridView mGridView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<AlbumAdapter.Item> data = new ArrayList<>();

        cameraImages = getCameraImages(getActivity());

        for (String cameraImage : cameraImages) {
            File f = new File(cameraImage);
            AlbumAdapter.Item item = new AlbumAdapter.Item(cameraImage, f.getParent());
            data.add(item);
        }

//        for (int i = 0; i < 40; i++) {
//            AlbumAdapter.Item header = new AlbumAdapter.Item("header " + i, i);
//            data.add(header);
//            for (int j = 0; j < 13; j++) {
//                AlbumAdapter.Item item = new AlbumAdapter.Item("item " + j, i);
//                data.add(item);
//            }
//        }

        mGridView.setAdapter(new AlbumAdapter(getActivity(), data));
        mGridView.setAreHeadersSticky(false);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlbumViewFragment fragment = AlbumViewFragment.newInstance(position);

                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_album_container, fragment, "gg")
                        .addToBackStack("gg")
                        .commit();
            }
        });
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_album;
    }

    public ArrayList<String> getCameraImages(Context context) {

        // Set up an array of the Thumbnail Image ID column we want
        String[] projection = {MediaStore.Images.Media.DATA};


        final Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null);

        ArrayList<String> result = new ArrayList<>(cursor.getCount());

        Log.i("cursor.getCount()) :", cursor.getCount() + "");

        if (cursor.moveToFirst()) {
            final int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            do {
                final String data = "file:///" + cursor.getString(dataColumn);
                Log.i("data :", data);
                result.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return result;

    }

    private List<String> getPhotos() {
        String ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();

        String targetPath = ExternalStorageDirectoryPath + "/dcim/camera/";

        File targetDirector = new File(targetPath);

        List<String> data = new ArrayList<>();

        File[] files = targetDirector.listFiles();
        for (File file : files){
            final String absolutePath = file.getAbsolutePath();
            data.add(absolutePath);
        }
        return data;
    }
}