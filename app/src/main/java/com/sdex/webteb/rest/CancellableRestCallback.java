package com.sdex.webteb.rest;

import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by Yuriy Mysochenko on 09.02.2015.
 */
public interface CancellableRestCallback<T> extends Callback<T> {

    public void failure(RestError restError);
    public void cancel();
    public boolean isCancelled();

}
