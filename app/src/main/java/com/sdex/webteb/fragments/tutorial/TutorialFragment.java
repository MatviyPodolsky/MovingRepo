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

    @InjectView(R.id.root)
    RelativeLayout root;
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
                root.setBackgroundResource(R.drawable.embryo_min);
                break;
            case 1:
                root.setBackgroundResource(R.drawable.tests_min);
//                content.setVisibility(View.VISIBLE);
                break;
            case 2:
                root.setBackgroundResource(R.drawable.baby_min);
                break;
            case 3:
                root.setBackgroundResource(R.drawable.tools_min);
                break;
        }

    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_tutorial;
    }

}
