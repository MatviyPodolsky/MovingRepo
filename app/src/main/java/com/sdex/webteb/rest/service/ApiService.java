package com.sdex.webteb.rest.service;

import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.request.BabyGeneralRequest;
import com.sdex.webteb.rest.request.BabyGotbirthRequest;
import com.sdex.webteb.rest.request.BabyProfileRequest;
import com.sdex.webteb.rest.request.BabyReminderRequest;
import com.sdex.webteb.rest.request.BabyTestDoneRequest;
import com.sdex.webteb.rest.request.ChangePasswordRequest;
import com.sdex.webteb.rest.request.ContactUsRequest;
import com.sdex.webteb.rest.request.FacebookLoginRequest;
import com.sdex.webteb.rest.request.NotificationReceivedRequest;
import com.sdex.webteb.rest.request.NotificationTappedRequest;
import com.sdex.webteb.rest.request.PushTokenRequest;
import com.sdex.webteb.rest.request.RegisterAccountRequest;
import com.sdex.webteb.rest.request.RegisterUserRequest;
import com.sdex.webteb.rest.request.SendEventRequest;
import com.sdex.webteb.rest.request.SetPasswordRequest;
import com.sdex.webteb.rest.response.ArticlesResponse;
import com.sdex.webteb.rest.response.BabyConfigResponse;
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
    void login(@Field("grant_type") String grantType, @Field("username") String username,
                      @Field("password") String password, Callback<UserLoginResponse> callback);

    @POST("/Account/FacebookLogin")
    void facebookLogin(@Body FacebookLoginRequest body, Callback<UserLoginResponse> callback);

    @POST("/Account/Register")
    void register(@Body RegisterAccountRequest body, Callback<String> callback);

    @POST("/Account/ForgotPassword")
    void restorePassword(@Query("email") String email, Callback<String> callback);

    @POST("/Account/ChangePassword")
    void changePassword(@Body ChangePasswordRequest body, Callback<String> callback);

    @POST("/Account/SetPassword")
    void setPassword(@Body SetPasswordRequest body, Callback<String> callback);

    @POST("/Account/Logout")
    void logout(Callback<String> callback);

    @POST("/baby/settings/profile")
    void setBabyProfile(@Body BabyProfileRequest body, Callback<String> callback);

    @GET("/baby/settings/profile")
    void getBabyProfile(Callback<BabyProfileResponse> callback);

    @DELETE("/baby/settings")
    void deleteSettings(Callback<String> callback);

    @GET("/Account/UserInfo")
    void getUserInfo(Callback<UserInfoResponse> callback);

    @GET("/baby/home")
    void getBabyHome(Callback<BabyHomeResponse> callback);

    @POST("/baby/settings/general")
    void setBabyGeneral(@Body BabyGeneralRequest body, Callback<String> callback);

    @POST("/baby/settings/general")
    String setBabyGeneral(@Body BabyGeneralRequest body);

    @GET("/baby/settings/general")
    void getBabyGeneral(Callback<BabyGeneralResponse> callback);

    @POST("/baby/settings/gotbirth")
    void setBabyGotbirth(@Body BabyGotbirthRequest body, Callback<String> callback);

    @POST("/baby/tests/Reminder")
    void setBabyReminder(@Body BabyReminderRequest body, Callback<String> callback);

    @DELETEWITHBODY("/baby/tests/Reminder")
    void deleteBabyReminder(@Body BabyReminderRequest body, Callback<String> callback);

    @POST("/baby/tests/Done")
    void makeTestDone(@Body BabyTestDoneRequest body, Callback<String> callback);

    @DELETEWITHBODY("/baby/tests/Done")
    void makeTestUndone(@Body BabyTestDoneRequest body, Callback<String> callback);

    @GET("/baby/tests")
    void getBabyTests(Callback<List<BabyTestResponse>> callback);

    @POST("/Users/Register")
    void makeTestDone(@Body RegisterUserRequest body, Callback<String> callback);

    @GET("/Users/Retrieve?userId={userId}")
    void retrieveUser(@Path("userId") String userId, Callback<UserRetrieveResponse> callback);

    @GET("/Users/UnreadEntities?userId={userId}")
    void getUnreadEntities(@Path("userId") String userId,
                                  Callback<List<UnreadEntitiesResponse>> callback);

    @GET("/Users/GetUrl?EntityType={EntityType}&EntityID={EntityID}")
    void getUnreadEntities(@Path("EntityType") String entityType,
                                  @Path("EntityID") String entityID, Callback<String> callback);

    @GET("/GetPromotedApps")
    void getPromotedApps(Callback<PromotedAppsResponse> callback);

    @GET("/lookups/specialties/baby")
    void getSpecialties(Callback<List<SpecialtiesResponse>> callback);

    @GET("/lookups/cities/bycountry")
    void getCities(@Query("isoCode") String isoCode, Callback<List<CityResponse>> callback);

    @GET("/SearchDoctors")
    void searchDoctor(@QueryMap Map<String, String> options,
                             RestCallback<SearchDoctorResponse> callback);

    @GET("/baby/articles/latest")
    void getArticles(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize,
                            Callback<ArticlesResponse> callback);

    @GET("/baby/week")
    void getWeek(@Query("weekNumber") int weekNumber, Callback<WeekResponse> callback);

    @GET("/baby/tests/week")
    void getWeekTests(@Query("weekNumber") int weekNumber, Callback<List<BabyTestResponse>> callback);

    @GET("/baby/month")
    void getMonth(@Query("ageInMonths") int ageInMonths, Callback<MonthResponse> callback);

    @GET("/baby/tests/month")
    void getMonthTests(@Query("ageInMonths") int ageInMonths, Callback<List<BabyTestResponse>> callback);

    @GET("/GetEntity")
    void getEntity(@Query("ID") int id, @Query("Type") String type,
                          @Query("FieldName") String fieldName, Callback<EntityResponse> callback);

    @POST("/ContactUs")
    void contactUs(@Body ContactUsRequest body, Callback<String> callback);

    @POST("/SendEvent")
    void sendEvent(@Body SendEventRequest body, Callback<String> callback);

    @GET("/baby/notifications")
    void getNotifications(@Query("ignoreSettings") boolean ignoreSettings,
                                 Callback<NotificationsResponse> callback);

    @POST("/baby/notifications/tapped")
    void postNotificationTapped(@Body NotificationTappedRequest body, Callback<String> callback);

    @POST("/baby/notifications/received")
    String postNotificationReceived(@Body NotificationReceivedRequest body);

    @POST("/baby/settings/pushtoken")
    void postPushToken(@Body PushTokenRequest body, Callback<String> callback);

    @GET("/baby/config")
    void getBabyConfig(Callback<BabyConfigResponse> callback);

}
