package com.sdex.webteb.rest;

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
            String headerValue = accessTokenType + " " + accessToken;
            request.addHeader("Authorization", headerValue);
        }
    }

}
