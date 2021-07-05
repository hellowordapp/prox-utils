package com.proxglobal.proxads;

import android.app.Activity;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.proxglobal.proxads.callback.AdClose;

public class ProxInterstitialAd {
    private InterstitialAd interstitialAd;
    private Activity activity;
    private String adId;

    public ProxInterstitialAd(Activity activity, String adId) {
        this.activity = activity;
        this.adId = adId;
    }

    public ProxInterstitialAd load () {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(activity, adId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                ProxInterstitialAd.this.interstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                interstitialAd = null;
            }
        });
        return this;
    }

    public void show (AdClose adClose) {
        if (interstitialAd == null) {
            adClose.onAdClose();
            return;
        }
        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                adClose.onAdClose();
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                adClose.onAdClose();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });
        interstitialAd.show(activity);
        interstitialAd = null;
    }

    public ProxInterstitialAd loadSplash(int timeout, AdClose adClose) {

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (interstitialAd == null) {
                    adClose.onAdClose();
                }
            }
        };
        handler.postDelayed(runnable, timeout);

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(activity, adId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                ProxInterstitialAd.this.interstitialAd = interstitialAd;
                show(adClose);
                handler.removeCallbacks(runnable);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                adClose.onAdClose();
            }
        });

        return this;
    }
}
