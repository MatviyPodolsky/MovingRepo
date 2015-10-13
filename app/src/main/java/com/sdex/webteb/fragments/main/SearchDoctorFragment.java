package com.sdex.webteb.fragments.main;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.database.DatabaseHelper;
import com.sdex.webteb.database.model.DbLocation;
import com.sdex.webteb.dialogs.SearchFilterDialog;
import com.sdex.webteb.internal.events.DoctorsFoundEvent;
import com.sdex.webteb.internal.events.DoctorsNotFoundEvent;
import com.sdex.webteb.model.Ad;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.response.CityResponse;
import com.sdex.webteb.rest.response.SpecialtiesResponse;
import com.sdex.webteb.utils.AdUtil;
import com.sdex.webteb.utils.KeyboardUtils;
import com.sdex.webteb.utils.PreferencesManager;

import java.util.List;
import java.util.Locale;

import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;
import de.greenrobot.event.EventBus;
import retrofit.client.Response;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class SearchDoctorFragment extends BaseMainFragment {

    public static final String NAME = SearchDoctorFragment.class.getSimpleName();

    public static final int REQUEST_GET_COUNTRY = 0;
    public static final int REQUEST_GET_CITY = 1;
    public static final int REQUEST_GET_SPECIALITY = 2;
    public static final int ANY_SPECIALITY_ID = -99;
    public static final String LIST = "LIST";
    public static final String REQUEST = "REQUEST";
    public static final String REQUEST_STRING = "REQUEST_STRING";

    public static final long MIN_TIME_BW_UPDATES = 10;
    public static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10f;
    private boolean canGetLocation;
    private String[] citiesList;
    private String[] specialtiesList;
    private String[] countriesList;
    private int[] countryIds;
    private int[] citiesIds;
    private int[] specialitiesIds;
    private String[] countryCodes;
    private int currentCountryPosition = 0;  //Any country

    private EventBus mEventBus = EventBus.getDefault();

    private PreferencesManager mPreferencesManager;
    private DatabaseHelper databaseHelper;

    @InjectView(R.id.search)
    EditText mSearch;
    @InjectView(R.id.country_text)
    TextView mCountry;
    @InjectView(R.id.city_text)
    TextView mCity;
    @InjectView(R.id.specialty_text)
    TextView mSpecialty;
    @InjectView(R.id.error_view)
    View mErrorView;
    @InjectView(R.id.query)
    TextView mQuery;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sendAnalyticsScreenName(R.string.screen_search_doctor);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        countriesList = getActivity().getResources().getStringArray(R.array.countries);
        countryIds = getActivity().getResources().getIntArray(R.array.country_ids);
        countryCodes = getActivity().getResources().getStringArray(R.array.iso_codes);
        citiesList = new String[] {getString(R.string.any_city)};
        specialtiesList = new String[] {getString(R.string.any_speciality)};

        databaseHelper = DatabaseHelper.getInstance(getActivity());
        mPreferencesManager = PreferencesManager.getInstance();

        String username = mPreferencesManager.getEmail();

        DbLocation dbLocation = databaseHelper.getLocation(username);
        if (!(dbLocation != null && checkoutCountry(dbLocation.getCountry()))) {
            setCurrentCountry(currentCountryPosition);
        }
        setSpecialties();

//        AdUtil.initInterstitialAd(getActivity(), name, Ad.INTERSTITIAL,
//                item.getContentPreview().getTargeting());
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_search_doctor;
    }

    @Override
    public void onStart() {
        super.onStart();
        mEventBus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mEventBus.unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_GET_COUNTRY:
                    if (data != null) {
                        mCountry.setText(data.getStringExtra(SearchFilterDialog.EXTRA_DATA));
                        currentCountryPosition = data.getIntExtra(SearchFilterDialog.EXTRA_POSITION, 3);
                        setCurrentCountry(currentCountryPosition);
                    }
                    break;
                case REQUEST_GET_CITY:
                    if (data != null) {
                        mCity.setText(data.getStringExtra(SearchFilterDialog.EXTRA_DATA));
                    }
                    break;
                case REQUEST_GET_SPECIALITY:
                    if (data != null) {
                        mSpecialty.setText(data.getStringExtra(SearchFilterDialog.EXTRA_DATA));
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
        if (citiesList.length > 1) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            DialogFragment dialog = new SearchFilterDialog();
            Bundle args = new Bundle();
            args.putStringArray(LIST, citiesList);
            args.putInt(REQUEST, REQUEST_GET_CITY);
            dialog.setArguments(args);
            dialog.setTargetFragment(this, REQUEST_GET_CITY);
            dialog.show(ft, "dialog");
        } else {
            Toast.makeText(getActivity(), getString(R.string.please_select_country), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.specialty)
    public void selectSpeciality() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment dialog = new SearchFilterDialog();
        Bundle args = new Bundle();
        args.putStringArray(LIST, specialtiesList);
        args.putInt(REQUEST, REQUEST_GET_SPECIALITY);
        dialog.setArguments(args);
        dialog.setTargetFragment(this, REQUEST_GET_SPECIALITY);
        dialog.show(ft, "dialog");
    }

    @OnClick(R.id.btn_search)
    public void search() {
        KeyboardUtils.hideKeyboard(mSearch);
        Fragment fragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        args.putString("Name", mSearch.getText().toString());

        String countryName = mCountry.getText().toString();
        String countryId = String.valueOf(getIdItemFromString(countryName, countriesList, REQUEST_GET_COUNTRY));
        if (!countryName.equals(getString(R.string.any_country))) {
            args.putString("Country", countryId);
        }

        String cityName = mCity.getText().toString();
        String cityId = String.valueOf(getIdItemFromString(cityName, citiesList, REQUEST_GET_CITY));
        if (!cityName.equals(getString(R.string.any_city))) {
            args.putString("City", cityId);
        }

        String specialityName = mSpecialty.getText().toString();
        String specialityId = String.valueOf(getIdItemFromString(specialityName, specialtiesList, REQUEST_GET_SPECIALITY));
        if (!specialityName.equals(getString(R.string.any_speciality))) {
            args.putString("Specialty", specialityId);
        } else {
            args.putString("Specialty", String.valueOf(ANY_SPECIALITY_ID));
        }
        setDbLocation(countryCodes[getCountryPosition(countryName)], cityName);
        fragment.setArguments(args);

        addNestedFragment(R.id.fragment_container, fragment, SearchResultsFragment.NAME);
    }

    private void setCurrentCountry(int currentCountry) {
        this.currentCountryPosition = currentCountry;
        String countryName = countriesList[currentCountry];
        String countryIso = countryCodes[currentCountry];
        mCountry.setText(countryName);
        setCities(countryCodes[currentCountry]);

        String username = mPreferencesManager.getEmail();
        DbLocation dbLocation = databaseHelper.getLocation(username);
        if (dbLocation != null && dbLocation.getCountry().equals(countryIso)
                && !TextUtils.isEmpty(dbLocation.getCity())) {
            mCity.setText(dbLocation.getCity());
        } else {
            mCity.setText(getString(R.string.any_city));
        }
    }

    private int getCountryPosition(String countryName) {
        for (int i = 0; i < countriesList.length; i++) {
            if (countryName.equals(countriesList[i])) {
                return i;
            }
        }
        return 0;
    }

    private void setDbLocation(String countryName, String cityName) {
        String username = mPreferencesManager.getEmail();
        if (databaseHelper.getLocation(username) != null) {
            databaseHelper.deleteLocation(databaseHelper.getLocation(username));
        }
        DbLocation dbLocation = new DbLocation();
        dbLocation.setOwner(username);
        dbLocation.setCountry(countryName);
        dbLocation.setCity(cityName);

        databaseHelper.setLocation(dbLocation);
    }

    private int getIdItemFromString(String str, String[] list, int requestCode) {
        for (int i = list.length - 1; i > 0; i--) {
            if (str.equals(list[i])) {
                if (requestCode == REQUEST_GET_COUNTRY) {
                    return countryIds[i];
                } else if (requestCode == REQUEST_GET_CITY) {
                    return citiesIds[i];
                } else if (requestCode == REQUEST_GET_SPECIALITY) {
                    return specialitiesIds[i];
                }
            }
        }
        return 0;
    }

    private boolean checkoutCountry(String isoCode) {
        for (int i = 0; i < countryCodes.length; i++) {
            if (countryCodes[i].equals(isoCode)) {
                setCurrentCountry(i);
                return true;
            }
        }
        return false;
    }

    private void setCurrentLocation() {

        new AsyncTask<Void, Void, List<Address>>() {

            @Override
            protected List<Address> doInBackground(Void... params) {
                Locale defaultLocale = getActivity().getResources().getConfiguration().locale;
                List<Address> addresses = null;

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
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(),
                                        getString(R.string.turn_location_options), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Geocoder geocoder = new Geocoder(getActivity(), defaultLocale);
                        canGetLocation = true;
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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return addresses;
            }

            @Override
            protected void onPostExecute(List<Address> addressList) {
                super.onPostExecute(addressList);
                if (addressList != null) {
                    checkoutCountry(addressList.get(0).getCountryCode());
                }
            }

        }.execute();
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

    private void setCities(String isoCode) {
        RestCallback<List<CityResponse>> getCitiesCallback = new RestCallback<List<CityResponse>>() {
            @Override
            public void failure(RestError restError) {

            }

            @Override
            public void success(List<CityResponse> cities, Response response) {
                if (cities != null) {
                    int sizeArray = cities.size() + 1;
                    citiesList = new String[sizeArray];
                    citiesIds = new int[sizeArray];
                    citiesList[0] = getString(R.string.any_city);
                    citiesIds[0] = 0;

                    for (int i = 0; i < cities.size(); i++) {
                        citiesList[i + 1] = cities.get(i).getName();
                        citiesIds[i + 1] = cities.get(i).getId();
                    }
                    int i = 0;
                }
            }
        };
        RestClient.getApiService().getCities(isoCode, getCitiesCallback);
    }

    private void setSpecialties() {
        RestCallback<List<SpecialtiesResponse>> getSpecialtiesCallback = new RestCallback<List<SpecialtiesResponse>>() {
            @Override
            public void failure(RestError restError) {

            }

            @Override
            public void success(List<SpecialtiesResponse> specialties, Response response) {
                if (specialties != null) {
                    int sizeArray = specialties.size() + 1;
                    specialtiesList = new String[sizeArray];
                    specialitiesIds = new int[sizeArray];
                    specialtiesList[0] = getString(R.string.any_speciality);
                    specialitiesIds[0] = ANY_SPECIALITY_ID;

                    for (int i = 0; i < specialties.size(); i++) {
                        specialtiesList[i + 1] = specialties.get(i).getName();
                        specialitiesIds[i + 1] = specialties.get(i).getId();
                    }
                }
            }
        };
        RestClient.getApiService().getSpecialties(getSpecialtiesCallback);
    }

    public void onEvent(DoctorsNotFoundEvent event) {
        mQuery.setText(mSearch.getText());
        int height = getResources().getDimensionPixelSize(R.dimen.notification_bar_height);
        mErrorView.getLayoutParams().height = height;
        mErrorView.requestLayout();
    }

    public void onEvent(DoctorsFoundEvent event) {
        if (mErrorView.getLayoutParams().height != 0) {
            mErrorView.getLayoutParams().height = 0;
            mErrorView.requestLayout();
        }
    }

    private void showError() {
        int height = getResources().getDimensionPixelSize(R.dimen.notification_bar_height);
        ValueAnimator va = ValueAnimator.ofInt(0, height);
        va.setDuration(500);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mErrorView != null) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    mErrorView.getLayoutParams().height = value;
                    mErrorView.requestLayout();
                }
            }
        });
        va.start();
    }

    @Optional
    @OnClick(R.id.hide_error)
    void hideError() {
        if (mErrorView.getLayoutParams().height != 0) {
            int height = getResources().getDimensionPixelSize(R.dimen.notification_bar_height);
            ValueAnimator va = ValueAnimator.ofInt(height, 0);
            va.setDuration(500);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (mErrorView != null) {
                        Integer value = (Integer) animation.getAnimatedValue();
                        mErrorView.getLayoutParams().height = value;
                        mErrorView.requestLayout();
                    }
                }
            });
            va.start();
        }
    }

}
