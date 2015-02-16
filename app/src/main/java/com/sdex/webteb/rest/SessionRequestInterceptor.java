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
//            String headerValue = "Bearer BshnJs-XJG5pQIXIxyolSyiUojSZBOqP8K1oQot0U9E0W3Ehq0yZovg7rzpyUpFLCnVNYJeHFCPwnl8u1uhYQfQ8LyrwWMC62gm_AouRc3c7ornrBIcar9loZ3o9-SO33MxbfCn2oRYcUnQsRLh10u9Owq_8ZUYTkXX9W6QJfemBU1j-mX7F3EeT4k8W5YZbeaQeF1X0ehc7QWmnA0pgFFgJADVn_Hy2ifU4912siu1XVnKSxRnO8pqUaZHC_Kws-W0WD1N5aQmMV-dgioinAqrlyRx6H9Qv4iil1jJ1asHBE4yiSInkgllUSvSgAKgPXbmqCU14esHnCv_W-1ZkTb-nuARc8Eyzydviu_X6zb2KQOCet3AyeijDTWaXTg8Mlv1ijXVZMMG4MVke3lLX5zhpLt9FIjDWVq6uyk6wTPFU_eaJkUL_0RsHNM6b19TMuHvOk7NL_p-Vshthp8CY28vWrrZVftdsoffFtusHD6nGy8Wo";
            request.addHeader("Authorization", headerValue);
        }
    }

}
