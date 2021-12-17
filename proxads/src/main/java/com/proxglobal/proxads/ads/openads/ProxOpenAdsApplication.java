package com.proxglobal.proxads.ads.openads;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.proxglobal.purchase.ProxPurchase;

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
    }

    protected final void disableOpenAdsAt(Class ... clss) {
        for(Class cls : clss) {
            AppOpenManager.getInstance().registerDisableOpenAdsAt(cls);
        }
    }

    protected abstract String getOpenAdsId();
}
