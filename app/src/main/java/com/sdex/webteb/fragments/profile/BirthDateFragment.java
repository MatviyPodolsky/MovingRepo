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
import com.sdex.webteb.utils.PreferencesManager;
import com.sdex.webteb.utils.ResourcesUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by MPODOLSKY on 03.02.2015.
 */
public class BirthDateFragment extends BaseFragment {

    public static final int EMPTY_DATA = -1;
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
    private Calendar lastSelectedDate;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDate.setText(ResourcesUtil.getString(getActivity(), "click_to_select_date"));

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
                    lastSelectedDate = DateUtil.getCalendarFromDate(date);
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
                        if (isValidDate(date)) {
                            ((SetupProfileActivity) getActivity()).setBirthDate(requestDate);
                            mDate.setText(textDate);
                            dateString = textDate;
                            lastSelectedDate = date;
                            updateChildAge(date);
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
            if (date != null && isValidDate(DateUtil.getCalendarFromDate(date))) {
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

    private void updateChildAge(Calendar calendar) {
        long selectedDate = calendar.getTime().getTime();
        long currentTime = DateUtil.getCurrentDate().getTime();
        long age = 0;
        int dateFormat;
        long diffTime = Math.abs(currentTime - selectedDate);
        //add half day to avoid some time changes like a transition to winter/daylight saving time
        diffTime = diffTime + 3600 * 1000 * 12;
        if (mDateType == BIRTH_DATE) {
            Calendar currentCalendar = DateUtil.getCurrentCalendar();
//            formula from Jacob
//            age = ((currentCalendar.get(Calendar.YEAR) - calendar.get(Calendar.YEAR))*12)
//                    + currentCalendar.get(Calendar.MONTH) - calendar.get(Calendar.MONTH);
            age = diffTime / 1000 / 3600 / 24 / 30;
            dateFormat = PreferencesManager.DATE_TYPE_MONTH;
        } else {
            if (mDateType == LAST_PERIOD) {
                age = diffTime / 1000 / 3600 / 24 / 7;
                dateFormat = PreferencesManager.DATE_TYPE_WEEK;
            } else {
                long currentWeek = (280 - diffTime / 1000 / 3600 / 24) / 7;
                age = (currentWeek < 0) ? 0 : currentWeek;
                dateFormat = PreferencesManager.DATE_TYPE_WEEK;
            }
        }
        if (getActivity() instanceof SetupProfileActivity) {
            ((SetupProfileActivity) getActivity()).setChildAge((int) age, dateFormat);
        }

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
        int dateFormat = -1;
        switch (category) {
            case LAST_PERIOD:
                clearCategories();
                mFirstCategory.setTextColor(getResources().getColor(R.color.selected_text));
                mFirstCategory.setBackgroundColor(getResources().getColor(R.color.primary));
                mDescription.setText(getString(R.string.last_period_description));
                mDateType = LAST_PERIOD;
                dateFormat = PreferencesManager.DATE_TYPE_WEEK;
                ((SetupProfileActivity) getActivity()).setDateType(mDateType);
                break;
            case DUE_TO:
                clearCategories();
                mSecondCategory.setTextColor(getResources().getColor(R.color.selected_text));
                mSecondCategory.setBackgroundColor(getResources().getColor(R.color.primary));
                mDescription.setText(getString(R.string.expected_birth_date_description));
                mDateType = DUE_TO;
                dateFormat = PreferencesManager.DATE_TYPE_WEEK;
                ((SetupProfileActivity) getActivity()).setDateType(mDateType);
                break;
            case BIRTH_DATE:
                clearCategories();
                mThirdCategory.setTextColor(getResources().getColor(R.color.selected_text));
                mThirdCategory.setBackgroundColor(getResources().getColor(R.color.primary));
                mDescription.setText(getString(R.string.birth_date_description));
                mDateType = BIRTH_DATE;
                dateFormat = PreferencesManager.DATE_TYPE_MONTH;
                ((SetupProfileActivity) getActivity()).setDateType(mDateType);
                break;
            default:
                break;
        }
        if (lastSelectedDate != null) {
            if (isValidDate(lastSelectedDate)) {
                updateChildAge(lastSelectedDate);
            } else {
                ((SetupProfileActivity) getActivity()).setChildAge(EMPTY_DATA, dateFormat);
            }
        }
    }

    public boolean isValidDate(Calendar calendar) {
        if (mDateType == BabyProfileResponse.DATE_TYPE_NOT_SET) {
            Toast.makeText(getActivity(), getString(R.string.please_select_date), Toast.LENGTH_SHORT).show();
            return false;
        }
        Date currentDate = DateUtil.getCurrentDate();
        Date date = (Date) calendar.getTime();
        if (mDateType == BabyProfileResponse.DATE_TYPE_BIRTH_DATE
                && DateUtil.compareDatesWithToday(date, currentDate, true)) {
            Toast.makeText(getActivity(), getString(R.string.please_select_correct_date), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mDateType == BabyProfileResponse.DATE_TYPE_LAST_PERIOD
                && DateUtil.compareDatesForLastPeriod(date, currentDate)) {
            Toast.makeText(getActivity(), getString(R.string.please_select_correct_date), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mDateType == BabyProfileResponse.DATE_TYPE_DUE_TO
                && DateUtil.compareDatesWithToday(currentDate, date, false)) {
            Toast.makeText(getActivity(), getString(R.string.please_select_correct_date), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
