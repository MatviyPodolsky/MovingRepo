package com.sdex.webteb.fragments.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.activities.SetupProfileActivity;
import com.sdex.webteb.dialogs.FamilyRelationDialog;
import com.sdex.webteb.fragments.BaseFragment;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by MPODOLSKY on 03.02.2015.
 */
public class FamilyRelationFragment extends BaseFragment {

    public static final int REQUEST_GET_RELATION = 0;
    public static final String RELATIONS_LIST = "RELATIONS_LIST";

    @InjectView(R.id.family_relation)
    TextView relation;

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_family_relation;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle data = getArguments();
        if(data != null && data.containsKey(SetupProfileActivity.FAMILY_RELATION)) {
            String[] relations = getResources().getStringArray(R.array.relations);
            int relationPosition = data.getInt(SetupProfileActivity.FAMILY_RELATION, 0);
            relation.setText(relations[relationPosition]);
            ((SetupProfileActivity) getActivity()).setFamilyRelation(
                    relationPosition);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_GET_RELATION:
                    String extra = data.getStringExtra(FamilyRelationDialog.EXTRA_RELATION);
                    relation.setText(extra);
                    ((SetupProfileActivity)getActivity()).setFamilyRelation(
                            data.getIntExtra(FamilyRelationDialog.EXTRA_POSITION, 0));
                    break;
            }
        }
    }

    @OnClick(R.id.next)
    public void scrollToNextPage() {
        if(getActivity() instanceof SetupProfileActivity){
            ((SetupProfileActivity) getActivity()).scrollToNextPage();
        }
    }

    @OnClick(R.id.linearFamilyRelation)
    public void selectFamilyRelation() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment dialog = new FamilyRelationDialog();
        Bundle args = new Bundle();
        args.putStringArray(RELATIONS_LIST, getResources().getStringArray(R.array.relations));
        dialog.setArguments(args);
        dialog.setTargetFragment(this, REQUEST_GET_RELATION);
        dialog.show(ft, "dialog");
    }
}
