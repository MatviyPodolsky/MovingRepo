package com.sdex.webteb.fragments.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sdex.webteb.R;

import butterknife.InjectView;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class AboutFragment extends BaseMainFragment {

    private final String TAG = "AboutFragment:";

    @InjectView(R.id.textAppVersion)
    TextView appVersion;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String versionName = null;
        try {
            Activity activity = getActivity();
            versionName = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
        } catch (Exception e) {
            Log.e(TAG, "Try to get version name: " + e.getMessage());
            e.printStackTrace();
        }
        appVersion.setText(versionName + " application version");
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_about;
    }

}
