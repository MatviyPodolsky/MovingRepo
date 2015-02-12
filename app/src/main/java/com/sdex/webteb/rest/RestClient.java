package com.sdex.webteb.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdex.webteb.rest.service.ApiService;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by Yuriy Mysochenko on 09.02.2015.
 */
public class RestClient {

    private static final String API_URL = "http://api.qwebteb.com";

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
            .create();

    private static final OkHttpClient okHttpClient = new OkHttpClient();

    private static final RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(API_URL)
            .setClient(new OkClient(okHttpClient))
            .setRequestInterceptor(new SessionRequestInterceptor())
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setConverter(new GsonConverter(gson))
            .build();

    private static final ApiService apiService = restAdapter.create(ApiService.class);

    private RestClient() {
    }

    public static ApiService getApiService() {
        return apiService;
    }

}