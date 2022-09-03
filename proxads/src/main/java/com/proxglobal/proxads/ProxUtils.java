package com.proxglobal.proxads;

import android.app.Activity;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.proxglobal.proxads.ads.ProxInterstitialAd;
import com.proxglobal.proxads.ads.ProxNativeAd;
import com.proxglobal.proxads.remote_config.ProxRemoteConfig;

public class ProxUtils {
    private ProxUtils() {
    }
    public static ProxUtils INSTANCE = new ProxUtils();

    public static String TAG = "ProxUtils";

    public static final String TEST_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712";
    public static final String TEST_NATIVE_ID = "ca-app-pub-3940256099942544/2247696110";
    public static final String TEST_BANNER_ID = "ca-app-pub-3940256099942544/6300978111";
    public static final String TEST_REWARD_ID = "ca-app-pub-3940256099942544/5224354917";

    public void initFirebaseRemoteConfig(AppCompatActivity activity, int appVersionCode, boolean isDebug,
                                         int iconAppId, String appName) {
        new ProxRemoteConfig(iconAppId, appName).showRemoteConfigIfNecessary(activity, appVersionCode, isDebug);
    }
}
