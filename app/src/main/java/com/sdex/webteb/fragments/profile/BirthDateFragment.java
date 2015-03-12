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
import com.sdex.webteb.utils.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by MPODOLSKY on 03.02.2015.
 */
public class BirthDateFragment extends BaseFragment {

    public static final int REQUEST_GET_DATE = 0;
    public static final int LAST_PERIOD = 1;
    public static final int DUE_TO = 2;
    public static final int BIRTH_DATE = 3;

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
    private int mDateType = LAST_PERIOD;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle data = getArguments();
        if(data != null) {
            int dateType = data.getInt(SetupProfileActivity.DATE_TYPE, LAST_PERIOD);
            String dateStr = data.getString(SetupProfileActivity.DATE);
            selectCategory(dateType);
            ((SetupProfileActivity) getActivity()).setBirthDate(dateStr, dateType);
            if(dateStr != null){
                Date date = DateUtil.parseDate(dateStr);
                if(date != null){
                    dateStr = DateUtil.formatDate(date);
                }
//                SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
//                try {
//                    date = inFormat.parse(dateStr);
//                    SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd");
//                    dateStr = outFormat.format(date);
//                }catch(Exception ex){
//                    ex.printStackTrace();
//                }
            }
            mDate.setText(dateStr);
        }
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
                        Calendar date = new GregorianCalendar(year,month,day);
                        mDate.setText(DateUtil.formatDate(date.getTime()));
                        SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                        String dt = outFormat.format(date.getTime());
                        ((SetupProfileActivity)getActivity()).setBirthDate(dt, mDateType);
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
        selectCategory(LAST_PERIOD);
    }

    @OnClick(R.id.category_2)
    public void selectSecondCategory(TextView v) {
        selectCategory(DUE_TO);
    }

    @OnClick(R.id.category_3)
    public void selectThirdCategory(TextView v) {
        selectCategory(BIRTH_DATE);

    }

    public void clearCategories(){
        mFirstCategory.setTextColor(getResources().getColor(R.color.primary));
        mSecondCategory.setTextColor(getResources().getColor(R.color.primary));
        mThirdCategory.setTextColor(getResources().getColor(R.color.primary));
        mFirstCategory.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mSecondCategory.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mThirdCategory.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    private void selectCategory(int category){
        switch (category){
            case LAST_PERIOD:
                clearCategories();
                mFirstCategory.setTextColor(getResources().getColor(R.color.selected_text));
                mFirstCategory.setBackgroundColor(getResources().getColor(R.color.primary));
                mDescription.setText("category 1");
                mDateType = LAST_PERIOD;
                break;
            case DUE_TO:
                clearCategories();
                mSecondCategory.setTextColor(getResources().getColor(R.color.selected_text));
                mSecondCategory.setBackgroundColor(getResources().getColor(R.color.primary));
                mDescription.setText("category 2");
                mDateType = DUE_TO;
                break;
            case BIRTH_DATE:
                clearCategories();
                mThirdCategory.setTextColor(getResources().getColor(R.color.selected_text));
                mThirdCategory.setBackgroundColor(getResources().getColor(R.color.primary));
                mDescription.setText("category 3");
                mDateType = BIRTH_DATE;
                break;
            default:
                break;
        }
    }
}
