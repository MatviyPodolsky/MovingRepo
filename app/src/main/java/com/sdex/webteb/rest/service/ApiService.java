package com.sdex.webteb.rest.service;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Yuriy Mysochenko on 09.02.2015.
 */
public interface ApiService {

    @GET("/Login")
    public void login(@Query("username") String username,@Query("password") String password, Callback<String> callback);

    @GET("/Register")
    public void register(@Query("username") String username,@Query("password") String password, Callback<String> callback);

}
