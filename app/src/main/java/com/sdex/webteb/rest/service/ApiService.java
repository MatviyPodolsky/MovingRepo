package com.sdex.webteb.rest.service;

import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.request.BabyGeneralRequest;
import com.sdex.webteb.rest.request.BabyGotbirthRequest;
import com.sdex.webteb.rest.request.BabyProfileRequest;
import com.sdex.webteb.rest.request.BabyReminderRequest;
import com.sdex.webteb.rest.request.BabyTestDoneRequest;
import com.sdex.webteb.rest.request.ChangePasswordRequest;
import com.sdex.webteb.rest.request.FacebookLoginRequest;
import com.sdex.webteb.rest.request.NotificationRequest;
import com.sdex.webteb.rest.request.PushTokenRequest;
import com.sdex.webteb.rest.request.RegisterAccountRequest;
import com.sdex.webteb.rest.request.RegisterUserRequest;
import com.sdex.webteb.rest.request.SetPasswordRequest;
import com.sdex.webteb.rest.response.ArticlesResponse;
import com.sdex.webteb.rest.response.BabyGeneralResponse;
import com.sdex.webteb.rest.response.BabyHomeResponse;
import com.sdex.webteb.rest.response.BabyProfileResponse;
import com.sdex.webteb.rest.response.BabyTestResponse;
import com.sdex.webteb.rest.response.CityResponse;
import com.sdex.webteb.rest.response.EntityResponse;
import com.sdex.webteb.rest.response.MonthResponse;
import com.sdex.webteb.rest.response.NotificationsResponse;
import com.sdex.webteb.rest.response.PromotedAppsResponse;
import com.sdex.webteb.rest.response.SearchDoctorResponse;
import com.sdex.webteb.rest.response.SpecialtiesResponse;
import com.sdex.webteb.rest.response.UnreadEntitiesResponse;
import com.sdex.webteb.rest.response.UserInfoResponse;
import com.sdex.webteb.rest.response.UserLoginResponse;
import com.sdex.webteb.rest.response.UserRetrieveResponse;
import com.sdex.webteb.rest.response.WeekResponse;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

/**
 * Created by Yuriy Mysochenko on 09.02.2015.
 */
public interface ApiService {

    @POST("/Token")
    @FormUrlEncoded
    public void login(@Field("grant_type") String grantType, @Field("username") String username,
                      @Field("password") String password, Callback<UserLoginResponse> callback);

    @POST("/Account/FacebookLogin")
    public void facebookLogin(@Body FacebookLoginRequest body, Callback<UserLoginResponse> callback);

    @POST("/Account/Register")
    public void register(@Body RegisterAccountRequest body, Callback<String> callback);

    @POST("/Account/ForgotPassword")
    public void restorePassword(@Query("email") String email, Callback<String> callback);

    @POST("/Account/ChangePassword")
    public void changePassword(@Body ChangePasswordRequest body, Callback<String> callback);

    @POST("/Account/SetPassword")
    public void setPassword(@Body SetPasswordRequest body, Callback<String> callback);

    @POST("/Account/Logout")
    public void logout(Callback<String> callback);

    @POST("/baby/settings/profile")
    public void setBabyProfile(@Body BabyProfileRequest body, Callback<String> callback);

    @GET("/baby/settings/profile")
    public void getBabyProfile(Callback<BabyProfileResponse> callback);

    @DELETE("/baby/settings")
    public void deleteSettings(Callback<String> callback);

    @GET("/Account/UserInfo")
    public void getUserInfo(Callback<UserInfoResponse> callback);

    @GET("/baby/home")
    public void getBabyHome(Callback<BabyHomeResponse> callback);

    @POST("/baby/settings/general")
    public void setBabyGeneral(@Body BabyGeneralRequest body, Callback<String> callback);

    @POST("/baby/settings/general")
    public String setBabyGeneral(@Body BabyGeneralRequest body);

    @GET("/baby/settings/general")
    public void getBabyGeneral(Callback<BabyGeneralResponse> callback);

    @POST("/baby/settings/gotbirth")
    public void setBabyGotbirth(@Body BabyGotbirthRequest body, Callback<String> callback);

    @POST("/baby/tests/Reminder")
    public void setBabyReminder(@Body BabyReminderRequest body, Callback<String> callback);

    @DELETEWITHBODY("/baby/tests/Reminder")
    public void deleteBabyReminder(@Body BabyReminderRequest body, Callback<String> callback);

    @POST("/baby/tests/Done")
    public void makeTestDone(@Body BabyTestDoneRequest body, Callback<String> callback);

    @DELETEWITHBODY("/baby/tests/Done")
    public void makeTestUndone(@Body BabyTestDoneRequest body, Callback<String> callback);

    @GET("/baby/tests")
    public void getBabyTests(Callback<List<BabyTestResponse>> callback);

    @POST("/Users/Register")
    public void makeTestDone(@Body RegisterUserRequest body, Callback<String> callback);

    @GET("/Users/Retrieve?userId={userId}")
    public void retrieveUser(@Path("userId") String userId, Callback<UserRetrieveResponse> callback);

    @GET("/Users/UnreadEntities?userId={userId}")
    public void getUnreadEntities(@Path("userId") String userId,
                                  Callback<List<UnreadEntitiesResponse>> callback);

    @GET("/Users/GetUrl?EntityType={EntityType}&EntityID={EntityID}")
    public void getUnreadEntities(@Path("EntityType") String entityType,
                                  @Path("EntityID") String entityID, Callback<String> callback);

    @GET("/GetPromotedApps")
    public void getPromotedApps(Callback<PromotedAppsResponse> callback);

    @GET("/lookups/specialties/baby")
    public void getSpecialties(Callback<List<SpecialtiesResponse>> callback);

    @GET("/lookups/cities/bycountry")
    public void getCities(@Query("isoCode") String isoCode, Callback<List<CityResponse>> callback);

    @GET("/SearchDoctors")
    public void searchDoctor(@QueryMap Map<String, String> options,
                             RestCallback<SearchDoctorResponse> callback);

    @GET("/baby/articles/latest")
    public void getArticles(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize,
                            Callback<ArticlesResponse> callback);

    @GET("/baby/week")
    public void getWeek(@Query("weekNumber") int weekNumber, Callback<WeekResponse> callback);

    @GET("/baby/month")
    public void getMonth(@Query("ageInMonths") int ageInMonths, Callback<MonthResponse> callback);

    @GET("/GetEntity")
    public void getEntity(@Query("ID") int id, @Query("Type") String type,
                         @Query("FieldName") String fieldName, Callback<EntityResponse> callback);

    @GET("/baby/notifications")
    public void getNotifications(@Query("ignoreSettings") boolean ignoreSettings,
                                 Callback<NotificationsResponse> callback);

    @POST("/baby/notifications/tapped")
    public void postNotification(@Body NotificationRequest body, Callback<String> callback);

    @POST("/baby/settings/pushtoken")
    public void postPushToken(@Body PushTokenRequest body, Callback<String> callback);

}
