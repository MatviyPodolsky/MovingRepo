package com.sdex.webteb.fragments.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.sdex.webteb.R;
import com.sdex.webteb.activities.SetupProfileActivity;
import com.sdex.webteb.adapters.ChildrenAdapter;
import com.sdex.webteb.fragments.BaseFragment;
import com.sdex.webteb.model.Child;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by MPODOLSKY on 03.02.2015.
 */
public class ChildInfoFragment extends BaseFragment {

    private ChildrenAdapter mAdapter;
    @InjectView(R.id.childs_list)
    ListView mList;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new ChildrenAdapter(getActivity(), new ArrayList<Child>());
        mList.setAdapter(mAdapter);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_child_info;
    }

    @OnClick(R.id.add)
    public void addChild(){
        mAdapter.add(new Child());
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.done)
    public void launchMainActivity() {
        if(getActivity() instanceof SetupProfileActivity){
            ((SetupProfileActivity) getActivity()).launchMainActivity();
        }
    }
}
