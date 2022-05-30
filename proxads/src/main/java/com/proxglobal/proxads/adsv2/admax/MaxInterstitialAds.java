package com.proxglobal.proxads.adsv2.admax;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.proxglobal.proxads.R;
import com.proxglobal.proxads.adsv2.ads.InterAds;
import com.proxglobal.proxads.adsv2.ads.ProxAds;
import com.proxglobal.proxads.adsv2.callback.AdsCallback;

public class MaxInterstitialAds extends InterAds<MaxInterstitialAd> {
    private KProgressHUD kProgressHUD;

    public MaxInterstitialAds(Activity activity, String adId) {
        super(activity, adId);
    }

    @Override
    public void specificLoadAdsMethod() {
        ads = new MaxInterstitialAd(adId, mActivity);
        ads.setListener(new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                onLoadSuccess();
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                onShowSuccess();
                if (kProgressHUD==null){
                    kProgressHUD = createKHub(mActivity);
                }
                kProgressHUD.show();
            }

            @Override
            public void onAdHidden(MaxAd ad) {
                onClosed();
                if (kProgressHUD!=null){
                    kProgressHUD.dismiss();
                    kProgressHUD = null;
                }
            }

            @Override
            public void onAdClicked(MaxAd ad) {
            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                onShowError();
                onLoadFailed();
                if (ads == null){
                    ads = new MaxInterstitialAd(adId, mActivity);
                }
                ads.loadAd();
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                onShowError();
                if (ads == null){
                    ads = new MaxInterstitialAd(adId, mActivity);
                }
                ads.loadAd();
            }
        });

        ads.loadAd();
    }

    @Override
    public void specificShowAdsMethod(Activity activity) {
        if (ads.isReady()){
            ads.showAd();
        }
    }

    private KProgressHUD createKHub(Activity activity) {
        return KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setAutoDismiss(true)
                .setDimAmount(0.5f);
    }
}
