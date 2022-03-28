package com.proxglobal.proxads.adsv2.adcolony;

import android.app.Activity;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;
import com.proxglobal.proxads.adsv2.ads.InterAds;
import com.proxglobal.proxads.adsv2.callback.AdsCallback;

public class ColonyInterstitialAd extends InterAds<AdColonyInterstitial> {
    private ColonyInterstitialCallback mListener;

    private AdColonyInterstitialListener getMListener() {
        if(mListener == null) {
            mListener = new ColonyInterstitialCallback();
        }

        return mListener;
    }

    public ColonyInterstitialAd(String zoneId) {
        super(null, zoneId);
        this.adId = zoneId;
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
    public void specificLoadAdsMethod() {
        AdColony.requestInterstitial(adId, getMListener());
    }

    @Override
    public void specificShowAdsMethod(Activity activity) {
        ads.show();
    }

    // base callback for adcolony
    private class ColonyInterstitialCallback extends AdColonyInterstitialListener {
        @Override
        @CallSuper
        public void onRequestFilled(AdColonyInterstitial adColonyInterstitial) {
            ColonyInterstitialAd.this.ads = adColonyInterstitial;
            ColonyInterstitialAd.this.onLoadSuccess();
        }

        @Override
        @CallSuper
        public void onRequestNotFilled(AdColonyZone zone) {
            super.onRequestNotFilled(zone);
            ColonyInterstitialAd.this.ads = null;
            ColonyInterstitialAd.this.onLoadFailed();
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
