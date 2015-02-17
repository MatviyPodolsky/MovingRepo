package com.sdex.webteb.fragments.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.activities.SetupProfileActivity;
import com.sdex.webteb.dialogs.DatePickerFragmentDialog;
import com.sdex.webteb.fragments.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by MPODOLSKY on 03.02.2015.
 */
public class BirthDateFragment extends BaseFragment {

    public static final int REQUEST_GET_DATE = 0;

    @InjectView(R.id.category_1)
    TextView mFirstCategory;
    @InjectView(R.id.category_2)
    TextView mSecondCategory;
    @InjectView(R.id.category_3)
    TextView mThirdCategory;
    @InjectView(R.id.description)
    TextView mDescription;
    @InjectView(R.id.select_date)
    TextView mDate;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_birth_date;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_GET_DATE:
                    int year = data.getIntExtra(DatePickerFragmentDialog.EXTRA_YEAR, -1);
                    int month = data.getIntExtra(DatePickerFragmentDialog.EXTRA_MONTH, -1);
                    int day = data.getIntExtra(DatePickerFragmentDialog.EXTRA_DAY, -1);
                    if(year >=0 && month>=0 && day>=0) {
                        mDate.setText(year + " " + month + " " + day);
                        Calendar date = new GregorianCalendar(year,month,day,0,0);
                        SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                        String dt = outFormat.format(date.getTime());
                        ((SetupProfileActivity)getActivity()).setBirthDate(dt);
                    }
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

    @OnClick(R.id.select_date)
    public void selectDate() {
        DialogFragment dialog = new DatePickerFragmentDialog();
        dialog.setTargetFragment(this, REQUEST_GET_DATE);
        dialog.show(getFragmentManager(), null);
    }

    @OnClick(R.id.category_1)
    public void selectFirstCategory(TextView v) {
        clearCategories();
        v.setTextColor(getResources().getColor(R.color.selected_text));
        v.setBackgroundColor(getResources().getColor(R.color.primary));
        mDescription.setText("category 1");
    }

    @OnClick(R.id.category_2)
    public void selectSecondCategory(TextView v) {
        clearCategories();
        v.setTextColor(getResources().getColor(R.color.selected_text));
        v.setBackgroundColor(getResources().getColor(R.color.primary));
        mDescription.setText("category 2");
    }

    @OnClick(R.id.category_3)
    public void selectThirdCategory(TextView v) {
        clearCategories();
        v.setTextColor(getResources().getColor(R.color.selected_text));
        v.setBackgroundColor(getResources().getColor(R.color.primary));
        mDescription.setText("category 3");
    }

    public void clearCategories(){
        mFirstCategory.setTextColor(getResources().getColor(R.color.primary));
        mSecondCategory.setTextColor(getResources().getColor(R.color.primary));
        mThirdCategory.setTextColor(getResources().getColor(R.color.primary));
        mFirstCategory.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mSecondCategory.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mThirdCategory.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }
}
