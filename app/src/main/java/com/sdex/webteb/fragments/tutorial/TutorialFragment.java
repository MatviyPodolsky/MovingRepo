package com.sdex.webteb.fragments.tutorial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;

import com.sdex.webteb.R;
import com.sdex.webteb.fragments.BaseFragment;

import butterknife.InjectView;

public class TutorialFragment extends BaseFragment {

    @InjectView(R.id.content)
    RelativeLayout content;

    public static Fragment newInstance(int position) {
        TutorialFragment fragment = new TutorialFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switch (getArguments().getInt("position")) {
            case 0:
                content.setBackgroundResource(R.drawable.webteb_welcome_1);
                break;
            case 1:
                content.setBackgroundResource(R.drawable.webteb_welcome_2);
                break;
            case 2:
                content.setBackgroundResource(R.drawable.webteb_welcome_1);
                break;
            case 3:
                content.setBackgroundResource(R.drawable.webteb_welcome_2);
                break;
        }

    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_tutorial;
    }

}
