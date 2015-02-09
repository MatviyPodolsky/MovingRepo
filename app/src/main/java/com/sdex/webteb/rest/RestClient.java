package com.sdex.webteb.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdex.webteb.rest.service.ApiService;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by Yuriy Mysochenko on 09.02.2015.
 */
public class RestClient {

    private static final String API_URL = "http://api.qwebteb.com";

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
            .create();

    private static final RestAdapter REST_ADAPTER = new RestAdapter.Builder()
            .setEndpoint(API_URL)
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setConverter(new GsonConverter(gson))
            .build();

    private static final ApiService apiService = REST_ADAPTER.create(ApiService.class);

    public static ApiService getApiService() {
        return apiService;
    }

}
