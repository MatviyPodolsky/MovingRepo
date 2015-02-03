package com.sdex.webteb.fragments.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.activities.SetupProfileActivity;
import com.sdex.webteb.fragments.BaseFragment;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by MPODOLSKY on 03.02.2015.
 */
public class FamilyRelationFragment extends BaseFragment {

    @InjectView(R.id.family_relation)
    TextView relation;

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_family_relation;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.next)
    public void scrollToNextPage() {
        if(getActivity() instanceof SetupProfileActivity){
            ((SetupProfileActivity) getActivity()).scrollToNextPage();
        }
    }
}
