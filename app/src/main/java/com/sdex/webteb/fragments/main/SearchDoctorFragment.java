package com.sdex.webteb.fragments.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.dialogs.SearchFilterDialog;

import java.util.List;
import java.util.Locale;

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
    public static final String REQUEST_STRING = "REQUEST_STRING";

    public static final long MIN_TIME_BW_UPDATES = 10;
    public static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10f;
    private boolean canGetLocation;

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
        setCurrentLocation();
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                    Fragment fragment = new SearchResultsFragment();
                    Bundle args = new Bundle();
//                    args.putStringArray(REQUEST_STRING, buildSearchString());
                    args.putString("Name", search.getText().toString());
                    args.putString("Country", country.getText().toString());
                    args.putString("City", city.getText().toString());
                    args.putString("Specialty", specialty.getText().toString());
                    fragment.setArguments(args);
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
                    if (data != null) {
                        country.setText(data.getStringExtra(SearchFilterDialog.EXTRA_DATA));
                    }
                    break;
                case REQUEST_GET_CITY:
                    if (data != null) {
                        city.setText(data.getStringExtra(SearchFilterDialog.EXTRA_DATA));
                    }
                    break;
                case REQUEST_GET_SPECIALITY:
                    if (data != null) {
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

    private void setCurrentLocation(){
        Locale defaultLocale = getActivity().getResources().getConfiguration().locale;

        LocationManager locationManager = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);
        double latitude = 0, longitude = 0;
        Location location = null;
        try {
            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                Toast.makeText(getActivity(), "No network provider is enabled", Toast.LENGTH_SHORT).show();
            } else {
                Geocoder geocoder = new Geocoder(getActivity(), defaultLocale);
                List<Address> addresses = null;
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                    Log.d("Network", "Network Enabled");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                } else {
                    // if GPS Enabled get lat/long using GPS Services
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                            Log.d("GPS", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                this.country.setText(addresses.get(0).getCountryName());
                this.city.setText(addresses.get(0).getLocality());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

    };
}
