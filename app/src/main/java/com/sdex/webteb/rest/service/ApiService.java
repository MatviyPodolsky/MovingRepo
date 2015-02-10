package com.sdex.webteb.rest.service;

import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.request.ChangePasswordRequest;
import com.sdex.webteb.rest.request.RegisterUserRequest;
import com.sdex.webteb.rest.request.RestorePasswordRequest;
import com.sdex.webteb.rest.request.SetBabyProfileRequest;
import com.sdex.webteb.rest.request.SetPasswordRequest;
import com.sdex.webteb.rest.response.BabyHomeResponse;
import com.sdex.webteb.rest.response.BabyProfileResponse;
import com.sdex.webteb.rest.response.UserInfoResponse;
import com.sdex.webteb.rest.response.UserLoginResponse;

import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
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

    @POST("/Account/RegisterExternal")
    public void restorePassword(@Body RestorePasswordRequest body, RestCallback<String> callback);

    @POST("/Account/ChangePassword")
    public void changePassword(@Body ChangePasswordRequest body, RestCallback<String> callback);

    @POST("/Account/SetPassword")
    public void setPassword(@Body SetPasswordRequest body, RestCallback<String> callback);

    @POST("/Account/Logout")
    public void logout(RestCallback<String> callback);

    @POST("/baby/settings/profile")
    public void setBabyProfile(@Body SetBabyProfileRequest body, RestCallback<String> callback);

    @GET("/Account/UserInfo")
    public void getUserInfo(RestCallback<UserInfoResponse> callback);

    @GET("/baby/home")
    public void getBabyHome(RestCallback<BabyHomeResponse> callback);

    @GET("/baby/settings/profile")
    public void getBabyProfile(RestCallback<BabyProfileResponse> callback);

}
