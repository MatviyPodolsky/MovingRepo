package com.sdex.webteb.mock;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.sdex.webteb.R;
import com.sdex.webteb.rest.response.BabyHomeResponse;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Yuriy Mysochenko on 25.02.2015.
 */
public class MockData {

    public static BabyHomeResponse getMockHome(Context context) {
        Resources resources = context.getResources();
        InputStream is = resources.openRawResource(R.raw.home_mock);
        Gson gson = new Gson();
        return gson.fromJson(new JsonReader(new InputStreamReader(is)), BabyHomeResponse.class);
    }

}
