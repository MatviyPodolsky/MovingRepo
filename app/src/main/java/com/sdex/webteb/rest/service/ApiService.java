package com.sdex.webteb.rest.service;

import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.request.BabyGeneralRequest;
import com.sdex.webteb.rest.request.BabyGotbirthRequest;
import com.sdex.webteb.rest.request.BabyProfileRequest;
import com.sdex.webteb.rest.request.BabyReminderRequest;
import com.sdex.webteb.rest.request.BabyTestDoneRequest;
import com.sdex.webteb.rest.request.ChangePasswordRequest;
import com.sdex.webteb.rest.request.RegisterAccountRequest;
import com.sdex.webteb.rest.request.RegisterUserRequest;
import com.sdex.webteb.rest.request.RestorePasswordRequest;
import com.sdex.webteb.rest.request.SetPasswordRequest;
import com.sdex.webteb.rest.response.BabyGeneralResponse;
import com.sdex.webteb.rest.response.BabyHomeResponse;
import com.sdex.webteb.rest.response.BabyLookupResponse;
import com.sdex.webteb.rest.response.BabyProfileResponse;
import com.sdex.webteb.rest.response.BabyTestResponse;
import com.sdex.webteb.rest.response.PromotedAppsResponse;
import com.sdex.webteb.rest.response.UnreadEntitiesResponse;
import com.sdex.webteb.rest.response.UserInfoResponse;
import com.sdex.webteb.rest.response.UserLoginResponse;
import com.sdex.webteb.rest.response.UserRetrieveResponse;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Yuriy Mysochenko on 09.02.2015.
 */
public interface ApiService {

    @POST("/Token")
    @FormUrlEncoded
    public void login(@Field("grant_type") String grantType, @Field("username") String username,
                      @Field("password") String password, RestCallback<UserLoginResponse> callback);

    @POST("/Account/Register")
    public void register(@Body RegisterAccountRequest body, RestCallback<String> callback);

    @POST("/Account/RegisterExternal")
    public void restorePassword(@Body RestorePasswordRequest body, RestCallback<String> callback);

    @POST("/Account/ChangePassword")
    public void changePassword(@Body ChangePasswordRequest body, RestCallback<String> callback);

    @POST("/Account/SetPassword")
    public void setPassword(@Body SetPasswordRequest body, RestCallback<String> callback);

    @POST("/Account/Logout")
    public void logout(RestCallback<String> callback);

    @POST("/baby/settings/profile")
    public void setBabyProfile(@Body BabyProfileRequest body, RestCallback<String> callback);

    @GET("/baby/settings/profile")
    public void getBabyProfile(RestCallback<BabyProfileResponse> callback);

    @DELETE("/baby/settings")
    public void deleteSettings(RestCallback<String> callback);

    @GET("/Account/UserInfo")
    public void getUserInfo(RestCallback<UserInfoResponse> callback);

    @GET("/baby/home")
    public void getBabyHome(RestCallback<BabyHomeResponse> callback);

    @POST("/baby/settings/general")
    public void setBabyGeneral(@Body BabyGeneralRequest body, RestCallback<String> callback);

    @GET("/baby/settings/general")
    public void getBabyGeneral(RestCallback<BabyGeneralResponse> callback);

    @POST("/baby/settings/gotbirth")
    public void setBabyGotbirth(@Body BabyGotbirthRequest body, RestCallback<String> callback);

    @POST("/baby/tests/Reminder")
    public void setBabyReminder(@Body BabyReminderRequest body, RestCallback<String> callback);

    @DELETEWITHBODY("/baby/tests/Reminder")
    public void deleteBabyReminder(@Body BabyReminderRequest body, RestCallback<String> callback);

    @POST("/baby/tests/Done")
    public void makeTestDone(@Body BabyTestDoneRequest body, RestCallback<String> callback);

    @DELETEWITHBODY("/baby/tests/Done")
    public void makeTestUndone(@Body BabyTestDoneRequest body, RestCallback<String> callback);

    @GET("/baby/tests")
    public void getBabyTests(RestCallback<List<BabyTestResponse>> callback);

    @POST("/Users/Register")
    public void makeTestDone(@Body RegisterUserRequest body, RestCallback<String> callback);

    @GET("/Users/Retrieve?userId={userId}")
    public void retrieveUser(@Path("userId") String userId, RestCallback<UserRetrieveResponse> callback);

    @GET("/Users/UnreadEntities?userId={userId}")
    public void getUnreadEntities(@Path("userId") String userId, RestCallback<List<UnreadEntitiesResponse>> callback);

    @GET("/Users/GetUrl?EntityType={EntityType}&EntityID={EntityID}")
    public void getUnreadEntities(@Path("EntityType") String entityType, @Path("EntityID") String entityID, RestCallback<String> callback);

    @GET("/GetPromotedApps")
    public void getPromotedApps(RestCallback<PromotedAppsResponse> callback);

    @GET("/lookups/specialties/baby")
    public void getBabyLookups(RestCallback<List<BabyLookupResponse>> callback);

}
