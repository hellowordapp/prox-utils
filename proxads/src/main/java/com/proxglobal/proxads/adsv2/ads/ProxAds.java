package com.proxglobal.proxads.adsv2.ads;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.applovin.sdk.AppLovinMediationProvider;
import com.applovin.sdk.AppLovinSdk;
import com.facebook.ads.AdSettings;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.proxglobal.proxads.R;
import com.proxglobal.proxads.adsv2.adgoogle.GoogleBannerAds;
import com.proxglobal.proxads.adsv2.adgoogle.GoogleInterstitialAd;
import com.proxglobal.proxads.adsv2.adgoogle.GoogleNativeAds;
import com.proxglobal.proxads.adsv2.adgoogle.GoogleRewardAds;
import com.proxglobal.proxads.adsv2.admax.MaxBannerAds;
import com.proxglobal.proxads.adsv2.admax.MaxInterstitialAds;
import com.proxglobal.proxads.adsv2.admax.MaxNativeAds;
import com.proxglobal.proxads.adsv2.callback.AdsCallback;
import com.proxglobal.proxads.adsv2.callback.LoadCallback;
import com.proxglobal.proxads.adsv2.callback.RewardCallback;
import com.proxglobal.purchase.ProxPurchase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

public class ProxAds {
    private final HashMap<String, BaseAds> adsStorage;
    private static ProxAds INSTANCE = null;

    private ProxAds() {
        adsStorage = new HashMap<>();
    }

    private boolean splashDone = false;

    public static ProxAds getInstance() {
        if (INSTANCE == null) {
            synchronized (ProxAds.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ProxAds();
                }
            }
        }

        return INSTANCE;
    }

    /**
     * load inter ads available
     *
     * @param activity
     * @param googleAdsId
     * @param tag         tag name of ads
     */
    public void initInterstitial(@NonNull Activity activity, @NonNull String googleAdsId, @NonNull String tag) {
        if (ProxPurchase.getInstance().checkPurchased()) return;

        InterAds ads = new GoogleInterstitialAd(activity, googleAdsId);
        adsStorage.put(tag, ads);
        ads.load();
    }

    public void initInterstitialMax(@NonNull Activity activity, @NonNull String adsId, @NonNull String tag) {
        if (ProxPurchase.getInstance().checkPurchased()) return;

        InterAds ads = new MaxInterstitialAds(activity, adsId);
        adsStorage.put(tag, ads);
        ads.load();
    }

    /**
     * show showInterstitial with existed tag name
     *
     * @param activity
     * @param tag      tag name of ads
     * @param callback
     */
    public void showInterstitial(@NonNull Activity activity, @NonNull String tag, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased()) {
            callback.onError();
            return;
        }

        BaseAds ads = adsStorage.get(tag);

        if (ads == null) {
            callback.onError();
            return;
        }
        if (!ads.isAvailable()) {
            ads.show(activity, callback);
            return;
        }

        showAdsWithKHub(activity, ads, callback);
    }

    public void showInterstitialMax(@NonNull Activity activity, @NonNull String tag, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased()) {
            callback.onError();
            return;
        }

        BaseAds ads = adsStorage.get(tag);

        if (ads == null) {
            callback.onError();
            return;
        }
        if (!ads.isAvailable()) {
            ads.show(activity, callback);
            return;
        }

        if (ads.inLoading()) {
            callback.onError();
            return;
        }

        showAdsWithKHub(activity, ads, callback);
    }

    InterAds splashAds;

    /**
     * show splash ads
     *
     * @param activity
     * @param callback
     * @param googleAdsId
     * @param timeout     timeout
     */
    public void showSplash(@NonNull Activity activity, @NonNull AdsCallback callback,
                           @NonNull String googleAdsId, int timeout) {
        if (ProxPurchase.getInstance().checkPurchased()) {
            callback.onError();
            return;
        }

        splashDone = false;

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!splashDone) {
                    callback.onError();
                    splashDone = true;
                }
            }
        };
        handler.postDelayed(runnable, timeout);

        splashAds = new GoogleInterstitialAd(activity, googleAdsId);

        splashAds.setLoadCallback(new LoadCallback() {
            @Override
            public void onLoadSuccess() {
                if (splashDone) return;
                splashAds.turnOffAutoReload();
                showAdsWithKHub(activity, splashAds, callback);
                handler.removeCallbacksAndMessages(null);

                splashAds = null;
            }

            @Override
            public void onLoadFailed() {

            }
        }).load();
    }

    public void showSplashMax(@NonNull Activity activity, @NonNull AdsCallback callback,
                              @NonNull String adsId, int timeout) {
        if (ProxPurchase.getInstance().checkPurchased()) {
            callback.onError();
            return;
        }

        splashDone = false;

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!splashDone) {
                    callback.onError();
                    splashDone = true;
                }
            }
        };
        handler.postDelayed(runnable, timeout);

        splashAds = new MaxInterstitialAds(activity, adsId);

        splashAds.setLoadCallback(new LoadCallback() {
            @Override
            public void onLoadSuccess() {
                if (splashDone) return;
                splashAds.turnOffAutoReload();
                showAdsWithKHub(activity, splashAds, callback);
                handler.removeCallbacksAndMessages(null);

                splashAds = null;
            }

            @Override
            public void onLoadFailed() {
            }
        }).load();
    }

    private void clearStorage() {
        adsStorage.clear();
    }

    private void showAdsWithKHub(Activity activity, BaseAds ads, AdsCallback callback) {
        KProgressHUD khub = createKHub(activity);

        khub.show();
        new Handler().postDelayed(() -> {
            ads.show(activity, callback);
            khub.dismiss();
        }, 700);
    }

    public static KProgressHUD createKHub(Activity activity) {
        return KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(activity.getString(R.string._loading_ads))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setAutoDismiss(true)
                .setDimAmount(0.5f);
    }

    // ----------------------- Banner -----------------------
    public void showBanner(Activity activity, FrameLayout container, String adId, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased()) {
            callback.onError();
            return;
        }

        new GoogleBannerAds(activity, container, adId).load().show(activity, callback);
    }

    public void showBannerMax(Activity activity, FrameLayout container, String adId, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased()) {
            callback.onError();
            if (container != null) {
                container.setVisibility(View.GONE);
            }
            return;
        }
        if (!isNetworkAvailable(activity)) {
            callback.onError();
            if (container != null) {
                container.setVisibility(View.GONE);
            }
        }

        new MaxBannerAds(activity, container, adId).load().show(activity, callback);
    }

    // ---------------------- Native -------------------------
    public void showSmallNativeMax(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        new MaxNativeAds(activity, adContainer, adId, R.layout.max_native_custom_ad_small).load().show(activity, callback);
    }

    public void showSmallNativeMaxWithShimmer(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.max_native_custom_ad_small);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_banner);
        maxNativeAds.load().show(activity, callback);
    }

    public void showSmallNativeMaxStyle15(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_small_15).load().show(activity, callback);
    }

    public void showSmallNativeMaxWithShimmerStyle15(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_small_15);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_small_15);
        maxNativeAds.load().show(activity, callback);
    }

    public void showSmallNativeMaxWithShimmerStyle15(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_small_15, styleButtonAds);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_small_15);
        maxNativeAds.load().show(activity, callback);
    }

    public void showSmallNativeMaxStyle16(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_small_16).load().show(activity, callback);
    }

    public void showSmallNativeMaxWithShimmerStyle16(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_small_16);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_small_16);
        maxNativeAds.load().show(activity, callback);
    }

    public void showSmallNativeMaxWithShimmerStyle16(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_small_16, styleButtonAds);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_small_16);
        maxNativeAds.load().show(activity, callback);
    }

    public void showSmallNativeMaxStyle21(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_small_21).load().show(activity, callback);
    }

    public void showSmallNativeMaxWithShimmerStyle21(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_small_21);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_small_21);
        maxNativeAds.load().show(activity, callback);
    }

    public void showSmallNativeMaxWithShimmerStyle21(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_small_21, styleButtonAds);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_small_21);
        maxNativeAds.load().show(activity, callback);
    }

    public void showSmallNativeMaxStyle22(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_small_22).load().show(activity, callback);
    }

    public void showSmallNativeMaxWithShimmerStyle22(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_small_22);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_small_22);
        maxNativeAds.load().show(activity, callback);
    }

    public void showSmallNativeMaxWithShimmerStyle22(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_small_22, styleButtonAds);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_small_22);
        maxNativeAds.load().show(activity, callback);
    }

    public void showMediumNative(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased()) {
            callback.onError();
            return;
        }

        new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_medium).load().show(activity, callback);
    }

    public void showMediumNativeMax(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        new MaxNativeAds(activity, adContainer, adId, R.layout.max_native_custom_ad_medium).load().show(activity, callback);
    }

    public void showMediumNativeMaxWithShimmer(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.max_native_custom_ad_medium);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_medium);
        maxNativeAds.load().show(activity, callback);
    }

    public void showMediumNativeMaxStyle19(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_medium_19).load().show(activity, callback);
    }

    public void showMediumNativeMaxWithShimmerStyle19(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_medium_19);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_medium_19);
        maxNativeAds.load().show(activity, callback);
    }

    public void showMediumNativeMaxWithShimmerStyle19(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_medium_19, styleButtonAds);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_medium_19);
        maxNativeAds.load().show(activity, callback);
    }

    public void showMediumNativeMaxStyle20(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_medium_20).load().show(activity, callback);
    }

    public void showMediumNativeMaxWithShimmerStyle20(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_medium_20);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_medium_20);
        maxNativeAds.load().show(activity, callback);
    }

    public void showMediumNativeMaxWithShimmerStyle20(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_medium_20, styleButtonAds);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_medium_20);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNative(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased()) {
            callback.onError();
            return;
        }

        new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big).load().show(activity, callback);
    }

    public void showBigNativeMax(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        new MaxNativeAds(activity, adContainer, adId, R.layout.max_native_custom_ad_big).load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmer(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.max_native_custom_ad_big);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxStyle1(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_1).load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle1(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_1);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_1);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle1(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_1, styleButtonAds);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_1);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxStyle2(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_2).load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle2(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_2);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_2);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle2(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_2, styleButtonAds);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_2);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxStyle3(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_3).load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle3(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_3);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_3);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle3(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_3, styleButtonAds);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_3);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxStyle4(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_4).load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle4(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_4);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_4);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle4(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_4, styleButtonAds);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_4);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxStyle5(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_5).load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle5(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_5);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_5);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle5(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_5, styleButtonAds);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_5);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxStyle6(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_6).load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle6(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_6);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_6);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle6(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_6, styleButtonAds);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_6);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxStyle7(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_7).load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle7(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_7);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_7);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle7(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_7, styleButtonAds);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_7);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxStyle9(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_9).load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle9(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_9);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_9);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle9(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_9, styleButtonAds);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_9);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxStyle10(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_10).load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle10(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_10);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_10);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle10(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_10, styleButtonAds);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_10);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxStyle11(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_11).load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle11(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_11);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_11);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle11(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_11, styleButtonAds);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_11);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxStyle12(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_12).load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle12(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_12);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_12);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle12(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_12, styleButtonAds);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_12);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxStyle13(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_13).load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle13(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_13);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_13);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle13(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_13, styleButtonAds);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_13);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxStyle14(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_14).load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle14(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_14);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_14);
        maxNativeAds.load().show(activity, callback);
    }

    public void showBigNativeMaxWithShimmerStyle14(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }
        MaxNativeAds maxNativeAds = new MaxNativeAds(activity, adContainer, adId, R.layout.ads_max_native_big_14, styleButtonAds);
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_14);
        maxNativeAds.load().show(activity, callback);
    }
    // ----------------------- Reward -------------------------

    /**
     * show inter ads
     *
     * @param activity
     * @param googleAdsId
     * @param tag         tag name of ads
     */
    public void initRewardAds(@NonNull Activity activity, @NonNull String googleAdsId, @NonNull String tag) {
        if (ProxPurchase.getInstance().checkPurchased()) return;

        RewardAds ads = new GoogleRewardAds(activity, googleAdsId);

        ads.setLoadCallback(new LoadCallback() {
            @Override
            public void onLoadSuccess() {
                adsStorage.put(tag, ads);
            }

            @Override
            public void onLoadFailed() {

            }
        }).load();
    }

    /**
     * show showInterstitial with existed tag name
     *
     * @param activity
     * @param tag      tag name of ads
     * @param callback
     */
    public void showRewardAds(@NonNull Activity activity, String tag, AdsCallback callback, RewardCallback rewardCallback) {
        if (ProxPurchase.getInstance().checkPurchased()) {
            callback.onError();
            return;
        }

        GoogleRewardAds ads = (GoogleRewardAds) adsStorage.get(tag);

        if (ads == null) return;
        if (!ads.isAvailable()) {
            ads.show(activity, callback, rewardCallback);
            return;
        }

        showAdsWithKHub(activity, ads, callback);
    }

    // ------------------- show sequence ads ---------------
    public void initInterstitials(@NonNull Activity activity, String tag, @NonNull InterAds... adss) {
        if (ProxPurchase.getInstance().checkPurchased() || adss.length == 0) return;

        Stack<InterAds> idAdsStack = new Stack<>();
        idAdsStack.addAll(Arrays.asList(adss));

        final InterAds[] loadAds = {idAdsStack.pop()};

        final boolean[] isSuccess = {false};

        LoadCallback callback = new LoadCallback() {
            @Override
            public void onLoadSuccess() {
                isSuccess[0] = true;
            }

            @Override
            public void onLoadFailed() {
                loadAds[0] = idAdsStack.pop();
            }
        };

        while (!idAdsStack.isEmpty() && !isSuccess[0]) {
            loadAds[0].setLoadCallback(callback);
            loadAds[0].load();
        }

        if (isSuccess[0]) adsStorage.put(tag, loadAds[0]);

        idAdsStack.clear();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void initMax(Context context) {
        //bật chế độ LDU cho người dùng ở California
        AdSettings.setDataProcessingOptions( new String[] {"LDU"}, 1, 1000 );

        // Initialize the AppLovin SDK
        AppLovinSdk.getInstance(context).setMediationProvider(AppLovinMediationProvider.MAX);
        AppLovinSdk.getInstance(context).initializeSdk(config -> {
        });
    }

    public void showMaxMediationDebug(Context context) {
        AppLovinSdk.getInstance(context).showMediationDebugger();
    }

    public static class Factory {
        public InterAds getInterAds(@NonNull Activity activity, String adId) {
            return new GoogleInterstitialAd(activity, adId);
        }
    }
}
