package com.proxglobal.proxads.adsv2.facade;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAppOptions;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.proxglobal.proxads.ProxUtils;
import com.proxglobal.proxads.R;
import com.proxglobal.proxads.ads.callback.NativeAdCallback2;
import com.proxglobal.proxads.adsv2.adcolony.ColonyInterstitialAd;
import com.proxglobal.proxads.adsv2.adgoogle.GoogleBannerAds;
import com.proxglobal.proxads.adsv2.adgoogle.GoogleInterstitialAd;
import com.proxglobal.proxads.adsv2.adgoogle.GoogleNativeAds;
import com.proxglobal.proxads.adsv2.base.BaseAds;
import com.proxglobal.proxads.adsv2.base.InterAds;
import com.proxglobal.proxads.adsv2.callback.AdsCallback;
import com.proxglobal.proxads.adsv2.callback.LoadCallback;
import com.proxglobal.purchase.ProxPurchase;

import java.util.HashMap;

public final class ProxAds {
    private final HashMap<String, BaseAds> interStorage;
    private static ProxAds INSTANCE = null;

    private ProxAds() {
        interStorage = new HashMap<>();
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

    public void initInterstitial(@NonNull Activity activity, @NonNull String googleAdsId,
                                 @Nullable String colonyZoneId, @NonNull String tag) {
        if(ProxPurchase.getInstance().checkPurchased()) return;

        InterAds ads = new GoogleInterstitialAd(activity, googleAdsId);
        interStorage.put(tag, ads);

        ads.setLoadCallback(new LoadCallback() {
            @Override
            public void onLoadSuccess() {

            }

            @Override
            public void onLoadFailed() {
                if(colonyZoneId != null) {
                    interStorage.put(tag, new ColonyInterstitialAd(colonyZoneId).load());
                }
            }
        }).load();
    }

    public void showInterstitial(@NonNull Activity activity, String tag, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased()) {
            callback.onError();
            return;
        }

        BaseAds ads = interStorage.get(tag);

        if(ads == null) return;
        if(!ads.isAvailable()) {
            ads.show(activity, callback);
            return;
        }

        showAdsWithKHub(activity, ads, callback);
    }

    InterAds splashAds;
    public void  showSplash(@NonNull Activity activity,@NonNull AdsCallback callback,
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
        interStorage.clear();
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
                .setLabel(activity.getString(R.string.loading_ads))
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
            return;
        }

        new GoogleBannerAds(activity, container, adId).load().show(activity, callback);
    }

    // ---------------------- Native -------------------------
    public void showMediumNative(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased()) {
            callback.onError();
            return;
        }

        new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_medium).load().show(activity, callback);
    }

    public void showBigNative(Activity activity, String adId, FrameLayout adContainer, AdsCallback callback) {
        if(ProxPurchase.getInstance().checkPurchased()) {
            callback.onError();
            return;
        }

        new GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big).load().show(activity, callback);
    }
}
