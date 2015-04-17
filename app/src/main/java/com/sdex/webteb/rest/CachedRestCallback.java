package com.sdex.webteb.rest;

import com.sdex.webteb.WTApp;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Author: Yuriy Mysochenko
 * Date: 17.04.2015
 */
public abstract class CachedRestCallback<T> implements Callback<T> {

    @Override
    public void failure(RetrofitError error) {
        try {
            RestError restError = (RestError) error.getBodyAs(RestError.class);
            if (restError != null) {
                failure(restError);
            } else {
                failure(new RestError(error.getMessage()));
            }
        } catch (Exception e) {
            int status = error.getResponse().getStatus();
            final RestError restError = new RestError(error.getMessage());
            restError.setCode(status);
            failure(restError);
        }
    }

    @Override
    public void success(T t, Response response) {
        HttpHeaderParser.CacheEntry cacheEntry = HttpHeaderParser.parseCacheHeaders(response.getHeaders());
        if (cacheEntry != null) {
            WTApp.getCacheManager().putAsync(response.getUrl(), t,
                    cacheEntry.maxAge, false, null);
        }
        success(t);
    }

    public abstract void success(T t);
    public abstract void failure(RestError restError);

}
