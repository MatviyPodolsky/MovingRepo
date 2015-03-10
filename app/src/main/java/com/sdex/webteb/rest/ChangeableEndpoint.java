package com.sdex.webteb.rest;

import retrofit.Endpoint;

/**
 * Created by Yuriy Mysochenko on 10.03.2015.
 */
public class ChangeableEndpoint implements Endpoint {

    private String url;

    public ChangeableEndpoint(String url) {
        this.url = url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getName() {
        return "default";
    }

}
