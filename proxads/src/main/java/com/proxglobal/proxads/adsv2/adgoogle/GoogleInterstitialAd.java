package com.proxglobal.proxads.adsv2.adgoogle;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.proxglobal.proxads.adsv2.base.BaseInterAds;
import com.proxglobal.proxads.adsv2.callback.AdsCallback;

public class GoogleInterstitialAd extends BaseInterAds {
    private InterstitialAd interstitialAd;
    private String adId;

    private GoogleInterstitialCallback mListener;

    private FullScreenContentCallback getMListener() {
        if(mListener == null) {
            mListener = new GoogleInterstitialCallback();
        }

        return mListener;
    }

    public GoogleInterstitialAd(Activity activity, String adId) {
        super(activity);
        this.adId = adId;
    }

    public GoogleInterstitialAd load () {
        if (this.interstitialAd != null || inLoading) return this;
        inLoading = true;

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(mActivity.getApplicationContext(), adId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                interstitialAd.setFullScreenContentCallback(getMListener());

                GoogleInterstitialAd.this.interstitialAd = interstitialAd;
                GoogleInterstitialAd.this.onLoadSuccess();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                interstitialAd = null;
                GoogleInterstitialAd.this.onLoadFailed();
            }
        });

        return this;
    }

    @Override
    public void show(Activity activity) {
        if(interstitialAd == null) {
            if(autoReload) load();
            onShowError();
            return;
        }

        interstitialAd.show(activity);
        interstitialAd = null;
    }

    @Override
    public void show(Activity activity,@NonNull AdsCallback adCallback) {
        setAdsCallback(adCallback);

        show(activity);
    }

    @Override
    public boolean isAvailable() {
        return (interstitialAd != null);
    }

    private class GoogleInterstitialCallback extends FullScreenContentCallback {
        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
            GoogleInterstitialAd.this.onShowError();
        }

        @Override
        public void onAdDismissedFullScreenContent() {
            GoogleInterstitialAd.this.onClosed();
        }

        @Override
        public void onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent();
            GoogleInterstitialAd.this.onShowSuccess();
        }

        @Override
        public void onAdImpression() {
            super.onAdImpression();
        }
    }
}
