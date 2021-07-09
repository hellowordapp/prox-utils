package com.proxglobal;

import android.app.Activity;
import android.content.Context;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.proxglobal.proxads.ads.ProxInterstitialAd;
import com.proxglobal.proxads.ads.ProxNativeAd;
import com.proxglobal.proxads.remote_config.ProxRemoteConfig;
import com.proxglobal.rate.ProxRateDialog;

public class ProxUtils {
    private ProxUtils() {
    }
    public static ProxUtils INSTANCE = new ProxUtils();

    public static String TAG = "ProxUtils";

    public static final String TEST_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712";
    public static final String TEST_NATIVE_ID = "ca-app-pub-3940256099942544/2247696110";

    public ProxInterstitialAd createInterstitialAd (Activity activity, String adId) {
        return new ProxInterstitialAd(activity, adId);
    }

    public ProxNativeAd createNativeAd (Activity activity, String adId, FrameLayout adContainer, int layoutAdId) {
        return new ProxNativeAd(activity, adId, adContainer, layoutAdId);
    }

    public void initFirebaseRemoteConfig(AppCompatActivity activity, int appVersionCode, int iconAppId) {
        new ProxRemoteConfig(iconAppId).showRemoteConfigIfNecessary(activity, appVersionCode);
    }
}
