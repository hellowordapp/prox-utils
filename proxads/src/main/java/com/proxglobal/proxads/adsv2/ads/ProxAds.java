package com.proxglobal.proxads.adsv2.ads;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAppOptions;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.proxglobal.proxads.R;
import com.proxglobal.proxads.adsv2.adcolony.ColonyInterstitialAd;
import com.proxglobal.proxads.adsv2.adgoogle.GoogleBannerAds;
import com.proxglobal.proxads.adsv2.adgoogle.GoogleInterstitialAd;
import com.proxglobal.proxads.adsv2.adgoogle.GoogleNativeAds;
import com.proxglobal.proxads.adsv2.adgoogle.GoogleRewardAds;
import com.proxglobal.proxads.adsv2.callback.AdsCallback;
import com.proxglobal.proxads.adsv2.callback.LoadCallback;
import com.proxglobal.proxads.adsv2.callback.RewardCallback;
import com.proxglobal.purchase.ProxPurchase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

public final class ProxAds {
    private final HashMap<String, BaseAds> adsStorage;
    private static ProxAds INSTANCE = null;

    private ProxAds() {
        adsStorage = new HashMap<>();
    }

    private boolean splashDone = false;

    public static ProxAds getInstance() {
        if(INSTANCE == null) {
            synchronized (ProxAds.class) {
                if(INSTANCE == null) {
                    INSTANCE = new ProxAds();
                }
            }
        }

        return INSTANCE;
    }

    /**
     * load inter ads available
     * @param activity
     * @param activity
     * @param googleAdsId
     * @param colonyZoneId pass null if don't want to use this
     * @param tag tag name of ads
     */
    public void initInterstitial(@NonNull Activity activity, @NonNull String googleAdsId,
                                 @Nullable String colonyZoneId, @NonNull String tag) {
        if(ProxPurchase.getInstance().checkPurchased()) return;

        InterAds ads = new GoogleInterstitialAd(activity, googleAdsId);
        adsStorage.put(tag, ads);

        ads.setLoadCallback(new LoadCallback() {
            @Override
            public void onLoadSuccess() {

            }

            @Override
            public void onLoadFailed() {
                if(colonyZoneId != null) {
                    adsStorage.put(tag, new ColonyInterstitialAd(colonyZoneId).load());
                }
            }
        }).load();
    }

    /**
     * show showInterstitial with existed tag name
     * @param activity
     * @param tag tag name of ads
     * @param callback
     */
    public void showInterstitial(@NonNull Activity activity,@NonNull String tag, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased()) {
            callback.onError();
            return;
        }

        BaseAds ads = adsStorage.get(tag);

        if(ads == null) {
            callback.onError();
            return;
        }
        if(!ads.isAvailable()) {
            ads.show(activity, callback);
            return;
        }

        showAdsWithKHub(activity, ads, callback);
    }

    InterAds splashAds;

    /**
     * show splash ads
     * @param activity
     * @param callback
     * @param googleAdsId
     * @param colonyZoneId pass null if don't want to use this
     * @param timeout timeout
     */
    public void showSplash(@NonNull Activity activity,@NonNull AdsCallback callback,
                           @NonNull String googleAdsId,@Nullable String colonyZoneId,
                           int timeout) {
        if(ProxPurchase.getInstance().checkPurchased()) {
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
                if(splashDone) return;
                splashAds.turnOffAutoReload();
                showAdsWithKHub(activity, splashAds, callback);
                handler.removeCallbacksAndMessages(null);

                splashAds = null;
            }

            @Override
            public void onLoadFailed() {
                if(splashDone) return;

                if(colonyZoneId != null) {
                    splashAds = (InterAds) new ColonyInterstitialAd(colonyZoneId).setLoadCallback(new LoadCallback() {
                        @Override
                        public void onLoadSuccess() {
                            if(splashDone) return;
                            splashAds.turnOffAutoReload();
                            showAdsWithKHub(activity, splashAds, callback);
                            handler.removeCallbacksAndMessages(null);

                            splashAds = null;
                        }

                        @Override
                        public void onLoadFailed() {
                            splashAds = null;
                            splashDone = true;
                            handler.removeCallbacksAndMessages(null);

                            callback.onError();
                        }
                    });

                    splashAds.load();
                }
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

    // --------------- config ad colony ------------------------
    /**
     * this method due to configure adcolony in only once time
     * @param activity
     * @param appId
     * @param zoneIds
     */
    public void configure(Activity activity, String appId, String... zoneIds) {
        AdColony.configure(activity, appId, zoneIds);
    }

    /**
     * this method due to configure adcolony in only once time
     * @param application
     * @param appId
     * @param zoneIds
     */
    public void configure(Application application, String appId, String... zoneIds) {
        AdColony.configure(application, appId, zoneIds);
    }

    /**
     * this method due to configure adcolony in only once time
     * @param activity
     * @param appId
     * @param zoneIds
     */
    public void configure(Activity activity, AdColonyAppOptions options, String appId, String... zoneIds) {
        AdColony.configure(activity, options, appId, zoneIds);
    }

    /**
     * this method due to configure adcolony in only once time
     * @param application
     * @param appId
     * @param zoneIds
     */
    public void configure(Application application, AdColonyAppOptions options, String appId, String... zoneIds) {
        AdColony.configure(application, options, appId, zoneIds);
    }

    // ----------------------- Banner -----------------------
    public void showBanner(Activity activity, FrameLayout container, String adId, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased()) {
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

        new GoogleBannerAds(activity, container, adId).load().show(activity, callback);
    }

    // ---------------------- Native -------------------------
    public void showSmallNativeWithShimmerStyle15(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_small_15);
        nativeAds.enableShimmer(R.layout.shimmer_native_small_15);
        nativeAds.load().show(activity, callback);
    }

    public void showSmallNativeWithShimmerStyle15(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_small_15, styleButtonAds);
        nativeAds.enableShimmer(R.layout.shimmer_native_small_15);
        nativeAds.load().show(activity, callback);
    }

    public void showSmallNativeWithShimmerStyle16(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_small_16);
        nativeAds.enableShimmer(R.layout.shimmer_native_small_16);
        nativeAds.load().show(activity, callback);
    }

    public void showSmallNativeWithShimmerStyle16(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_small_16, styleButtonAds);
        nativeAds.enableShimmer(R.layout.shimmer_native_small_16);
        nativeAds.load().show(activity, callback);
    }

    public void showSmallNativeWithShimmerStyle21(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_small_21);
        nativeAds.enableShimmer(R.layout.shimmer_native_small_21);
        nativeAds.load().show(activity, callback);
    }

    public void showSmallNativeWithShimmerStyle21(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_small_21, styleButtonAds);
        nativeAds.enableShimmer(R.layout.shimmer_native_small_21);
        nativeAds.load().show(activity, callback);
    }

    public void showSmallNativeWithShimmerStyle22(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_small_22);
        nativeAds.enableShimmer(R.layout.shimmer_native_small_22);
        nativeAds.load().show(activity, callback);
    }

    public void showSmallNativeWithShimmerStyle22(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_small_22, styleButtonAds);
        nativeAds.enableShimmer(R.layout.shimmer_native_small_22);
        nativeAds.load().show(activity, callback);
    }

    public void showMediumNativeWithShimmerStyle19(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_medium_19);
        nativeAds.enableShimmer(R.layout.shimmer_native_medium_19);
        nativeAds.load().show(activity, callback);
    }

    public void showMediumNativeWithShimmerStyle19(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_medium_19, styleButtonAds);
        nativeAds.enableShimmer(R.layout.shimmer_native_medium_19);
        nativeAds.load().show(activity, callback);
    }

    public void showMediumNativeWithShimmerStyle20(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_medium_20);
        nativeAds.enableShimmer(R.layout.shimmer_native_medium_20);
        nativeAds.load().show(activity, callback);
    }

    public void showMediumNativeWithShimmerStyle20(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_medium_20, styleButtonAds);
        nativeAds.enableShimmer(R.layout.shimmer_native_medium_20);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle1(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_1);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_1);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle1(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_1, styleButtonAds);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_1);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle2(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_2);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_2);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle2(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_2, styleButtonAds);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_2);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle3(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_3);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_3);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle3(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_3, styleButtonAds);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_3);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle4(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_4);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_4);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle4(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_4, styleButtonAds);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_4);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle5(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_5);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_5);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle5(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_5, styleButtonAds);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_5);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle6(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_6);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_6);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle6(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_6, styleButtonAds);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_6);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle7(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_7);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_7);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle7(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_7, styleButtonAds);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_7);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle9(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_9);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_9);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle9(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_9, styleButtonAds);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_9);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle10(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_10);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_10);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle10(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_10, styleButtonAds);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_10);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle11(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_11);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_11);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle11(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_11, styleButtonAds);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_11);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle12(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_12);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_12);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle12(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_12, styleButtonAds);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_12);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle13(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_13);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_13);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle13(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_13, styleButtonAds);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_13);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle14(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_14);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_14);
        nativeAds.load().show(activity, callback);
    }

    public void showBigNativeWithShimmerStyle14(Activity activity, String adId, FrameLayout adContainer, int styleButtonAds, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError();
            if (adContainer != null) {
                adContainer.setVisibility(View.GONE);
            }
            return;
        }

        GoogleNativeAds nativeAds = new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_14, styleButtonAds);
        nativeAds.enableShimmer(R.layout.shimmer_native_big_14);
        nativeAds.load().show(activity, callback);
    }
    // ----------------------- Reward -------------------------
    /**
     * show inter ads
     * @param activity
     * @param googleAdsId
     * @param colonyZoneId pass null if don't want to use this
     * @param tag tag name of ads
     */
    public void initRewardAds(@NonNull Activity activity, @NonNull String googleAdsId,
                                 @Nullable String colonyZoneId, @NonNull String tag) {
        if(ProxPurchase.getInstance().checkPurchased()) return;

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
     * @param activity
     * @param tag tag name of ads
     * @param callback
     */
    public void showRewardAds(@NonNull Activity activity, String tag, AdsCallback callback, RewardCallback rewardCallback) {
        if(ProxPurchase.getInstance().checkPurchased()) {
            callback.onError();
            return;
        }

        GoogleRewardAds ads = (GoogleRewardAds) adsStorage.get(tag);

        if(ads == null) return;
        if(!ads.isAvailable()) {
            ads.show(activity, callback, rewardCallback);
            return;
        }

        showAdsWithKHub(activity, ads, callback);
    }

    // ------------------- show sequence ads ---------------
    public void initInterstitials(@NonNull Activity activity, String tag, @NonNull InterAds... adss) {
        if(ProxPurchase.getInstance().checkPurchased() || adss.length == 0) return;

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

        while(!idAdsStack.isEmpty() && !isSuccess[0]) {
            loadAds[0].setLoadCallback(callback);
            loadAds[0].load();
        }

        if(isSuccess[0]) adsStorage.put(tag, loadAds[0]);

        idAdsStack.clear();
    }

    public static class Factory {
        public InterAds getInterAds(@NonNull Activity activity, String adId, AdsType type) {
            switch (type) {
                case GOOGLE:
                    return new GoogleInterstitialAd(activity, adId);
                case COLONY:
                    return new ColonyInterstitialAd(adId);
            }

            return null;
        }
    }

    public static enum AdsType {
        GOOGLE,
        COLONY
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
