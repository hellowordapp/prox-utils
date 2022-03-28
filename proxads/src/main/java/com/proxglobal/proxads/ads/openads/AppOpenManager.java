package com.proxglobal.proxads.ads.openads;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.proxglobal.proxads.ads.ProxInterstitialAd;
import com.proxglobal.proxads.adsv2.ads.ProxAds;
import com.proxglobal.purchase.ProxPurchase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** Prefetches App Open Ads. */
public class AppOpenManager implements LifecycleObserver, Application.ActivityLifecycleCallbacks {
    private static final String LOG_TAG = "AppOpenManager";
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294";
    private static String AD_ID;

    private AppOpenAd appOpenAd = null;

    private AppOpenAd.AppOpenAdLoadCallback loadCallback;

    private ProxOpenAdsApplication myApplication;

    private static boolean isShowingAd = false;
    private long loadTime = 0;

    private Activity currentActivity;

    private List<Class> disableOpenAdsList;

    private boolean openAdsEnable = true;

    private static volatile AppOpenManager INSTANCE;

    public static AppOpenManager getInstance() {
        synchronized (AppOpenManager.class) {
            if(INSTANCE == null) {
                synchronized (AppOpenManager.class) {
                    INSTANCE = new AppOpenManager();
                }
            }
        }
        return INSTANCE;
    }

    private AppOpenManager() {

    }

    /** Constructor */
    public void init(ProxOpenAdsApplication myApplication, String adsID) {
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        disableOpenAdsList = new ArrayList<>();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        AD_ID = adsID;
    }

    /** Request an ad */
    public void fetchAd() {
        // Have unused ad, no need to fetch another.
        if (isAdAvailable()) {
            return;
        }

        loadCallback =
                new AppOpenAd.AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        AppOpenManager.this.appOpenAd = ad;
                        AppOpenManager.this.loadTime = (new Date()).getTime();
                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error.
                     */
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        // Handle the error.
                    }
                };
        AdRequest request = getAdRequest();
        AppOpenAd.load(
                myApplication, AD_ID, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }

    private boolean inDisableOpenAdsList() {
        return disableOpenAdsList.contains(currentActivity.getClass());
    }

    /** Shows the ad if one isn't already showing. */
    public void showAdIfAvailable() {
        if(inDisableOpenAdsList() || !openAdsEnable) return;

        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!isShowingAd && isAdAvailable()) {
            Log.d(LOG_TAG, "Will show ad.");

            KProgressHUD dialog = ProxAds.createKHub(currentActivity);
            dialog.show();

            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            AppOpenManager.this.appOpenAd = null;
                            isShowingAd = false;
                            fetchAd();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            dialog.dismiss();
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            isShowingAd = true;
                            dialog.dismiss();
                        }
                    };

            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd.show(currentActivity);

        } else {
            Log.d(LOG_TAG, "Can not show ad.");
            fetchAd();
        }
    }

    public void disableOpenAds() {
        this.openAdsEnable = false;
    }

    public void enableOpenAds() {
        this.openAdsEnable = true;
    }

    /** Creates and returns ad request. */
    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    /** Utility method that checks if ad exists and can be shown. */
    public boolean isAdAvailable() {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    /** Utility method to check if ad was loaded more than n hours ago. */
    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - this.loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    /** LifecycleObserver methods */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        if (ProxInterstitialAd.isShowing || ProxPurchase.getInstance().checkPurchased()) {
            return;
        }
        showAdIfAvailable();
        Log.d(LOG_TAG, "onStart");
    }

    public void registerDisableOpenAdsAt(Class cls) {
        disableOpenAdsList.add(cls);
    }

    public void removeDisableOpenAdsAt(Class cls) {
        disableOpenAdsList.remove(cls);
    }

    // Activity lifecycle
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        currentActivity = null;
    }
}
