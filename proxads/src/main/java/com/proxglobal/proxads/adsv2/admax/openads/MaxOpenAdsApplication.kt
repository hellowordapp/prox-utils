package com.proxglobal.proxads.adsv2.admax.openads;

import android.app.Application;

public abstract class MaxOpenAdsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MaxOpenAds.getInstance().init(this, getOpenAdsId());
    }

    protected final void disableOpenAdsAt(Class... clss) {
        for (Class cls : clss) {
            MaxOpenAds.getInstance().registerDisableOpenAdsAt(cls);
        }
    }

    protected abstract String getOpenAdsId();
}
