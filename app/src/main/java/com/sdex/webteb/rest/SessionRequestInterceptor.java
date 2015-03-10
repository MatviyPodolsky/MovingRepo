package com.sdex.webteb.rest;

import android.os.Build;

import com.sdex.webteb.BuildConfig;
import com.sdex.webteb.utils.PreferencesManager;

import retrofit.RequestInterceptor;

/**
 * Created by Yuriy Mysochenko on 09.02.2015.
 */
public class SessionRequestInterceptor implements RequestInterceptor {

    private PreferencesManager preferencesManager = PreferencesManager.getInstance();

    @Override
    public void intercept(RequestFacade request) {
        final String accessToken = preferencesManager.getAccessToken();
        if (accessToken != null) {
            final String accessTokenType = preferencesManager.getAccessTokenType();
            String authHeaderValue = accessTokenType + " " + accessToken;
            request.addHeader("Authorization", authHeaderValue);
        }
        request.addHeader("ApplicationVersion", BuildConfig.VERSION_NAME);
        request.addHeader("DeviceType", "DeviceType");
        request.addHeader("ApplicationName", "baby");
        request.addHeader("OS", "Android");
        request.addHeader("OSVersion", Build.VERSION.RELEASE);
    }

}
