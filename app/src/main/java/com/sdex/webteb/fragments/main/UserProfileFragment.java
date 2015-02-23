package com.sdex.webteb.fragments.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.TagsAdapter;
import com.sdex.webteb.dialogs.PhotoDialog;
import com.sdex.webteb.utils.CameraHelper;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class UserProfileFragment extends BaseMainFragment {

    public static final int REQUEST_TAKE_PHOTO = 0;
    public static final int REQUEST_SELECT_PHOTO = 1;
    public static final int REQUEST_DIALOG = 2;

    @InjectView(R.id.tags)
    RecyclerView mRecyclerView;
    public static final int TAKE_PICTURE = 1221;
    private CameraHelper mCameraHelper;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCameraHelper = new CameraHelper(getActivity());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        final TagsAdapter adapter = new TagsAdapter();
        adapter.setItemCount(10);
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectedItem(position);
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_user_profile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                Toast.makeText(getActivity(), "PHOTO TOOK fragment", Toast.LENGTH_SHORT).show();
            }
            if (requestCode == REQUEST_SELECT_PHOTO) {
                Toast.makeText(getActivity(), "PHOTO SELECTED fragment", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.avatar)
    public void getAvatar(View v){
        DialogFragment dialog = new PhotoDialog();
        dialog.setTargetFragment(this, REQUEST_DIALOG);
        dialog.show(getFragmentManager(), null);
//        File CameraDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/" + "test_album");
//        File[] files = CameraDirectory.listFiles();
//        mCameraHelper.dispatchTakePictureIntent(TAKE_PICTURE);
    }
}
