package com.sdex.webteb.rest;

import retrofit.RequestInterceptor;

/**
 * Created by Yuriy Mysochenko on 09.02.2015.
 */
public class SessionRequestInterceptor implements RequestInterceptor {
    @Override
    public void intercept(RequestFacade request) {
        //if (user is connected){
            request.addHeader("Authorization", "token_type access_token");
        //}
    }
}
