package com.sdex.webteb.rest;

import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by Yuriy Mysochenko on 09.02.2015.
 */
public abstract class RestCallback<T> implements Callback<T> {

    public abstract void failure(RestError restError);

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

}
