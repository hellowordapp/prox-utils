package com.proxglobal.proxads.adsv2.adcolony;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;
import com.proxglobal.proxads.adsv2.base.BaseInterAds;
import com.proxglobal.proxads.adsv2.callback.AdsCallback;

public class ColonyInterstitialAd extends BaseInterAds {
    private AdColonyInterstitial ads;
    private final String zoneId;

    private ColonyInterstitialCallback mListener;

    private AdColonyInterstitialListener getMListener() {
        if(mListener == null) {
            mListener = new ColonyInterstitialCallback();
        }

        return mListener;
    }

    public ColonyInterstitialAd(String zoneId) {
        this.zoneId = zoneId;
    }

    @Override
    public ColonyInterstitialAd load() {
        if (ads != null || inLoading) return this;
        inLoading = true;

        AdColony.requestInterstitial(zoneId, getMListener());
        Log.d(TAG, "load: colony");
        return this;
    }

    @Override
    public void show(Activity activity) {
        if(ads == null) {
            if(autoReload) load();
            onShowError();
            return;
        }

        ads.show();

        ads = null;
    }

    @Override
    /**
     * show ads if it's available and return result via the callback
     * @param callback
     */
    public void show(Activity activity, @NonNull AdsCallback callback) {
        setAdsCallback(callback);

        show(activity);
    }

    @Override
    public boolean isAvailable() {
        return (ads != null);
    }

    // base callback for adcolony
    private class ColonyInterstitialCallback extends AdColonyInterstitialListener {
        @Override
        @CallSuper
        public void onRequestFilled(AdColonyInterstitial adColonyInterstitial) {
            ColonyInterstitialAd.this.ads = adColonyInterstitial;
            ColonyInterstitialAd.this.onLoadSuccess();

            inLoading = false;
        }

        @Override
        @CallSuper
        public void onRequestNotFilled(AdColonyZone zone) {
            super.onRequestNotFilled(zone);
            ColonyInterstitialAd.this.ads = null;
            ColonyInterstitialAd.this.onLoadFailed();

            inLoading = false;
        }

        @Override
        @CallSuper
        public void onClosed(AdColonyInterstitial ad) {
            super.onClosed(ad);
            ColonyInterstitialAd.this.onClosed();
        }

        @Override
        @CallSuper
        public void onOpened(AdColonyInterstitial ad) {
            super.onOpened(ad);
            ColonyInterstitialAd.this.onShowSuccess();
        }
    }
}
