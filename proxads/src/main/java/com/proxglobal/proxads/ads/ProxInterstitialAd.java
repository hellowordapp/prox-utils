package com.proxglobal.proxads.ads;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

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
    private Activity mActivity;
    private String adId;
    private boolean isDone = false;
    private int countTime = -1;
    private KProgressHUD loadingDialog;
    public static boolean isShowing = false;
    private boolean autoReload = true;


    public ProxInterstitialAd(Activity activity, String adId) {
        this.mActivity = activity;
        this.adId = adId;
        createKHub(activity);
    }

    public ProxInterstitialAd load () {
        if(ProxPurchase.getInstance().checkPurchased() || interstitialAd != null) return this;

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(mActivity, adId, adRequest, new InterstitialAdLoadCallback() {
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

//    /**
//     * <b>Don't show progress dialog if use static interstitial</b>
//     * @param adCallback
//     */
//    @Deprecated
//    public void show(AdClose adCallback) {
//        boolean isExist = !mActivity.isFinishing();
//
//        if(isExist && loadingDialog.isShowing()) {
//            return;
//        }
//
//        if(ProxPurchase.getInstance().checkPurchased()) {
//            showAds(adCallback);
//            if(autoReload) load();
//        } else {
//            if(isExist) loadingDialog.show();
//
//            new Handler().postDelayed(() -> {
//                if(isExist) loadingDialog.dismiss();
//                showAds(adCallback);
//                if(autoReload) load();
//            }, 700);
//        }
//    }

    public void show(Activity activity, AdClose adCallback) {
        if (ProxPurchase.getInstance().checkPurchased()) {
            adCallback.onAdClose();
            return;
        }

        if(interstitialAd == null) {
            adCallback.onAdClose();
            if(autoReload) load();
            return;
        }

        if(mActivity != activity || loadingDialog == null) {
            createKHub(activity);
        } else {
            if (loadingDialog.isShowing()){
                return;
            }
        }

        loadingDialog.show();
        new Handler().postDelayed(() -> {
            loadingDialog.dismiss();
            showAds(adCallback);
            if(autoReload) load();
        }, 700);
    }

    public void show(Activity activity, AdClose adCallback, int times) {
        ++ countTime;
        if (countTime % times == 0) {
            show(activity, adCallback);
        } else {
            adCallback.onAdClose();
        }
    }

    private void showAds (AdClose adCallback) {
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
        interstitialAd.show(mActivity);
        interstitialAd = null;
    }

    public ProxInterstitialAd loadSplash(int timeout, AdClose adCallback) {
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
        InterstitialAd.load(mActivity, adId, adRequest, new InterstitialAdLoadCallback() {
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

    private void createKHub(Activity activity) {
        loadingDialog = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(activity.getString(R.string.loading_ads))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setAutoDismiss(true)
                .setDimAmount(0.5f);
    }
}
