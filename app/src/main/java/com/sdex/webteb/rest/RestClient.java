package com.sdex.webteb.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdex.webteb.rest.service.ApiService;
import com.sdex.webteb.utils.PreferencesManager;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by Yuriy Mysochenko on 09.02.2015.
 */
public class RestClient {

    public static final String SERVER_URL = "http://api.qwebteb.com";

    private static final String API_URL = PreferencesManager.getInstance()
            .getPreferences().getString("server", SERVER_URL);

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
            .create();

    private static final OkHttpClient okHttpClient = new OkHttpClient();

    public static final ChangeableEndpoint ENDPOINT = new ChangeableEndpoint(API_URL);

    private static final RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(ENDPOINT)
            .setClient(new OkClient(okHttpClient))
            .setRequestInterceptor(new SessionRequestInterceptor())
            .setLogLevel(RestAdapter.LogLevel.NONE)
            .setConverter(new GsonConverter(gson))
            .build();

    private static final ApiService apiService = restAdapter.create(ApiService.class);

//    static {
//        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
//        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
//        okHttpClient.setWriteTimeout(60, TimeUnit.SECONDS);
//    }

    private RestClient() {
    }

    public static ApiService getApiService() {
        return apiService;
    }

}
