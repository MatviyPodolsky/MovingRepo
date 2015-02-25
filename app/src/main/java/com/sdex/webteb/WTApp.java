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

        PreferencesManager.getInstance().setTokenData("CRMZeyj5MRS3Tjtqj1To5RvWk3edDMdq7TetjJzng" +
                "KAkjjeEN97EgCpwSJrL8PJy2gRsHZ6_65i57kQ3fKvKj-Q0chvrBKyNEVAXxv9HDWGMxXIfONHHLAv8" +
                "jxmHmFdGdFrUZnI53kxBcv8zrL2DDiL4AWH00_QTkFFYbL_VyYDTQ7rbT-0fbSnxz7K8_JJvbJ9AbcA" +
                "4dXULZoGQkMP6oUygLs-Ob-pnfckXDJxpedomVBEDmbrcQ1lV_7ADQiDTjGJje1pd2ThDJOTfaMUuZ4" +
                "sVi0s0xHJU1AqBDdC_uxbHOKYOhwPN6ksjw5eROR05SBognSl9nrLRHzmXqmkfuDqsUCeNRVrIVzPMw" +
                "6WgtVL4bryV6hoGAqBSuaDnzGsCEI0FeJoisHxhBIoNKd7WNskAfTdql7pHmpGHMhesDXv_WBLqZVpj" +
                "7v8UrvVnNC9Upur-WxcEwzRt0zavQF9hO4c9Z8k", "Bearer");
    }

}
