package com.sdex.webteb.rest;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Yuriy Mysochenko on 26.02.2015.
 */
public abstract class RestCallback<T> implements CancellableRestCallback<T> {

    private boolean isCancelled;

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
        if(isCancelled()) return;
        success(t, response);
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

}
