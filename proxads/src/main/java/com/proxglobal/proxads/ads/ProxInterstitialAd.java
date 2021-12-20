package com.proxglobal.proxads.ads;

import android.app.Activity;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.proxglobal.proxads.R;
import com.proxglobal.proxads.ads.callback.AdCallback;
import com.proxglobal.proxads.ads.callback.AdClose;
import com.proxglobal.purchase.ProxPurchase;

public class ProxInterstitialAd {
    private InterstitialAd interstitialAd;
    private Activity activity;
    private String adId;
    private boolean isDone = false;
    private int countTime = -1;
    private KProgressHUD loadingDialog;
    public static boolean isShowing = false;
    private boolean autoReload = true;


    public ProxInterstitialAd(Activity activity, String adId) {
        this.activity = activity;
        this.adId = adId;
        loadingDialog = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(activity.getString(R.string.loading_ads))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setAutoDismiss(true)
                .setDimAmount(0.5f);
    }

    public ProxInterstitialAd load () {
        if(ProxPurchase.getInstance().checkPurchased()) return this;

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

    public void show (AdClose adCallback) {
        if (loadingDialog.isShowing()){
            return;
        }
        loadingDialog.show();
        new Handler().postDelayed(() -> {
            loadingDialog.dismiss();
            showAds(adCallback);
            if(autoReload) load();
        }, 700);
    }

    public void show(AdClose adCallback, int times) {
        ++ countTime;
        if (countTime % times == 0) {
            show(adCallback);
        } else {
            adCallback.onAdClose();
        }
    }

    private void showAds (AdClose adCallback) {
        if (interstitialAd == null && ProxPurchase.getInstance().checkPurchased()) {
            adCallback.onAdClose();
            return;
        }
        isShowing = true;
        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                isShowing = false;
                adCallback.onAdClose();
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                isShowing = false;
                adCallback.onAdClose();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
                if(adCallback instanceof AdCallback) {
                    ((AdCallback) adCallback).onAdShow();
                }
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }

        });
        interstitialAd.show(activity);
        interstitialAd = null;
    }

    public ProxInterstitialAd loadSplash(int timeout, AdCallback adCallback) {
        if (ProxPurchase.getInstance().checkPurchased()) {
            adCallback.onAdClose();
            return null;
        }

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (interstitialAd == null) {
                    adCallback.onAdClose();
                    isDone = true;
                }
            }
        };
        handler.postDelayed(runnable, timeout);

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(activity, adId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                if (isDone) return;
                ProxInterstitialAd.this.interstitialAd = interstitialAd;
                showAds(adCallback);
                handler.removeCallbacks(runnable);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                adCallback.onAdClose();
                handler.removeCallbacks(runnable);
            }
        });

        return this;
    }

    public void disableAutoReload() {
        this.autoReload = false;
    }

    public void enableAutoReload() {
        this.autoReload = true;
    }
}
