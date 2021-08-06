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

    public ProxInterstitialAd createInterstitialAd (Activity activity, String adId) {
        return new ProxInterstitialAd(activity, adId);
    }

    public ProxNativeAd createNativeAd (Activity activity, String adId, FrameLayout adContainer, int layoutAdId) {
        return new ProxNativeAd(activity, adId, adContainer, layoutAdId);
    }

    public ProxNativeAd createMediumNativeAdWithShimmer(Activity activity, String adId, FrameLayout adContainer) {
        ProxNativeAd nativeAd = new ProxNativeAd(activity, adId, adContainer, R.layout.ads_native_medium);
        nativeAd.enableShimmer(R.layout.shimmer_native_medium);
        return nativeAd;
    }

    public ProxNativeAd createBigNativeAdWithShimmer(Activity activity, String adId, FrameLayout adContainer) {
        ProxNativeAd nativeAd = new ProxNativeAd(activity, adId, adContainer, R.layout.ads_native_big);
        nativeAd.enableShimmer(R.layout.shimmer_native_big);
        return nativeAd;
    }

    public void initFirebaseRemoteConfig(AppCompatActivity activity, int appVersionCode,
                                         int iconAppId, String appName) {
        new ProxRemoteConfig(iconAppId, appName).showRemoteConfigIfNecessary(activity, appVersionCode);
    }
}