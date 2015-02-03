package com.sdex.webteb.fragments.profile;

import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.activities.SetupProfileActivity;
import com.sdex.webteb.fragments.BaseFragment;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by MPODOLSKY on 03.02.2015.
 */
public class BirthDateFragment extends BaseFragment {

    @InjectView(R.id.category_1)
    TextView mFirstCategory;
    @InjectView(R.id.category_2)
    TextView mSecondCategory;
    @InjectView(R.id.category_3)
    TextView mThirdCategory;
    @InjectView(R.id.description)
    TextView mDescription;

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_birth_date;
    }

    @OnClick(R.id.next)
    public void scrollToNextPage() {
        if(getActivity() instanceof SetupProfileActivity){
            ((SetupProfileActivity) getActivity()).scrollToNextPage();
        }
    }

    @OnClick(R.id.category_1)
    public void selectFirstCategory(TextView v) {
        clearCategories();
        v.setTextColor(getResources().getColor(R.color.selected_text));
        mDescription.setText("category 1");
    }

    @OnClick(R.id.category_2)
    public void selectSecondCategory(TextView v) {
        clearCategories();
        v.setTextColor(getResources().getColor(R.color.selected_text));
        mDescription.setText("category 2");
    }

    @OnClick(R.id.category_3)
    public void selectThirdCategory(TextView v) {
        clearCategories();
        v.setTextColor(getResources().getColor(R.color.selected_text));
        mDescription.setText("category 3");
    }

    public void clearCategories(){
        mFirstCategory.setTextColor(getResources().getColor(R.color.primary));
        mSecondCategory.setTextColor(getResources().getColor(R.color.primary));
        mThirdCategory.setTextColor(getResources().getColor(R.color.primary));
    }
}
