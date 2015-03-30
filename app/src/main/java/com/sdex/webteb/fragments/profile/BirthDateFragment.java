package com.sdex.webteb.fragments.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.activities.SetupProfileActivity;
import com.sdex.webteb.dialogs.DatePickerFragmentDialog;
import com.sdex.webteb.fragments.BaseFragment;
import com.sdex.webteb.rest.response.BabyProfileResponse;
import com.sdex.webteb.utils.DateUtil;

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
    @InjectView(R.id.next)
    Button mBtnNext;
    private int mDateType = LAST_PERIOD;
    private String dateString;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle data = getArguments();
        selectCategory(LAST_PERIOD);
        if (data != null) {
            int dateType = data.getInt(SetupProfileActivity.DATE_TYPE, LAST_PERIOD);
            String dateStr = data.getString(SetupProfileActivity.DATE);
            selectCategory(dateType);
            setButtonEnabled(true);
            ((SetupProfileActivity) getActivity()).setBirthDate(dateStr);
            ((SetupProfileActivity) getActivity()).setDateType(dateType);
            if (dateStr != null) {
                Date date = DateUtil.parseDate(dateStr);
                if (date != null) {
                    dateStr = DateUtil.formatDate(date, "MMM dd, yyyy");
                }
            }
            mDate.setText(dateStr);
            dateString = dateStr;
        } else {
            setButtonEnabled(false);
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
                    setButtonEnabled(true);
                    int year = data.getIntExtra(DatePickerFragmentDialog.EXTRA_YEAR, -1);
                    int month = data.getIntExtra(DatePickerFragmentDialog.EXTRA_MONTH, -1);
                    int day = data.getIntExtra(DatePickerFragmentDialog.EXTRA_DAY, -1);
                    if (year >= 0 && month >= 0 && day >= 0) {
                        //set time to 12:00:00 to show that day begins
                        Calendar date = new GregorianCalendar(year, month, day);
                        Date time = date.getTime();
                        String requestDate = DateUtil.formatDate(time, "yyyy-MM-dd'T'HH:mm:ssZ");
                        String textDate = DateUtil.formatDate(time, "MMM dd, yyyy");
                        if (isValidDate(time)) {
                            ((SetupProfileActivity) getActivity()).setBirthDate(requestDate);
                            mDate.setText(textDate);
                            long selectedDate = time.getTime();
                            long currentTime = Calendar.getInstance().getTime().getTime();
                            long age = 0;
                            long diffTime = Math.abs(currentTime - selectedDate);
                            if(mDateType == BIRTH_DATE){
                                age = diffTime / 1000 / 3600 / 24 / 30;
                            } else {
                                if(mDateType == LAST_PERIOD) {
                                    age = diffTime / 1000 / 3600 / 24 / 7;
                                } else {
                                    long currentWeek = (280 - diffTime / 1000 / 3600 / 24) / 7;
                                    age = (currentWeek < 0) ? 0 : currentWeek;
                                }
                            }
                            dateString = textDate;
                            if (getActivity() instanceof SetupProfileActivity) {
                                    ((SetupProfileActivity) getActivity()).setChildAge(String.valueOf(age));
                            }
                        }
                    }
                    break;
            }
        }
    }

    @OnClick(R.id.next)
    public void scrollToNextPage() {
        if (getActivity() instanceof SetupProfileActivity) {
            Date date = DateUtil.parseDate(mDate.getText().toString());
            if (date != null && isValidDate(date)) {
                ((SetupProfileActivity) getActivity()).scrollToNextPage();
            }
        }
    }

    @OnClick(R.id.container_select_date)
    public void selectDate() {
        DialogFragment dialog = new DatePickerFragmentDialog();
        dialog.setTargetFragment(this, REQUEST_GET_DATE);
        if (!TextUtils.isEmpty(dateString)) {
            Bundle args = new Bundle();
            args.putString(DatePickerFragmentDialog.EXTRA_DATE, dateString);
            dialog.setArguments(args);
        }
        dialog.show(getFragmentManager(), null);
    }

    private void setButtonEnabled(boolean isEnabled) {
        mBtnNext.setEnabled(isEnabled);
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

    public void clearCategories() {
        mFirstCategory.setTextColor(getResources().getColor(R.color.primary));
        mSecondCategory.setTextColor(getResources().getColor(R.color.primary));
        mThirdCategory.setTextColor(getResources().getColor(R.color.primary));
        mFirstCategory.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mSecondCategory.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mThirdCategory.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    private void selectCategory(int category) {
        switch (category) {
            case LAST_PERIOD:
                clearCategories();
                mFirstCategory.setTextColor(getResources().getColor(R.color.selected_text));
                mFirstCategory.setBackgroundColor(getResources().getColor(R.color.primary));
                mDescription.setText(getString(R.string.last_period_description));
                mDateType = LAST_PERIOD;
                ((SetupProfileActivity) getActivity()).setDateType(mDateType);
                break;
            case DUE_TO:
                clearCategories();
                mSecondCategory.setTextColor(getResources().getColor(R.color.selected_text));
                mSecondCategory.setBackgroundColor(getResources().getColor(R.color.primary));
                mDescription.setText(getString(R.string.expected_birth_date_description));
                mDateType = DUE_TO;
                ((SetupProfileActivity) getActivity()).setDateType(mDateType);
                break;
            case BIRTH_DATE:
                clearCategories();
                mThirdCategory.setTextColor(getResources().getColor(R.color.selected_text));
                mThirdCategory.setBackgroundColor(getResources().getColor(R.color.primary));
                mDescription.setText(getString(R.string.birth_date_description));
                mDateType = BIRTH_DATE;
                ((SetupProfileActivity) getActivity()).setDateType(mDateType);
                break;
            default:
                break;
        }
    }

    public boolean isValidDate(Date date) {
        if (mDateType == BabyProfileResponse.DATE_TYPE_NOT_SET) {
            Toast.makeText(getActivity(), getString(R.string.please_select_date), Toast.LENGTH_SHORT).show();
            return false;
        }
        Date currentDate = Calendar.getInstance().getTime();
        if (mDateType == BabyProfileResponse.DATE_TYPE_BIRTH_DATE
                && DateUtil.compareDatesWithToday(date, currentDate)) {
            Toast.makeText(getActivity(), getString(R.string.birth_date_cant_be_in_future), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mDateType == BabyProfileResponse.DATE_TYPE_LAST_PERIOD
                && DateUtil.compareDatesForLastPeriod(date, currentDate)) {
            Toast.makeText(getActivity(), getString(R.string.last_period_cant_be_in_future), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mDateType == BabyProfileResponse.DATE_TYPE_DUE_TO
                && DateUtil.compareDatesWithToday(currentDate, date)) {
            Toast.makeText(getActivity(), getString(R.string.expected_birth_date_cant_be_in_past), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
