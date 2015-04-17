package com.sdex.webteb.utils.cache;

public interface GetCallback<T> {
    public void onSuccess(T object);

    public void onFailure(Exception e);
}
