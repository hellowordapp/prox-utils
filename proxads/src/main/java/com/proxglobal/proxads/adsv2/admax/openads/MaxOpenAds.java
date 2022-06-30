package com.proxglobal.proxads.adsv2.admax.openads;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdRevenueListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.proxglobal.proxads.R;
import com.proxglobal.proxads.adsv2.admax.MaxInterstitialAds;
import com.proxglobal.proxads.adsv2.ads.ProxAds;
import com.proxglobal.purchase.ProxPurchase;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.ArrayList;
import java.util.List;

public class MaxOpenAds implements LifecycleObserver, Application.ActivityLifecycleCallbacks {
    private static final String LOG_TAG = "MaxOpenAds";

    private MaxInterstitialAd ads = null;
    private OpenAdsDialog openAdsDialog = null;

    private Activity currentActivity;
    private String adId;

    private boolean isShowingAd = false;
    private boolean isLoadingAd = false;

    private List<Class> disableOpenAdsList;

    private boolean openAdsEnable = true;

    private static volatile MaxOpenAds INSTANCE;

    public static MaxOpenAds getInstance() {
        synchronized (MaxOpenAds.class) {
            if (INSTANCE == null) {
                synchronized (MaxOpenAds.class) {
                    INSTANCE = new MaxOpenAds();
                }
            }
        }
        return INSTANCE;
    }

    private MaxOpenAds() {
    }

    public void init(Application myApplication, String adId) {
        this.adId = adId;
        myApplication.registerActivityLifecycleCallbacks(this);
        disableOpenAdsList = new ArrayList<>();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    /**
     * LifecycleObserver methods
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        Log.d(LOG_TAG, "onStart");
        if (MaxInterstitialAds.isShowing || ProxPurchase.getInstance().checkPurchased() || !ProxAds.isNetworkAvailable(currentActivity)) {
            return;
        }

        show();
    }

    private void show() {
        if (inDisableOpenAdsList() || !openAdsEnable || isLoadingAd || isShowingAd) return;

        if (ads != null && ads.isReady()) {
            Log.d(LOG_TAG, "Will show ad.");

            if (openAdsDialog == null) {
                openAdsDialog = new OpenAdsDialog(currentActivity);
            }
            openAdsDialog.show();
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                ads.showAd();
            }, 1500);
        } else {
            Log.d(LOG_TAG, "Can not show ad.");
            load();
        }
    }

    private void load() {
        ads = new MaxInterstitialAd(adId, currentActivity);
        ads.setRevenueListener(new MaxAdRevenueListener() {
            @Override
            public void onAdRevenuePaid(MaxAd ad) {
                Log.d("ntduc", "OpenAdsMax Revenue: " + ad.getRevenue());
                Log.d("ntduc", "OpenAdsMax NetworkName: " + ad.getNetworkName());
                Log.d("ntduc", "OpenAdsMax AdUnitId: " + ad.getAdUnitId());
                Log.d("ntduc", "OpenAdsMax Placement: " + ad.getPlacement());
                Log.d("ntduc", "-------------------------------------------");
            }
        });
        ads.setListener(new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                Log.d(LOG_TAG, "onAdLoaded");
                isLoadingAd = false;
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                Log.d(LOG_TAG, "onAdDisplayed");
                isShowingAd = true;

                if (openAdsDialog != null) {
                    openAdsDialog.dismiss();
                    openAdsDialog = null;
                }
            }

            @Override
            public void onAdHidden(MaxAd ad) {
                Log.d(LOG_TAG, "onAdHidden");
                isShowingAd = false;
                if (ads == null) {
                    ads = new MaxInterstitialAd(adId, currentActivity);
                }
                ads.loadAd();
                isLoadingAd = true;
            }

            @Override
            public void onAdClicked(MaxAd ad) {
            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                Log.d(LOG_TAG, "onAdLoadFailed");
                isLoadingAd = false;
                isShowingAd = false;
                if (ads == null) {
                    ads = new MaxInterstitialAd(adId, currentActivity);
                }
                ads.loadAd();
                isLoadingAd = true;
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                Log.d(LOG_TAG, "onAdDisplayFailed");
                if (openAdsDialog != null) {
                    openAdsDialog.dismiss();
                    openAdsDialog = null;
                }

                isShowingAd = false;
                if (ads == null) {
                    ads = new MaxInterstitialAd(adId, currentActivity);
                }
                ads.loadAd();
                isLoadingAd = true;
            }
        });

        ads.loadAd();
        isLoadingAd = true;
    }

    public void registerDisableOpenAdsAt(Class cls) {
        disableOpenAdsList.add(cls);
    }

    public void removeDisableOpenAdsAt(Class cls) {
        disableOpenAdsList.remove(cls);
    }

    private boolean inDisableOpenAdsList() {
        return disableOpenAdsList.contains(currentActivity.getClass());
    }

    public void disableOpenAds() {
        this.openAdsEnable = false;
    }

    public void enableOpenAds() {
        this.openAdsEnable = true;
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

    class OpenAdsDialog extends Dialog {

        public OpenAdsDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_open_ads);
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            setCancelable(false);

            NewtonCradleLoading newtonCradleLoading = findViewById(R.id.loading);
            newtonCradleLoading.start();
        }
    }
}
