package com.sdex.webteb.rest.service;

import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.UserLoginResponse;
import com.sdex.webteb.rest.request.RegisterUserRequest;

import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Yuriy Mysochenko on 09.02.2015.
 */
public interface ApiService {

    @POST("/Token")
    @FormUrlEncoded
    public void login(@Field("grant_type") String grantType, @Field("username") String username,
                      @Field("password") String password, RestCallback<UserLoginResponse> callback);

    @POST("/Account/Register")
    public void register(@Body RegisterUserRequest body, RestCallback<String> callback);

}
