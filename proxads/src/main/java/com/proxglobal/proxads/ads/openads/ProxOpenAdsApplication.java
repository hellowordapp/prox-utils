package com.proxglobal.proxads.ads.openads;

import android.app.Application;

import com.applovin.sdk.AppLovinMediationProvider;
import com.applovin.sdk.AppLovinSdk;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Arrays;

public abstract class ProxOpenAdsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(
                this,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                    }
                });

        AppOpenManager.getInstance().init(this, getOpenAdsId());

        // Initialize the AppLovin SDK
        AppLovinSdk.getInstance(this).setMediationProvider(AppLovinMediationProvider.MAX);
        AppLovinSdk.getInstance(this).initializeSdk(config -> {

        });
    }

    protected final void disableOpenAdsAt(Class ... clss) {
        for(Class cls : clss) {
            AppOpenManager.getInstance().registerDisableOpenAdsAt(cls);
        }
    }

    protected abstract String getOpenAdsId();
}
