package com.sdex.webteb.fragments.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.dialogs.SearchFilterDialog;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class SearchDoctorFragment extends BaseMainFragment {

    public static final int REQUEST_GET_COUNTRY = 0;
    public static final int REQUEST_GET_CITY = 1;
    public static final int REQUEST_GET_SPECIALITY = 2;
    public static final String LIST = "LIST";
    public static final String REQUEST = "REQUEST";

    @InjectView(R.id.search)
    EditText search;
    @InjectView(R.id.country_text)
    TextView country;
    @InjectView(R.id.city_text)
    TextView city;
    @InjectView(R.id.specialty_text)
    TextView specialty;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                    Fragment fragment = new SearchResultsFragment();
                    FragmentManager fragmentManager = getChildFragmentManager();
                    fragmentManager.beginTransaction()
                            .add(R.id.fragment_container, fragment, "content_fragment")
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_search_doctor;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_GET_COUNTRY:
                    if(data != null) {
                        country.setText(data.getStringExtra(SearchFilterDialog.EXTRA_DATA));
                    }
                    break;
                case REQUEST_GET_CITY:
                    if(data != null) {
                        city.setText(data.getStringExtra(SearchFilterDialog.EXTRA_DATA));
                    }
                    break;
                case REQUEST_GET_SPECIALITY:
                    if(data != null) {
                        specialty.setText(data.getStringExtra(SearchFilterDialog.EXTRA_DATA));
                    }
                    break;
            }
        }
    }

    @OnClick(R.id.country)
    public void selectCountry() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment dialog = new SearchFilterDialog();
        Bundle args = new Bundle();
        args.putStringArray(LIST, getResources().getStringArray(R.array.countries));
        args.putInt(REQUEST, REQUEST_GET_COUNTRY);
        dialog.setArguments(args);
        dialog.setTargetFragment(this, REQUEST_GET_COUNTRY);
        dialog.show(ft, "dialog");
    }

    @OnClick(R.id.city)
    public void selectCity() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment dialog = new SearchFilterDialog();
        Bundle args = new Bundle();
        args.putStringArray(LIST, getResources().getStringArray(R.array.cities));
        args.putInt(REQUEST, REQUEST_GET_CITY);
        dialog.setArguments(args);
        dialog.setTargetFragment(this, REQUEST_GET_CITY);
        dialog.show(ft, "dialog");
    }

    @OnClick(R.id.specialty)
    public void selectSpeciality() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment dialog = new SearchFilterDialog();
        Bundle args = new Bundle();
        args.putStringArray(LIST, getResources().getStringArray(R.array.specialities));
        args.putInt(REQUEST, REQUEST_GET_SPECIALITY);
        dialog.setArguments(args);
        dialog.setTargetFragment(this, REQUEST_GET_SPECIALITY);
        dialog.show(ft, "dialog");
    }

}
