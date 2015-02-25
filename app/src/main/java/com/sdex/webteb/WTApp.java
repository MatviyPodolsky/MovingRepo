package com.sdex.webteb;

import android.app.Application;

import com.sdex.webteb.utils.PreferencesManager;

/**
 * Created by Yuriy Mysochenko on 03.02.2015.
 */
public class WTApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferencesManager.initializeInstance(this);

//        PreferencesManager.getInstance().setTokenData("RnpGANN0F2LOVnn_UkLqcFpTHbKwsJEZcG6MotBdc6_LUW6x6CXY6w9A6n9wllv0_uSHgc8ZrbxTh4NkZIummLQtGKpZDTUP4vGw1iNIg7YE5MFY0SxbK0cBCro8AN81_PA-y0zjwcDJu4z_rKwh1dJDaGCyRKP61YUsotrJIwBYMmomslmBp2IEey3IxY31sllj36azymYIAv3JwuyBm9pZtAZcQZH8IhzxMiapEliIDlqqr0s9d4hD7SxZztpU2JAhurF_brmQ_wBHMRnjy7Hx8slKLvAw86vu68cUaq087nwCnoEAJQoc_Qb95RaLJlUwSPiGVkCzTpFdMizjxa7O_Ucsa8aWMa2ckbuzMOegEwck9FQqd1MLO0_HC4R58Tta23DMvpF3ZW6Jpx8sUtMIv72f6H9emHa2S5W7Tp7KQ919IiHdEu2er4TfAzfbgRBywcE0HJc0M_W2JrqbHiiB1o8", "Bearer");
    }

}
