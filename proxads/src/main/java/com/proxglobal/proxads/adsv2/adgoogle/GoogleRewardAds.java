package com.proxglobal.proxads.adsv2.adgoogle;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.proxglobal.proxads.adsv2.ads.RewardAds;
import com.proxglobal.proxads.adsv2.callback.AdsCallback;
import com.proxglobal.proxads.adsv2.callback.RewardCallback;

public class GoogleRewardAds extends RewardAds<RewardedAd> {
    private RewardCallback rewardCallback;
    private GoogleRewardCallback mListener;

    public GoogleRewardAds(Activity activity, String adId) {
        super(activity, adId);
    }

    private FullScreenContentCallback getMListener() {
        if(mListener == null) {
            mListener = new GoogleRewardCallback();
        }

        return mListener;
    }

    @Override
    public void specificLoadAdsMethod() {
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(mActivity, adId, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                onLoadFailed();
            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                super.onAdLoaded(rewardedAd);
                ads = rewardedAd;
                onLoadSuccess();
            }
        });

        ads.setFullScreenContentCallback(getMListener());
    }

    public void show(Activity activity, AdsCallback callback, RewardCallback rewardCallback) {
        this.rewardCallback = rewardCallback;
        super.show(activity, callback);
    }

    @Override
    public void specificShowAdsMethod(Activity activity) {
        ads.show(activity, rewardItem -> {
            if(rewardCallback != null) rewardCallback.getReward(rewardItem);
        });
    }

    @Override
    public void clearAllCallback() {
        super.clearAllCallback();
        this.rewardCallback = null;
    }

    private class GoogleRewardCallback extends FullScreenContentCallback {
        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
            GoogleRewardAds.this.onShowError();
        }

        @Override
        public void onAdDismissedFullScreenContent() {
            GoogleRewardAds.this.onClosed();
        }

        @Override
        public void onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent();
            GoogleRewardAds.this.onShowSuccess();
        }

        @Override
        public void onAdImpression() {
            super.onAdImpression();
        }
    }
}
