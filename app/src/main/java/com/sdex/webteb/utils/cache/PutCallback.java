package com.sdex.webteb.utils.cache;

public interface PutCallback {
    public void onSuccess();

    public void onFailure(Exception e);
}
