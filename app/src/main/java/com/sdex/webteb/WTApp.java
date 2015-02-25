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

//        PreferencesManager.getInstance().setTokenData("HfXra0ukOClt-et8lqZL-reSmnPIxFrkE4dB6w-8bmDvy679qG-boN2_WOGlAk5px3zfSs8pUQwwb-IhxqKCj0Brbw1Irvn_KFCy9kImwfTDUSE3uvLVu91mXsWh721Jr8CCboGP9uDCMQo1vYFuEg9ZlM_0CTqCSwHqCSXATK2AKexsNFYkN3fGRVU1NyJt7VvJIJ2QDAhMzSAY1LqHp2MiEagkYTPi5CVrFJ5ZIgl6l6zI71NwEvZlFqMhTMcg24sUGyfo6gTIoMkkl8qY0xJ6Yj-tB3SDY2Np5lsXbAz8SOJITFITgD5LqkP3m4IuIGiBPO5Jc1pdzGrFmFT4l_rrhc4XRL_mAQTMyHhxGcAyoWgEpGmWRJioOIe9s309wp3YoHsfBV_ZU__n5dwToJgacMTIeZxqKY7UaLfDi4cKpI-WseoNvu_gVCyPmcWaJecZ_EFaWV1MLnuE_fzRXcf4ToQ", "Bearer");
    }

}
