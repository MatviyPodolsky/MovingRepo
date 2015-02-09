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
//            request.addHeader("Authorization", "Bearer " + "BshnJs-XJG5pQIXIxyolSyiUojSZBOqP8K1oQot0U9E0W3Ehq0yZovg7rzpyUpFLCnVNYJeHFCPwnl8u1uhYQfQ8LyrwWMC62gm_AouRc3c7ornrBIcar9loZ3o9-SO33MxbfCn2oRYcUnQsRLh10u9Owq_8ZUYTkXX9W6QJfemBU1j-mX7F3EeT4k8W5YZbeaQeF1X0ehc7QWmnA0pgFFgJADVn_Hy2ifU4912siu1XVnKSxRnO8pqUaZHC_Kws-W0WD1N5aQmMV-dgioinAqrlyRx6H9Qv4iil1jJ1asHBE4yiSInkgllUSvSgAKgPXbmqCU14esHnCv_W-1ZkTb-nuARc8Eyzydviu_X6zb2KQOCet3AyeijDTWaXTg8Mlv1ijXVZMMG4MVke3lLX5zhpLt9FIjDWVq6uyk6wTPFU_eaJkUL_0RsHNM6b19TMuHvOk7NL_p-Vshthp8CY28vWrrZVftdsoffFtusHD6nGy8Wo");
        //}
    }
}
